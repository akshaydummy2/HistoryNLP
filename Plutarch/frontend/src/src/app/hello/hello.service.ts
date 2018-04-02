import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import {HttpClient} from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {environment} from '../../environments/environment';


@Injectable()
export class HelloService {

    serverUrl : string = environment.BACKEND_URL + "/api/hello";


  constructor(private httpClient: HttpClient) { }

  getHelloWorldFromService() : string {
      return "hello world from the service";
  }

  getHelloWorldFromJava() : Observable<any> {
      return this.httpClient.get(this.serverUrl);
  }
}
