import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { AuthStateService } from '../service/auth-state.service';

export const adminAuthGuard: CanActivateFn = () => {
  const platformId = inject(PLATFORM_ID);
  const authService = inject(AuthService);
  const authState = inject(AuthStateService);
  const router = inject(Router);

  // On SSR/prerender avoid server-side redirects; auth check runs in browser.
  if (!isPlatformBrowser(platformId)) {
    return of(true);
  }

  // If login state is already available in-memory, avoid forcing a network check
  // on every admin navigation to prevent false logouts on transient failures.
  if (authState.isLoggedIn()) {
    return of(true);
  }

  return authService.me().pipe(
    map((me) => {
      authState.setFromMe(me);
      return true;
    }),
    catchError(() => {
      authState.clearSession();
      return of(router.createUrlTree(['/home']));
    })
  );
};
