package eurcine.backend.controller;

import eurcine.backend.dto.AdminFilmFormData;
import eurcine.backend.dto.AdminFilmMetaResponse;
import eurcine.backend.dto.AdminFilmSaveRequest;
import eurcine.backend.dto.AdminFilmTitleOption;
import eurcine.backend.service.AdminFilmManagementService;
import java.util.List;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/film")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminFilmManagementController {

    private static final String SESSION_COOKIE_NAME = "eurcine_session";
    private final AdminFilmManagementService adminFilmManagementService;

    public AdminFilmManagementController(AdminFilmManagementService adminFilmManagementService) {
        this.adminFilmManagementService = adminFilmManagementService;
    }

    @GetMapping("/titoli")
    public List<AdminFilmTitleOption> getFilmTitles(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken
    ) {
        return adminFilmManagementService.getFilmTitles(sessionToken);
    }

    @GetMapping("/meta")
    public AdminFilmMetaResponse getMeta(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken
    ) {
        return adminFilmManagementService.getMeta(sessionToken);
    }

    @GetMapping("/{filmId}")
    public AdminFilmFormData getFilmById(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken,
        @PathVariable Long filmId
    ) {
        return adminFilmManagementService.getFilmById(sessionToken, filmId);
    }

    @PostMapping("/aggiungi")
    public AdminFilmFormData createFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken,
        @RequestBody AdminFilmSaveRequest request
    ) {
        return adminFilmManagementService.createFilm(sessionToken, request);
    }

    @PutMapping("/modifica/{filmId}")
    public AdminFilmFormData updateFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken,
        @PathVariable Long filmId,
        @RequestBody AdminFilmSaveRequest request
    ) {
        return adminFilmManagementService.updateFilm(sessionToken, filmId, request);
    }

    @DeleteMapping("/elimina/{filmId}")
    public void deleteFilm(
        @CookieValue(name = SESSION_COOKIE_NAME, required = false) String sessionToken,
        @PathVariable Long filmId
    ) {
        adminFilmManagementService.deleteFilm(sessionToken, filmId);
    }
}
