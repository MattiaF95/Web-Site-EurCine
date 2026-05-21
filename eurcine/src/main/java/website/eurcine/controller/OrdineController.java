package website.eurcine.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.eurcine.dto.BigliettoRecord;
import website.eurcine.dto.CreateOrdineRequest;
import website.eurcine.dto.OrdineRecord;
import website.eurcine.service.OrdineService;

@RestController
@RequestMapping("/api/ordini")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdineController {

    private final OrdineService ordineService;

    public OrdineController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @PostMapping
    public OrdineRecord create(@RequestBody CreateOrdineRequest request) {
        return ordineService.createOrdine(request);
    }

    @GetMapping("/{ordineId}")
    public OrdineRecord get(@PathVariable Long ordineId) {
        return ordineService.getOrdine(ordineId);
    }

    @GetMapping("/{ordineId}/biglietti")
    public List<BigliettoRecord> getTickets(@PathVariable Long ordineId) {
        return ordineService.getBiglietti(ordineId);
    }
}
