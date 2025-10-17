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

 http = inject(HttpClient);
 router = inject(Router); 
 cookieService = inject(CookieService);
    

 ngOnInit(): void {

  const token1 = this.cookieService.get('jwt');

  if(token1){
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
      this.http.post(`${environment.apiUrl}/login`, this.userForm.value, { responseType: 'text', withCredentials: true })
        .subscribe({
          next:(token: string) => {
            console.log("Login Successful");
            this.cookieService.set('jwt', token, 1, '/');
            this.router.navigate(['transfer']);
          },
          error: err => console.error('Login failed:', err)
        });
    }
  }

}
