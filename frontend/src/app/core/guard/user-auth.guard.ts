import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { AuthStateService } from '../service/auth-state.service';

export const userAuthGuard: CanActivateFn = () => {
  const platformId = inject(PLATFORM_ID);
  const authService = inject(AuthService);
  const authState = inject(AuthStateService);
  const router = inject(Router);

  if (!isPlatformBrowser(platformId)) {
    return of(true);
  }

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
