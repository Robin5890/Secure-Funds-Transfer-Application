import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
@Component({
  selector: 'app-user-info',
  imports: [],
  templateUrl: './user-info.html',
  styleUrl: './user-info.css',
})
export class UserInfo implements OnInit {
  http = inject(HttpClient);

  AccNumber = '';
  username = '';
  balance = 0;

  ngOnInit(): void {
    this.http
      .get<{
        username: string;
        balance: number;
        accountNumber: string;
      }>(`${environment.apiUrl}/getUserData`, { withCredentials: true })
      .subscribe({
        next: (data) => {
          this.username = data.username;
          this.AccNumber = data.accountNumber;
          this.balance = data.balance;
        },
        error: (err) => {
          console.error('Error fetching data', err);
        },
      });
  }
}
