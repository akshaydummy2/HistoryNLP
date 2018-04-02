import {Component, OnInit} from '@angular/core';

import {HealthService} from '../_services/health.service';
import {AuthService} from '../_services/auth.service';
import {Health} from '../_models/health';

@Component({
  selector: 'app-curator',
  templateUrl: './curator.component.html',
  styleUrls: ['./curator.component.css']
})

export class CuratorComponent implements OnInit {
  health: Health;

  constructor(private healthService: HealthService,
              private authService: AuthService) {

  }

  ngOnInit() {
    this.authService.checkCredentials();
    this.healthService.getHealth().subscribe(
      (healthStatus) => {
        this.health = healthStatus;
      }, (error) => {
        this.health = new Health('DOWN');
      });
  }

  logout() {
    this.authService.logout();
  }

}
