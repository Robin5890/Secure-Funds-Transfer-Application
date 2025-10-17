import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment.development';
import { CookieService } from 'ngx-cookie-service';
import { FormControl, FormGroup, Validators, ɵInternalFormsSharedModule, ReactiveFormsModule } from "@angular/forms";
@Component({
  selector: 'app-transfer',
  imports: [ɵInternalFormsSharedModule, ReactiveFormsModule],
  templateUrl: './transfer.html',
  styleUrl: './transfer.css'
})
export class Transfer implements OnInit{

  http = inject(HttpClient);
  cookie = inject(CookieService);

  username: string = '';
  balance: number = 0;

  transferForm!: FormGroup;

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
    Amount: new FormControl('', [Validators.required, Validators.pattern('^[0-9]+(\\.[0-9]+)?$') , Validators.min(1)])
  })

}

onSubmit() {
  if (this.transferForm.invalid) {
    this.transferForm.markAllAsTouched();
    return;
  }

  const { recipientID, Amount } = this.transferForm.value;
  const token = this.cookie.get('jwt');

  this.http.post(`${environment.apiUrl}/transfer`, { recipientID, Amount }, {
    headers: { Authorization: `Bearer ${token}` },
    withCredentials: true
  }).subscribe({
    next: res => console.log('Transfer successful', res),
    error: err => console.error('Transfer failed', err)
  });
}





}
