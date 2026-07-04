import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthMeResponse, LoginRequest, LoginResponse, RegisterRequest } from '../model/auth.model';
import { AuthTokenService } from './auth-token.service';
import { SessionSandboxService } from './session-sandbox.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly sandbox = inject(SessionSandboxService);
  private readonly authTokenService = inject(AuthTokenService);
  private readonly apiUrl = `${environment.apiBaseUrl}/api/auth`;

  login(payload: LoginRequest): Observable<LoginResponse> {
    if (environment.production && this.sandbox.isActive()) {
      return this.sandboxCall(() => this.sandbox.loginMock(payload));
    }

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, payload);
  }

  register(payload: RegisterRequest): Observable<LoginResponse> {
    if (environment.production && this.sandbox.isActive()) {
      return this.sandboxCall(() => this.sandbox.registerMock(payload));
    }

    return this.http.post<LoginResponse>(`${this.apiUrl}/register`, payload);
  }

  me(): Observable<AuthMeResponse> {
    if (environment.production && this.sandbox.isActive()) {
      const mock = this.sandbox.getMockSession(this.getCurrentToken());
      if (mock) {
        return of(mock);
      }
    }

    return this.http.get<AuthMeResponse>(`${this.apiUrl}/me`);
  }

  logout(): Observable<void> {
    if (environment.production && this.sandbox.isActive()) {
      this.sandbox.logoutMock();
      return of(void 0);
    }

    return this.http.post<void>(`${this.apiUrl}/logout`, {});
  }

  private getCurrentToken(): string | null {
    return this.authTokenService.getToken();
  }

  private sandboxCall<T>(factory: () => T): Observable<T> {
    try {
      return of(factory());
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Operazione non riuscita.';
      return throwError(() => new HttpErrorResponse({
        status: 400,
        statusText: 'Bad Request',
        error: { message }
      }));
    }
  }
}
