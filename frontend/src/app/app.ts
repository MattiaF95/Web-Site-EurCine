import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgOptimizedImage, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class App {
  private readonly router = inject(Router);

  readonly navItems = [
    { label: 'Home', path: '/home' },
    { label: 'About', path: '/about' },
    { label: 'Programmazione', path: '/programmazione' },
    { label: 'Film', path: '/film' },
    { label: 'Sale', path: '/sale' }
  ] as const;

  readonly searchOpen = signal(false);
  readonly filmSearch = signal('');

  toggleSearch(): void {
    this.searchOpen.set(!this.searchOpen());
  }

  goToFilmSearch(): void {
    const query = this.filmSearch().trim();
    if (!query) {
      return;
    }

    this.searchOpen.set(false);
    void this.router.navigate(['/film'], { queryParams: { q: query } });
  }
}
