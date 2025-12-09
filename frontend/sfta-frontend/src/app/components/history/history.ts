import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { environment } from '../../../environments/environment.development';
interface Transaction {
  senderAccount: string;
  receiverAccount: string;
  amount: number;
  timestamp: string;
}

@Component({
  selector: 'app-history',
  imports: [],
  templateUrl: './history.html',
  styleUrl: './history.css',
})
export class History implements OnInit {
  http = inject(HttpClient);
  transactions: Transaction[] = [];
  errorMessage = '';
  username = '';

  ngOnInit(): void {
    this.http
      .get<{ username: string }>(`${environment.apiUrl}/getUserData`, {
        withCredentials: true,
      })
      .subscribe({
        next: (data) => (this.username = data.username),
        error: (err) => console.error('Failed to fetch user info', err),
      });

    this.http
      .get<Transaction[]>(`${environment.apiUrl}/api/getTransferHistory`, {
        withCredentials: true,
      })
      .subscribe({
        next: (data) => (this.transactions = data),
        error: (err) => (this.errorMessage = 'Failed to load transaction history'),
      });
  }
}
