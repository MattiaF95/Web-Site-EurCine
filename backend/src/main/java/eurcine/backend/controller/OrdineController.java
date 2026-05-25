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

    @GetMapping("/{ordineId}")
    public OrdineRecord get(@PathVariable Long ordineId, @AuthenticationPrincipal JwtService.UserPrincipal user) {
        return ordineService.getOrdine(ordineId, user);
    }

    @GetMapping("/{ordineId}/biglietti")
    public List<BigliettoRecord> getTickets(
        @PathVariable Long ordineId,
        @AuthenticationPrincipal JwtService.UserPrincipal user
    ) {
        return ordineService.getBiglietti(ordineId, user);
    }
}
