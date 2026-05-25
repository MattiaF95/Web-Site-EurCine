package eurcine.backend.service;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import eurcine.backend.dto.BigliettoRecord;
import eurcine.backend.dto.CreateOrdineRequest;
import eurcine.backend.dto.OrdineRecord;
import eurcine.backend.model.Biglietto;
import eurcine.backend.model.Ordine;
import eurcine.backend.model.Posto;
import eurcine.backend.model.Programmazione;
import eurcine.backend.repository.BigliettoRepository;
import eurcine.backend.repository.OrdineRepository;
import eurcine.backend.repository.ProgrammazioneRepository;
import eurcine.backend.repository.PostoRepository;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final BigliettoRepository bigliettoRepository;
    private final ProgrammazioneRepository programmazioneRepository;
    private final PostoRepository postoRepository;
    private final EntityManager entityManager;

    public OrdineService(
        OrdineRepository ordineRepository,
        BigliettoRepository bigliettoRepository,
        ProgrammazioneRepository programmazioneRepository,
        PostoRepository postoRepository,
        EntityManager entityManager
    ) {
        this.ordineRepository = ordineRepository;
        this.bigliettoRepository = bigliettoRepository;
        this.programmazioneRepository = programmazioneRepository;
        this.postoRepository = postoRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public OrdineRecord createOrdine(CreateOrdineRequest request, JwtService.UserPrincipal user) {
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione richiesta.");
        }
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload ordine obbligatorio.");
        }
        if (request.programmazioneId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Programmazione obbligatoria.");
        }

        if (request.postoIds() == null || request.postoIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seleziona almeno un posto.");
        }

        Set<Long> uniqueSeatIds = new LinkedHashSet<>(request.postoIds());
        Programmazione programmazione = programmazioneRepository.findById(request.programmazioneId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Programmazione non trovata."));

        List<Posto> posti = postoRepository.findAllById(uniqueSeatIds);
        if (posti.size() != uniqueSeatIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno o più posti non esistono.");
        }

        for (Posto posto : posti) {
            boolean validSala = posto.getFila().getSala().getId().equals(programmazione.getSala().getId());
            if (!validSala || Boolean.FALSE.equals(posto.getAttivo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Posto non valido per questa programmazione.");
            }

            if (bigliettoRepository.existsByProgrammazioneIdAndPostoId(programmazione.getId(), posto.getId())) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Posto già occupato: " + posto.getFila().getLettera() + posto.getNumero()
                );
            }
        }

        BigDecimal prezzoUnitario = programmazione.getStartAt().getHour() < 18
            ? programmazione.getPrezzoBasePre18()
            : programmazione.getPrezzoBasePost18();

        Ordine ordine = new Ordine();
        ordine.setNumeroOrdine(generateOrderNumber());
        ordine.setNomeCliente((user.getNome() + " " + user.getCognome()).trim());
        ordine.setTotale(prezzoUnitario.multiply(BigDecimal.valueOf(uniqueSeatIds.size())));
        ordine.setUtente(entityManager.getReference(eurcine.backend.model.Utente.class, user.getId()));
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

    public OrdineRecord getOrdineByNumeroOrdine(String numeroOrdine, JwtService.UserPrincipal user) {
        Ordine ordine = findAuthorizedOrderByNumeroOrdine(numeroOrdine, user);
        List<Biglietto> biglietti = bigliettoRepository.findByOrdineId(ordine.getId());
        return toOrdineRecord(ordine, biglietti);
    }

    public List<BigliettoRecord> getBigliettiByNumeroOrdine(String numeroOrdine, JwtService.UserPrincipal user) {
        Ordine ordine = findAuthorizedOrderByNumeroOrdine(numeroOrdine, user);

        return bigliettoRepository.findByOrdineId(ordine.getId()).stream()
            .map(this::toBigliettoRecord)
            .toList();
    }

    public List<OrdineRecord> getOrdiniForUser(JwtService.UserPrincipal user) {
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione richiesta.");
        }

        List<Ordine> ordini = isAdminRole(user.getRuolo())
            ? ordineRepository.findAllByOrderByCreatedAtDesc()
            : ordineRepository.findAllByUtenteIdOrderByCreatedAtDesc(user.getId());

        return ordini.stream()
            .map((ordine) -> toOrdineRecord(ordine, bigliettoRepository.findByOrdineId(ordine.getId())))
            .toList();
    }

    private Ordine findAuthorizedOrderByNumeroOrdine(String numeroOrdine, JwtService.UserPrincipal user) {
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Autenticazione richiesta.");
        }
        if (numeroOrdine == null || numeroOrdine.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero ordine obbligatorio.");
        }

        if (isAdminRole(user.getRuolo())) {
            return ordineRepository.findByNumeroOrdine(numeroOrdine)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordine non trovato."));
        }

        return ordineRepository.findByNumeroOrdineAndUtenteId(numeroOrdine, user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Ordine non accessibile."));
    }

    private boolean isAdminRole(String role) {
        if (role == null) {
            return false;
        }
        String normalized = role.toUpperCase();
        return "ADMIN".equals(normalized) || "SUPER_ADMIN".equals(normalized);
    }

    private OrdineRecord toOrdineRecord(Ordine ordine, List<Biglietto> biglietti) {
        List<BigliettoRecord> bigliettiDto = biglietti.stream().map(this::toBigliettoRecord).toList();
        return new OrdineRecord(
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
