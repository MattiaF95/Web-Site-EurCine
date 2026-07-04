import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { SessionStorageService } from './session-storage.service';

@Injectable({ providedIn: 'root' })
export class AuthTokenService {
  private static readonly TOKEN_KEY = 'eurcine_auth_token';

  private readonly platformId = inject(PLATFORM_ID);
  private readonly storage = inject(SessionStorageService);
  private token: string | null = null;

  getToken(): string | null {
    if (this.token) {
      return this.token;
    }

    if (!isPlatformBrowser(this.platformId) || !this.storage.isBrowser()) {
      return null;
    }

    const storedToken = this.storage.getItem(AuthTokenService.TOKEN_KEY);
    this.token = storedToken && storedToken.trim() ? storedToken : null;
    return this.token;
  }

  setToken(token: string | null): void {
    const normalizedToken = token?.trim() ?? '';
    this.token = normalizedToken ? normalizedToken : null;

    if (!isPlatformBrowser(this.platformId) || !this.storage.isBrowser()) {
      return;
    }

    if (this.token) {
      this.storage.setItem(AuthTokenService.TOKEN_KEY, this.token);
      return;
    }

    this.storage.removeItem(AuthTokenService.TOKEN_KEY);
  }

  clearToken(): void {
    this.setToken(null);
  }
}
