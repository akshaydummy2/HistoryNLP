import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {CookieService} from "ngx-cookie";
import {environment} from "../../environments/environment";

@Injectable()
export class AuthService {
  constructor(private router: Router,
              private httpClient: HttpClient,
              private cookieService: CookieService){

  }

  obtainAccessToken(loginData){
    let params = new URLSearchParams();
    params.append('username',loginData.username);
    params.append('password',loginData.password);
    params.append('grant_type','password');
    params.append('client_id','testjwtclientid');
    params.append('client_secret','XY7kmzoNzl100');

    const headers = new HttpHeaders({'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'});

    this.httpClient.post(environment.BACKEND_URL + '/oauth/token', params.toString(), {headers: headers})
      .subscribe(
        (data) => {
          this.saveToken(data, loginData.username);
        },
        (err) => {
          alert('Invalid Credentials');
        }
      );
  }

  saveToken(token, username){
    let expiration = new Date(new Date().getTime() + (1000 * token.expires_in));

    this.cookieService.put("plutarch_token", token.access_token);
    this.cookieService.put("plutarch_user", username);

    this.router.navigate(['/curator/dashboard']);
  }

  checkCredentials(){
    if (!this.cookieService.get('plutarch_token')){
      this.router.navigate(['/login']);
    }
  }

  getUserName() {
    return this.cookieService.get('plutarch_user');
  }

  logout() {
    this.cookieService.remove('plutarch_token');
    this.cookieService.remove('plutarch_user');
    this.router.navigate(['/login']);
  }
}
