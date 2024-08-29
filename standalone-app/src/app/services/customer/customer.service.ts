import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl = `${environment.apiUrl}/customers`;
  private apiKey = environment.apiKey;

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'content-type': 'application/json',
      'api-key': this.apiKey,
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    });
  }

  getCustomers(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeaders();
    const params = {
      page: page.toString(),
      size: size.toString(),
    };
    return this.http.get<any>(this.apiUrl, { headers, params });
  }
}
