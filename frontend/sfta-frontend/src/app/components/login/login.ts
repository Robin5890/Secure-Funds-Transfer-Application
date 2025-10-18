import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service'; 
import { environment } from '../../../environments/environment.development';



@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login implements OnInit{

  userForm!: FormGroup;
  showform = false;
  errorMessage = '';
  feedBackExists = false;

 http = inject(HttpClient);
 router = inject(Router); 
 cookieService = inject(CookieService);
    

 ngOnInit(): void {

  const token = this.cookieService.get('jwt');

  if(token){
    this.router.navigate(["/transfer"]);
    return;
  }
    this.showform = true;
    this.userForm = new FormGroup({
          username: new FormControl('', [Validators.required]),
          password: new FormControl('', [Validators.required])
        })
     }

  onSubmit(){
            
  if (this.userForm.valid) {
      this.http.post<any>(`${environment.apiUrl}/login`, this.userForm.value, { withCredentials: true })
        .subscribe({
          next:(data) => {
            this.errorMessage = data.message || 'Login successful';
            this.feedBackExists = true;
            console.log(data.message);
            if(data.token){
              this.cookieService.set('jwt', data.token, 1, '/');
              this.router.navigate(['transfer']);
            }
          },
          error: (err) => {
            if(err.status === 401){
              this.errorMessage = err.error.message || 'Invalid username or password';
              
            }else{
              this.errorMessage = 'An error occured.';
            }
            this.feedBackExists = true;
            console.error('Login failed', err);
            
          }
        });
    }else{
      this.errorMessage = 'Please fill out all the fields';
      this.feedBackExists = true;
    }
  }

}
