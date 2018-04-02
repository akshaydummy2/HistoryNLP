import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import {Resource} from '../_models';
import {CookieService} from 'ngx-cookie';
import {environment} from "../../environments/environment";

@Injectable()
export class ResourceService {

  constructor(private httpClient: HttpClient, private cookieService: CookieService){ }

  addResource(resource: Resource): Observable<any> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.post(environment.BACKEND_URL + '/api/resource', resource, { headers: headers });
  }

  getUnprocessedResourceCount(): Observable<string> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.get<string>(environment.BACKEND_URL + '/api/resources/unprocessedcount', {headers: headers});
  }

}
