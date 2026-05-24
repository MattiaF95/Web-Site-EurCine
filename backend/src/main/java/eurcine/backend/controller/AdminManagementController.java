package eurcine.backend.controller;

import eurcine.backend.dto.AdminFilmFormData;
import eurcine.backend.dto.AdminFilmMetaResponse;
import eurcine.backend.dto.AdminFilmSaveRequest;
import eurcine.backend.dto.AdminFilmTitleOption;
import eurcine.backend.dto.AdminProgrammazioneBatchCreateRequest;
import eurcine.backend.dto.AdminProgrammazioneBatchCreateResponse;
import eurcine.backend.dto.AdminProgrammazioneCatalogResponse;
import eurcine.backend.dto.AdminProgrammazioneCreatedItem;
import eurcine.backend.service.AdminFilmManagementService;
import eurcine.backend.service.AdminProgrammazioneManagementService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminManagementController {

    private final AdminFilmManagementService adminFilmManagementService;
    private final AdminProgrammazioneManagementService adminProgrammazioneManagementService;

    public AdminManagementController(
        AdminFilmManagementService adminFilmManagementService,
        AdminProgrammazioneManagementService adminProgrammazioneManagementService
    ) {
        this.adminFilmManagementService = adminFilmManagementService;
        this.adminProgrammazioneManagementService = adminProgrammazioneManagementService;
    }

    @GetMapping("/film/titoli")
    public List<AdminFilmTitleOption> getFilmTitles() {
        return adminFilmManagementService.getFilmTitles();
    }

    @GetMapping("/film/meta")
    public AdminFilmMetaResponse getMeta() {
        return adminFilmManagementService.getMeta();
    }

    @GetMapping("/film/{filmId}")
    public AdminFilmFormData getFilmById(@PathVariable Long filmId) {
        return adminFilmManagementService.getFilmById(filmId);
    }

    @PostMapping("/film/aggiungi")
    public AdminFilmFormData createFilm(@RequestBody AdminFilmSaveRequest request) {
        return adminFilmManagementService.createFilm(request);
    }

    @PutMapping("/film/modifica/{filmId}")
    public AdminFilmFormData updateFilm(
        @PathVariable Long filmId,
        @RequestBody AdminFilmSaveRequest request
    ) {
        return adminFilmManagementService.updateFilm(filmId, request);
    }

    @DeleteMapping("/film/elimina/{filmId}")
    public void deleteFilm(@PathVariable Long filmId) {
        adminFilmManagementService.deleteFilm(filmId);
    }

    @GetMapping("/programmazione/catalog")
    public AdminProgrammazioneCatalogResponse getProgrammazioneCatalog() {
        return adminProgrammazioneManagementService.getCatalog();
    }

    @PostMapping("/programmazione/aggiungi")
    public AdminProgrammazioneBatchCreateResponse createProgrammazioni(
        @RequestBody AdminProgrammazioneBatchCreateRequest request
    ) {
        return adminProgrammazioneManagementService.createProgrammazioni(request);
    }

    @GetMapping("/programmazione/film/{filmId}")
    public List<AdminProgrammazioneCreatedItem> getProgrammazioniByFilm(
        @PathVariable Long filmId
    ) {
        return adminProgrammazioneManagementService.getProgrammazioniByFilmId(filmId);
    }

    @DeleteMapping("/programmazione/elimina/{programmazioneId}")
    public void deleteProgrammazione(@PathVariable Long programmazioneId) {
        adminProgrammazioneManagementService.deleteProgrammazione(programmazioneId);
    }
}
