import { Component, OnInit } from '@angular/core';
import {AuthService} from "../_services/auth.service";

@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrls: ['./default.component.css'],
  providers: [AuthService]
})
export class DefaultComponent implements OnInit {
  constructor(private authService: AuthService) { }

  public username: String;

  ngOnInit() {
    this.authService.checkCredentials();

    this.username = this.authService.getUserName();
  }

  logout() {
    this.authService.logout();
  }
}
