import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient, private cookieService: CookieService){}

  authCookie(): Observable<any> {
    const token = this.cookieService.get('jwt');
    return this.http.get<boolean>(`${environment.apiUrl}/auth`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true
    });
  }
}
