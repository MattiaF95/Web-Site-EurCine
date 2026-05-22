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
import org.springframework.web.bind.annotation.CookieValue;
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

    private static final String SESSION_COOKIE_NAME = "eurcine_session";
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
    public List<AdminFilmTitleOption> getFilmTitles(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token
    ) {
        return adminFilmManagementService.getFilmTitles(token);
    }

    @GetMapping("/film/meta")
    public AdminFilmMetaResponse getMeta(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token
    ) {
        return adminFilmManagementService.getMeta(token);
    }

    @GetMapping("/film/{filmId}")
    public AdminFilmFormData getFilmById(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @PathVariable Long filmId
    ) {
        return adminFilmManagementService.getFilmById(token, filmId);
    }

    @PostMapping("/film/aggiungi")
    public AdminFilmFormData createFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @RequestBody AdminFilmSaveRequest request
    ) {
        return adminFilmManagementService.createFilm(token, request);
    }

    @PutMapping("/film/modifica/{filmId}")
    public AdminFilmFormData updateFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @PathVariable Long filmId,
        @RequestBody AdminFilmSaveRequest request
    ) {
        return adminFilmManagementService.updateFilm(token, filmId, request);
    }

    @DeleteMapping("/film/elimina/{filmId}")
    public void deleteFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @PathVariable Long filmId
    ) {
        adminFilmManagementService.deleteFilm(token, filmId);
    }

    @GetMapping("/programmazione/catalog")
    public AdminProgrammazioneCatalogResponse getProgrammazioneCatalog(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token
    ) {
        return adminProgrammazioneManagementService.getCatalog(token);
    }

    @PostMapping("/programmazione/aggiungi")
    public AdminProgrammazioneBatchCreateResponse createProgrammazioni(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @RequestBody AdminProgrammazioneBatchCreateRequest request
    ) {
        return adminProgrammazioneManagementService.createProgrammazioni(token, request);
    }

    @GetMapping("/programmazione/film/{filmId}")
    public List<AdminProgrammazioneCreatedItem> getProgrammazioniByFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @PathVariable Long filmId
    ) {
        return adminProgrammazioneManagementService.getProgrammazioniByFilmId(token, filmId);
    }

    @DeleteMapping("/programmazione/elimina/{programmazioneId}")
    public void deleteProgrammazione(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String token,
        @PathVariable Long programmazioneId
    ) {
        adminProgrammazioneManagementService.deleteProgrammazione(token, programmazioneId);
    }
}
