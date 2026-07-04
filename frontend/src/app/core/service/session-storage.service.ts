import { isPlatformBrowser } from '@angular/common';
import { Injectable, PLATFORM_ID, inject } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SessionStorageService {
  private readonly platformId = inject(PLATFORM_ID);

  isBrowser(): boolean {
    return isPlatformBrowser(this.platformId) && typeof sessionStorage !== 'undefined';
  }

  getItem(key: string): string | null {
    if (!this.isBrowser()) {
      return null;
    }

    try {
      return sessionStorage.getItem(key);
    } catch {
      return null;
    }
  }

  setItem(key: string, value: string): void {
    if (!this.isBrowser()) {
      return;
    }

    try {
      sessionStorage.setItem(key, value);
    } catch {
      // Ignore storage quota or availability issues.
    }
  }

  removeItem(key: string): void {
    if (!this.isBrowser()) {
      return;
    }

    try {
      sessionStorage.removeItem(key);
    } catch {
      // Ignore storage availability issues.
    }
  }

  keys(): string[] {
    if (!this.isBrowser()) {
      return [];
    }

    try {
      return Array.from({ length: sessionStorage.length }, (_, index) => sessionStorage.key(index)).filter(
        (key): key is string => !!key
      );
    } catch {
      return [];
    }
  }

  readJson<T>(key: string, fallback: T): T {
    const raw = this.getItem(key);
    if (!raw) {
      return fallback;
    }

    try {
      return JSON.parse(raw) as T;
    } catch {
      return fallback;
    }
  }

  writeJson<T>(key: string, value: T): void {
    this.setItem(key, JSON.stringify(value));
  }

  updateJson<T>(key: string, fallback: T, mutator: (draft: T) => void): T {
    const draft = this.readJson<T>(key, fallback);
    mutator(draft);
    this.writeJson(key, draft);
    return draft;
  }
}
