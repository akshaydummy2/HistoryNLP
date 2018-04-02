import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import {CookieService} from 'ngx-cookie';
import {environment} from '../../environments/environment';
import {HistoryEvent} from '../_models/history_event';

@Injectable()
export class HistoryEventService {

  constructor(private httpClient: HttpClient, private cookieService: CookieService) { }

  updateValidity(hEvent: HistoryEvent): Observable<any> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.post(environment.BACKEND_URL + '/api/event/validity', hEvent, {headers: headers});
  }

  search(searchCriteria: string): Observable<HistoryEvent[]> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.post<HistoryEvent[]>(environment.BACKEND_URL + '/api/events/search', searchCriteria, {headers: headers});
  }

  getEvent(id: number): Observable<HistoryEvent> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.get<HistoryEvent>(environment.BACKEND_URL + '/api/event/' + id, {headers: headers});
  }

  getUnknownEvent(): Observable<HistoryEvent> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.get<HistoryEvent>(environment.BACKEND_URL + '/api/events/unknown', {headers: headers});
  }

  getUnknownEventCount(): Observable<number> {
    const headers = new HttpHeaders({'Authorization': 'Bearer ' + this.cookieService.get('plutarch_token')});
    return this.httpClient.get<number>(environment.BACKEND_URL + '/api/events/unknowncount', {headers: headers});
  }

  getValidHistoryEvents(): Observable<HistoryEvent[]> {
    return this.httpClient.get<HistoryEvent[]>(environment.BACKEND_URL + '/public/api/events/map');
  }
}
