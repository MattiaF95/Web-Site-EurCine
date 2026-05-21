import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { AuthSession } from '../../core/model/auth.model';

@Component({
  selector: 'app-login-success',
  templateUrl: './login-success.component.html',
  styleUrl: './login-success.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginSuccessComponent {
  readonly session = input.required<AuthSession>();
}
