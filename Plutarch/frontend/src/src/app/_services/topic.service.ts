import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import {CookieService} from 'ngx-cookie';
import {environment} from "../../environments/environment";
import {Topic} from "../_models/topic";

@Injectable()
export class TopicService {

  constructor(private httpClient: HttpClient,
              private cookieService: CookieService) { }

  getNextEmptyTopicLocation(): Observable<any> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.get(environment.BACKEND_URL + '/api/topic/location', {headers: headers});
  }

  updateLocation(topic: Topic): Observable<any> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.post(environment.BACKEND_URL + '/api/topic/location', topic, {headers: headers});
  }

  getEmptyTopicLocationCount(): Observable<number> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.get<number>(environment.BACKEND_URL + '/api/topics/emptylocationcount', {headers: headers});
  }
}
