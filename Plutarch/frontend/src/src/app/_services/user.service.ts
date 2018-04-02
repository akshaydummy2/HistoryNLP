import { Injectable } from '@angular/core';

import {User} from "../_models/user";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";


@Injectable()
export class UserService {
  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<User[]>(environment.BACKEND_URL + '/api/users');
  }

  getById(id: number) {
    return this.http.get(environment.BACKEND_URL + '/api/users/' + id);
  }

  create(user: User) {
    return this.http.post(environment.BACKEND_URL + '/api/users', user);
  }

  update(user: User) {
    return this.http.put(environment.BACKEND_URL + '/api/users/' + user.id, user);
  }

  delete(id: number) {
    return this.http.delete(environment.BACKEND_URL + '/api/users/' + id);
  }
}
