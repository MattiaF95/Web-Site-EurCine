import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-film-management',
  imports: [RouterLink],
  templateUrl: './admin-film-management.component.html',
  styleUrl: './admin-film-management.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminFilmManagementComponent {}
