package eurcine.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import eurcine.backend.dto.BigliettoRecord;
import eurcine.backend.dto.CreateOrdineRequest;
import eurcine.backend.dto.OrdineRecord;
import eurcine.backend.service.OrdineService;
import eurcine.backend.service.JwtService;

@RestController
@RequestMapping("/api/ordini")
public class OrdineController {

    private final OrdineService ordineService;

    public OrdineController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @GetMapping
    public List<OrdineRecord> getMine(@AuthenticationPrincipal JwtService.UserPrincipal user) {
        return ordineService.getOrdiniForUser(user);
    }

    @PostMapping
    public OrdineRecord create(
        @RequestBody CreateOrdineRequest request,
        @AuthenticationPrincipal JwtService.UserPrincipal user
    ) {
        return ordineService.createOrdine(request, user);
    }

    @GetMapping("/codice/{numeroOrdine}")
    public OrdineRecord getByNumeroOrdine(
        @PathVariable String numeroOrdine,
        @AuthenticationPrincipal JwtService.UserPrincipal user
    ) {
        return ordineService.getOrdineByNumeroOrdine(numeroOrdine, user);
    }

    @GetMapping("/codice/{numeroOrdine}/biglietti")
    public List<BigliettoRecord> getTicketsByNumeroOrdine(
        @PathVariable String numeroOrdine,
        @AuthenticationPrincipal JwtService.UserPrincipal user
    ) {
        return ordineService.getBigliettiByNumeroOrdine(numeroOrdine, user);
    }
}
