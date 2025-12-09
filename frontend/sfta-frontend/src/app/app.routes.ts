import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Transfer } from './components/transfer/transfer';
import { UserInfo } from './components/user-info/user-info';
import { History } from './components/history/history';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'transfer', component: Transfer, canActivate: [authGuard] },
  { path: 'user-info', component: UserInfo, canActivate: [authGuard] },
  { path: 'history', component: History, canActivate: [authGuard] },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login', pathMatch: 'full' },
];
