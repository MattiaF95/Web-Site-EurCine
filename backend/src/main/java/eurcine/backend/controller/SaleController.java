package eurcine.backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import eurcine.backend.dto.SalaRecord;
import eurcine.backend.service.SaleService;

@RestController
@RequestMapping("/api/sale")
@CrossOrigin(origins = "http://localhost:4200")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<SalaRecord> getAll() {
        return saleService.getAll();
    }
}
