import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../core/service/auth.service';
import { LoginSuccessComponent } from '../../components/login-success/login-success.component';
import { AuthStateService } from '../../core/service/auth-state.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, LoginSuccessComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly authState = inject(AuthStateService);

  readonly email = signal('');
  readonly password = signal('');
  readonly loading = signal(false);
  readonly error = signal('');
  readonly session = this.authState.session;
  readonly showWelcome = this.authState.showWelcome;

  submit(): void {
    const email = this.email().trim();
    const password = this.password();

    if (!email || !password) {
      this.error.set('Inserisci email e password.');
      return;
    }

    this.loading.set(true);
    this.error.set('');

    this.authService.login({ email, password }).subscribe({
      next: (response) => {
        this.authState.setFromLogin(response);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set((err.error?.message as string) || 'Login non riuscito.');
      }
    });
  }

  logout(): void {
    this.authState.logout();
  }

  closeWelcome(): void {
    this.authState.consumeWelcome();
  }
}
