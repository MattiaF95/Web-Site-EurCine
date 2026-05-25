import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthStateService } from './core/service/auth-state.service';
import { AuthService } from './core/service/auth.service';
import { LoginSuccessComponent } from './components/login-success/login-success.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgOptimizedImage, FormsModule, LoginSuccessComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class App {
  private readonly router = inject(Router);
  private readonly authState = inject(AuthStateService);
  private readonly authService = inject(AuthService);

  readonly navItems = [
    { label: 'Home', path: '/home' },
    { label: 'About', path: '/about' },
    { label: 'Programmazione', path: '/programmazione' },
    { label: 'Film', path: '/film' },
    { label: 'Sale', path: '/sale' }
  ] as const;

  readonly searchOpen = signal(false);
  readonly mobileNavOpen = signal(false);
  readonly filmSearch = signal('');
  readonly profileOpen = signal(false);
  readonly session = this.authState.session;
  readonly isLoggedIn = this.authState.isLoggedIn;
  readonly showWelcome = this.authState.showWelcome;
  readonly loginEmail = signal('');
  readonly loginPassword = signal('');
  readonly loginLoading = signal(false);
  readonly loginError = signal('');

  constructor() {
    this.authState.hydrateFromCookieHint();
  }

  toggleSearch(): void {
    this.profileOpen.set(false);
    this.searchOpen.set(!this.searchOpen());
  }

  toggleMobileNav(): void {
    this.profileOpen.set(false);
    this.mobileNavOpen.set(!this.mobileNavOpen());
  }

  toggleProfile(): void {
    this.mobileNavOpen.set(false);
    this.searchOpen.set(false);
    this.profileOpen.set(!this.profileOpen());
  }

  goToFilmSearch(): void {
    const query = this.filmSearch().trim();
    if (!query) {
      return;
    }

    this.searchOpen.set(false);
    this.mobileNavOpen.set(false);
    void this.router.navigate(['/film'], { queryParams: { q: query } });
  }

  goToManageFilm(): void {
    this.mobileNavOpen.set(false);
    this.profileOpen.set(false);
    void this.router.navigate(['/admin/film']);
  }

  goToManageProgrammazione(): void {
    this.mobileNavOpen.set(false);
    this.profileOpen.set(false);
    void this.router.navigate(['/admin/programmazione']);
  }

  closeMobileNav(): void {
    this.mobileNavOpen.set(false);
  }

  logout(): void {
    this.mobileNavOpen.set(false);
    this.profileOpen.set(false);
    this.authState.logout();
    void this.router.navigate(['/home']);
  }

  goToRegister(): void {
    this.mobileNavOpen.set(false);
    this.profileOpen.set(false);
    void this.router.navigate(['/registrati']);
  }

  goToOrdini(): void {
    this.mobileNavOpen.set(false);
    this.profileOpen.set(false);
    void this.router.navigate(['/ordini']);
  }

  isAdminRole(ruolo?: string): boolean {
    if (!ruolo) {
      return false;
    }
    const normalized = ruolo.toUpperCase();
    return normalized === 'ADMIN' || normalized === 'SUPER_ADMIN';
  }

  onPageClick(event: MouseEvent): void {
    const target = event.target as HTMLElement | null;

    if (this.mobileNavOpen()) {
      const clickedInsideMobileNav = !!target?.closest('.mobile-nav');
      if (!clickedInsideMobileNav) {
        this.mobileNavOpen.set(false);
      }
    }

    if (this.profileOpen()) {
      const clickedInsideProfileMenu = !!target?.closest('.profile-menu');
      if (!clickedInsideProfileMenu) {
        this.profileOpen.set(false);
      }
    }

    if (!this.showWelcome()) {
      return;
    }

    const clickedInsideBanner = !!target?.closest('.login-success-banner');
    if (!clickedInsideBanner) {
      this.authState.consumeWelcome();
    }
  }

  submitLogin(): void {
    const email = this.loginEmail().trim();
    const password = this.loginPassword();
    if (!email || !password) {
      this.loginError.set('Inserisci email e password.');
      return;
    }

    this.loginLoading.set(true);
    this.loginError.set('');

    this.authService.login({ email, password }).subscribe({
      next: (response) => {
        this.authState.setFromLogin(response);
        this.loginLoading.set(false);
        this.loginPassword.set('');
      },
      error: (err: HttpErrorResponse) => {
        this.loginLoading.set(false);
        this.loginError.set((err.error?.message as string) || 'Login non riuscito.');
      }
    });
  }

}
