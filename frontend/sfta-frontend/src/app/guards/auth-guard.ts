import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { tap, catchError, of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.authCookie().pipe(
    tap(isAuth => {
      if (!isAuth) router.navigate(['/login']); 
    }),
    catchError(() => {
      router.navigate(['/login']); 
      return of(false);
    })
  );
};

