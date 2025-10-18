import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { CookieService } from 'ngx-cookie-service';
import { FormControl, FormGroup, Validators, ɵInternalFormsSharedModule, ReactiveFormsModule } from "@angular/forms";
import { Router } from '@angular/router';
@Component({
  selector: 'app-transfer',
  imports: [ɵInternalFormsSharedModule, ReactiveFormsModule],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css'
})
export class Transfer implements OnInit{

  http = inject(HttpClient);
  cookie = inject(CookieService);
  router = inject(Router);

  username: string = '';
  balance: number = 0;
  errorMessage: string = '';
  transferForm!: FormGroup;
  feedBackExists = false;

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

  this.transferForm = new FormGroup({
    recipientID: new FormControl('', [Validators.required, Validators.pattern('^[A-Za-z0-9]+$')]),
    amount: new FormControl('', [Validators.required, Validators.pattern('^[0-9]+(\\.[0-9]+)?$') , Validators.min(1)])
  })

}

onSubmit() {

  if (this.transferForm.invalid) {
    this.errorMessage = 'Please fill out all the fields correctly';
    this.feedBackExists = true;
    return;
  }
  this.errorMessage = 'Transfer in progress.';
  this.feedBackExists = true;

  const { recipientID, amount } = this.transferForm.value;
  const token = this.cookie.get('jwt');

  this.http.post<{message: string}>(`${environment.apiUrl}/transfer`, { recipientID, amount }, {
    headers: { Authorization: `Bearer ${token}` },
    withCredentials: true
  }).subscribe({
    next: data => {
      this.errorMessage = data.message;
      this.transferForm.reset();
      this.balance -= amount;
    },
    error: err => {
      this.errorMessage = err.error.message || 'Transfer Failed'
      this.feedBackExists = true;
    }

  });
}

logout(){
  this.cookie.delete('jwt','/');
  this.router.navigate(['/login']);
}





}
