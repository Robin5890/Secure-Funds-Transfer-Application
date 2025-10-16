import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { CookieService } from 'ngx-cookie-service';
@Component({
  selector: 'app-transfer',
  imports: [],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css'
})
export class Transfer implements OnInit{

  http = inject(HttpClient);
  cookie = inject(CookieService);

  username: string = '';
  balance: number = 0;


  ngOnInit(): void {
    const token = this.cookie.get('jwt');
    this.http.get<{ username: string, balance: number }>(`${environment.apiUrl}/getUserData`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true 
  }).subscribe({
      next: data => {
        this.username = data.username;
        this.balance = data.balance;
      },
      error: err => {
        console.error('Failed to fetch user data', err);
      }
  });
}

}
