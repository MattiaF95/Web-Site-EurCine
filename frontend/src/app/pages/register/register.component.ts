import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../core/service/auth.service';
import { AuthStateService } from '../../core/service/auth-state.service';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly authState = inject(AuthStateService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal('');

  readonly form = this.fb.group({
    nome: ['', [Validators.required, Validators.maxLength(64), Validators.pattern(/^[\p{L}\s'-.]+$/u)]],
    cognome: ['', [Validators.required, Validators.maxLength(64), Validators.pattern(/^[\p{L}\s'-.]+$/u)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(160)]],
    password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]]
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.loading.set(true);
    this.error.set('');

    this.authService.register({
      nome: value.nome!.trim(),
      cognome: value.cognome!.trim(),
      email: value.email!.trim(),
      password: value.password!
    }).subscribe({
      next: (response) => {
        this.authService.me().subscribe({
          next: () => {
            this.authState.setFromLogin(response);
            this.loading.set(false);
            void this.router.navigate(['/home']);
          },
          error: () => {
            this.authState.clearSession();
            this.loading.set(false);
            this.error.set('Registrazione completata ma sessione non valida in produzione. Verifica cookie/CORS.');
          }
        });
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        this.error.set((err.error?.message as string) || 'Registrazione non riuscita.');
      }
    });
  }
}
