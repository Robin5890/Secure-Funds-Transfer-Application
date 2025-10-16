import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Transfer } from './components/transfer/transfer';
import { authGuard } from './guards/auth-guard';



export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'transfer', component: Transfer, canActivate: [authGuard] }
];
 
