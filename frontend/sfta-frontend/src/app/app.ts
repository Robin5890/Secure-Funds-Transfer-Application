import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Login } from './components/login/login';
import { Transfer } from './components/transfer/transfer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('sfta-frontend');
}
