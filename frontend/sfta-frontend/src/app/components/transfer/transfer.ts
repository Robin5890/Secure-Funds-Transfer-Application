import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transfer',
  imports: [ReactiveFormsModule],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css',
})
export class Transfer implements OnInit {
  http = inject(HttpClient);
  router = inject(Router);

  username: string = '';
  balance: number = 0;

  transferForm!: FormGroup;
  feedBackExists = false;
  errorMessage = '';
  feedbackType: 'success' | 'error' = 'success';

  ngOnInit(): void {
    this.http
      .get<{
        username: string;
        balance: number;
      }>(`${environment.apiUrl}/getUserData`, { withCredentials: true })
      .subscribe({
        next: (data) => {
          this.username = data.username;
          this.balance = data.balance;
        },
        error: (err) => {
          console.error('Failed to fetch user data', err);
          if (err.status === 401) this.router.navigate(['/login']);
        },
      });

    this.transferForm = new FormGroup({
      recipientID: new FormControl('', [Validators.required, Validators.pattern('^[A-Za-z0-9]+$')]),
      amount: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9]+(\\.[0-9]+)?$'),
        Validators.min(1),
      ]),
    });
  }

  onSubmit() {
    if (this.transferForm.invalid) {
      this.errorMessage = 'Please fill out all the fields correctly';
      this.feedbackType = 'error';
      this.feedBackExists = true;
      return;
    }

    this.errorMessage = 'Transfer in progress.';
    this.feedbackType = 'success';
    this.feedBackExists = true;

    const { recipientID, amount } = this.transferForm.value;

    this.http
      .post<{
        message: string;
      }>(`${environment.apiUrl}/transfer`, { recipientID, amount }, { withCredentials: true })
      .subscribe({
        next: (data) => {
          this.errorMessage = data.message;
          this.feedbackType = 'success';
          this.feedBackExists = true;
          this.transferForm.reset();
          this.balance -= amount;
        },
        error: (err) => {
          this.errorMessage = err.error?.message || 'Transfer Failed';
          this.feedbackType = 'error';
          this.feedBackExists = true;
        },
      });
  }

  logout() {
    this.http.post(`${environment.apiUrl}/api/logout`, {}, { withCredentials: true }).subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login']),
    });
  }

  GotoUserInfo() {
    this.router.navigate(['/user-info']);
  }

  GotoHistory() {
    this.router.navigate(['/history']);
  }
}
