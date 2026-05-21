package website.eurcine.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import website.eurcine.dto.BigliettoRecord;
import website.eurcine.dto.CreateOrdineRequest;
import website.eurcine.dto.OrdineRecord;
import website.eurcine.model.Biglietto;
import website.eurcine.model.Ordine;
import website.eurcine.model.Posto;
import website.eurcine.model.Programmazione;
import website.eurcine.repository.BigliettoRepository;
import website.eurcine.repository.OrdineRepository;
import website.eurcine.repository.ProgrammazioneRepository;
import website.eurcine.repository.PostoRepository;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final BigliettoRepository bigliettoRepository;
    private final ProgrammazioneRepository programmazioneRepository;
    private final PostoRepository postoRepository;

    public OrdineService(
        OrdineRepository ordineRepository,
        BigliettoRepository bigliettoRepository,
        ProgrammazioneRepository programmazioneRepository,
        PostoRepository postoRepository
    ) {
        this.ordineRepository = ordineRepository;
        this.bigliettoRepository = bigliettoRepository;
        this.programmazioneRepository = programmazioneRepository;
        this.postoRepository = postoRepository;
    }

    @Transactional
    public OrdineRecord createOrdine(CreateOrdineRequest request) {
        if (request == null || request.nomeCliente() == null || request.nomeCliente().isBlank()) {
            throw new IllegalArgumentException("Nome cliente obbligatorio.");
        }

        if (request.programmazioneId() == null) {
            throw new IllegalArgumentException("Programmazione obbligatoria.");
        }

        if (request.postoIds() == null || request.postoIds().isEmpty()) {
            throw new IllegalArgumentException("Seleziona almeno un posto.");
        }

        Set<Long> uniqueSeatIds = new LinkedHashSet<>(request.postoIds());
        Programmazione programmazione = programmazioneRepository.findById(request.programmazioneId())
            .orElseThrow(() -> new IllegalArgumentException("Programmazione non trovata."));

        List<Posto> posti = postoRepository.findAllById(uniqueSeatIds);
        if (posti.size() != uniqueSeatIds.size()) {
            throw new IllegalArgumentException("Uno o più posti non esistono.");
        }

        for (Posto posto : posti) {
            boolean validSala = posto.getFila().getSala().getId().equals(programmazione.getSala().getId());
            if (!validSala || Boolean.FALSE.equals(posto.getAttivo())) {
                throw new IllegalArgumentException("Posto non valido per questa programmazione.");
            }

            if (bigliettoRepository.existsByProgrammazioneIdAndPostoId(programmazione.getId(), posto.getId())) {
                throw new IllegalArgumentException("Posto già occupato: " + posto.getFila().getLettera() + posto.getNumero());
            }
        }

        BigDecimal prezzoUnitario = programmazione.getStartAt().getHour() < 18
            ? programmazione.getPrezzoBasePre18()
            : programmazione.getPrezzoBasePost18();

        Ordine ordine = new Ordine();
        ordine.setNumeroOrdine(generateOrderNumber());
        ordine.setNomeCliente(request.nomeCliente().trim());
        ordine.setTotale(prezzoUnitario.multiply(BigDecimal.valueOf(uniqueSeatIds.size())));
        ordineRepository.save(ordine);

        List<Biglietto> biglietti = new ArrayList<>();
        for (Posto posto : posti) {
            Biglietto b = new Biglietto();
            b.setOrdine(ordine);
            b.setProgrammazione(programmazione);
            b.setPosto(posto);
            b.setPrezzo(prezzoUnitario);
            biglietti.add(b);
        }

        List<Biglietto> saved = bigliettoRepository.saveAll(biglietti);
        return toOrdineRecord(ordine, saved);
    }

    public OrdineRecord getOrdine(Long ordineId) {
        Ordine ordine = ordineRepository.findById(ordineId)
            .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato."));
        List<Biglietto> biglietti = bigliettoRepository.findByOrdineId(ordineId);
        return toOrdineRecord(ordine, biglietti);
    }

    public List<BigliettoRecord> getBiglietti(Long ordineId) {
        if (!ordineRepository.existsById(ordineId)) {
            throw new IllegalArgumentException("Ordine non trovato.");
        }

        return bigliettoRepository.findByOrdineId(ordineId).stream()
            .map(this::toBigliettoRecord)
            .toList();
    }

    private OrdineRecord toOrdineRecord(Ordine ordine, List<Biglietto> biglietti) {
        List<BigliettoRecord> bigliettiDto = biglietti.stream().map(this::toBigliettoRecord).toList();
        return new OrdineRecord(
            ordine.getId(),
            ordine.getNumeroOrdine(),
            ordine.getNomeCliente(),
            ordine.getTotale(),
            ordine.getCreatedAt(),
            bigliettiDto
        );
    }

    private BigliettoRecord toBigliettoRecord(Biglietto b) {
        return new BigliettoRecord(
            b.getId(),
            b.getProgrammazione().getId(),
            b.getProgrammazione().getFilm().getTitolo(),
            b.getProgrammazione().getSala().getNome(),
            b.getProgrammazione().getStartAt(),
            b.getPosto().getFila().getLettera(),
            b.getPosto().getNumero(),
            b.getPrezzo()
        );
    }

    private String generateOrderNumber() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORD-" + LocalDateTime.now().format(fmt);
    }
}
