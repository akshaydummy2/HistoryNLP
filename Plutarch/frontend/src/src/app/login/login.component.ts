import { Component, OnInit } from '@angular/core';
import {AuthService} from '../_services';

@Component({
  moduleId: module.id,
  templateUrl: 'login.component.html'
})

export class LoginComponent implements OnInit {
  public loginData = {username: "", password: ""};
  loading: boolean = false;

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
  }

  login() {
    this.authService.obtainAccessToken(this.loginData);
  }
}
