import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, switchMap } from 'rxjs';
import { Customer } from '../../models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = 'http://localhost:8080/customers'

  constructor(private http: HttpClient) { }

  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.apiUrl);
  }

  getCustomerById(id: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`);
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

  addCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer);
  }

  updateCustomer(customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${customer.id}`, customer)
  }

  updateCustomerStatus(id: string, status: string): Observable<Customer> {
    return this.getCustomerById(id).pipe(
      switchMap( customer => {
        const updatedCustomer: Customer = { ...customer, status: status, updatedAt: new Date() };  
        return this.updateCustomer(updatedCustomer);
      })
    );
  }
}
