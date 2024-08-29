import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDTO, CustomerSaveDTO } from '../../models/customer.model';
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

  addCustomer(customerSave: CustomerSaveDTO): Observable<CustomerDTO> {
    const headers = this.getHeaders();
    return this.http.post<CustomerDTO>(this.apiUrl, customerSave, { headers });
  }

  updateCustomer(id: string, customerSave: CustomerSaveDTO): Observable<CustomerDTO> {
    const headers = this.getHeaders();
    return this.http.put<CustomerDTO>(`${this.apiUrl}/${id}`, customerSave, { headers })
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
