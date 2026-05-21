import { Injectable, PLATFORM_ID, computed, inject, signal } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { catchError, of } from 'rxjs';
import { AuthService } from './auth.service';
import { AuthMeResponse, LoginResponse, AuthSession } from '../model/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private readonly authService = inject(AuthService);
  private readonly platformId = inject(PLATFORM_ID);

  private readonly sessionSignal = signal<AuthSession | null>(null);
  private readonly showWelcomeSignal = signal(false);
  private hydrateStarted = false;

  readonly session = this.sessionSignal.asReadonly();
  readonly isLoggedIn = computed(() => !!this.sessionSignal());
  // Mostra il messaggio di benvenuto solo subito dopo il login.
  readonly showWelcome = this.showWelcomeSignal.asReadonly();

  hydrateFromStorage(): void {
    // Si avvia una sola volta: controlla il cookie HttpOnly e recupera l'utente loggato.
    if (this.hydrateStarted || !isPlatformBrowser(this.platformId)) {
      return;
    }
    this.hydrateStarted = true;

    this.authService.me().pipe(
      catchError(() => {
        this.clearSession();
        return of(null);
      })
    ).subscribe((me) => {
      if (!me) {
        return;
      }
      this.sessionSignal.set(this.toSession(me));
    });
  }

  setFromLogin(response: LoginResponse): void {
    const session: AuthSession = {
      adminId: response.adminId,
      nome: response.nome,
      cognome: response.cognome,
      email: response.email,
      ruolo: response.ruolo,
      message: response.message
    };
    this.sessionSignal.set(session);
    // Attiva il banner di successo solo nel momento del login.
    this.showWelcomeSignal.set(true);
  }

  logout(): void {
    // Pulisce sempre lo stato locale, anche se la chiamata logout fallisce.
    this.authService.logout().pipe(
      catchError(() => of(void 0))
    ).subscribe(() => {
      this.clearSession();
    });
  }

  clearSession(): void {
    // Reset completo dello stato locale (sessione + banner).
    this.sessionSignal.set(null);
    this.showWelcomeSignal.set(false);
  }

  consumeWelcome(): void {
    // Nasconde il banner di successo senza fare logout.
    this.showWelcomeSignal.set(false);
  }

  private toSession(me: AuthMeResponse): AuthSession {
    return {
      adminId: me.adminId,
      nome: me.nome,
      cognome: me.cognome,
      email: me.email,
      ruolo: me.ruolo,
      message: me.message
    };
  }
}
