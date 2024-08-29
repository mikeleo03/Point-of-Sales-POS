import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer, CustomerDTO } from '../../models/customer.model';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = `${environment.apiUrl}/customers`;
  private apiKey = environment.apiKey;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'content-type': 'application/json',
      'api-key': this.apiKey,
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
  }

  getCustomers(page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeaders();
    const params = {
      page: page.toString(),
      size: size.toString()
    };
    return this.http.get<any>(this.apiUrl, { headers, params });
  }

  getLastCustomerId(): Observable<number> {
    return new Observable<number>(observer => {
      this.http.get<any[]>(this.apiUrl).subscribe(customer => {
        const lastId = customer.reduce((max, customer) => Math.max(max, customer.id), 0);
        observer.next(lastId);
        observer.complete();
      });
    });
  }

  addCustomer(customer: CustomerDTO): Observable<CustomerDTO> {
    const headers = this.getHeaders();
    return this.http.post<CustomerDTO>(this.apiUrl, customer, { headers });
  }

  updateCustomer(customer: Customer): Observable<Customer> {
    const headers = this.getHeaders();
    return this.http.put<Customer>(`${this.apiUrl}/${customer.id}`, customer, { headers })
  }

  updateCustomerStatusActive(id: string): Observable<CustomerDTO> {
    const headers = this.getHeaders();
    return this.http.put<CustomerDTO>(`${this.apiUrl}/active/${id}`, {}, { headers });
  }

  updateCustomerStatusDeactive(id: string): Observable<CustomerDTO> {
    const headers = this.getHeaders();
    return this.http.put<CustomerDTO>(`${this.apiUrl}/deactive/${id}`, {}, { headers });
  }
}
