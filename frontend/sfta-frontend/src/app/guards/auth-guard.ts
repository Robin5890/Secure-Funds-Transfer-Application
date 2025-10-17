import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

export const authGuard: CanActivateFn = (route, state) => {
  const cookie = inject(CookieService); 
  const router = inject(Router);

  const token = cookie.get('jwt');
  if(!token){
    router.navigate(['/login']);
    return false;
  }

  try{
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expired = Date.now() >= payload.expires * 1000;
    if (expired) {
      cookie.delete('jwt', '/');
      router.navigate(['/login']);
      return false;
    }
    return true;
  } catch {
    router.navigate(['/login']);
    return false;
  } 
};

