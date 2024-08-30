import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import {
  InvoiceDTO,
  InvoiceSaveDTO,
  InvoiceSearchCriteriaDTO,
} from '../../models/invoice.model';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {
  private apiUrl = `${environment.apiUrl}/invoices`;
  private apiKey = environment.apiKey;
  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'api-key': this.apiKey,
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    });
  }

  getInvoices(
    criteria: InvoiceSearchCriteriaDTO,
    page: number = 0,
    size: number = 20
  ): Observable<any> {
    const headers = this.getHeaders();

    let params = new HttpParams();

    // Conditionally add 'page' and 'size' parameters only if they are provided
    if (page !== undefined && size !== undefined) {
      params = params.set('page', page.toString()).set('size', size.toString());
    }

    if (criteria.customerName) {
      params = params.set('customerName', criteria.customerName);
    }
    if (criteria.customerId) {
      params = params.set('customerId', criteria.customerId);
    }
    if (criteria.startDate) {
      params = params.set('startDate', criteria.startDate.toISOString());
    }
    if (criteria.endDate) {
      params = params.set('endDate', criteria.endDate.toISOString());
    }
    if (criteria.month) {
      params = params.set('month', criteria.month.toString());
    }
    if (criteria.sortByDate) {
      params = params.set('sortByDate', criteria.sortByDate);
    }
    if (criteria.sortByAmount) {
      params = params.set('sortByAmount', criteria.sortByAmount);
    }

    return this.http.get<any>(this.apiUrl, { headers, params });
  }

  getInvoiceById(id: string): Observable<InvoiceDTO> {
    return this.http.get<InvoiceDTO>(`${this.apiUrl}/${id}`);
  }

  createInvoice(invoice: InvoiceSaveDTO): Observable<InvoiceDTO> {
    const headers = this.getHeaders();
    return this.http.post<InvoiceDTO>(this.apiUrl, invoice, { headers });
  }

  // Update an existing invoice
  updateInvoice(
    id: string,
    invoice: InvoiceSaveDTO
  ): Observable<InvoiceSaveDTO> {
    const headers = this.getHeaders();
    return this.http.put<InvoiceSaveDTO>(`${this.apiUrl}/${id}`, invoice, {
      headers,
    });
  }

  deleteInvoice(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
