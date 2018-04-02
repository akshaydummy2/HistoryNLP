import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Health} from '../_models/health';


@Injectable()
export class HealthService {
  constructor(private http: HttpClient) { }

  getHealth() {
    return this.http.get<Health>(environment.BACKEND_URL + '/health');
  }
}
