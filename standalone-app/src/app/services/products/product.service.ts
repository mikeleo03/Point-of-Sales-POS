import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductSaveDTO, ProductShowDTO, ProductSearchCriteriaDTO } from '../../models/product.model';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;
  private apiKey = environment.apiKey;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'api-key': this.apiKey,
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
  }
  
  private getHeaders_2(): HttpHeaders {
    return new HttpHeaders({
      'api-key': this.apiKey,
      'Authorization': `Bearer ${localStorage.getItem('token')}`
    });
  }

  // Get products by criteria with pagination
  getProducts(criteria: ProductSearchCriteriaDTO, page: number = 0, size: number = 20): Observable<any> {
    const headers = this.getHeaders();
    const params = {
      ...criteria,
      page: page.toString(),
      size: size.toString()
    };
    return this.http.get<any>(this.apiUrl, { headers, params });
  }

  // Add a new product
  addProduct(product: ProductSaveDTO): Observable<ProductShowDTO> {
    const headers = this.getHeaders();
    return this.http.post<ProductShowDTO>(this.apiUrl, product, { headers });
  }

  // Update an existing product
  updateProduct(id: string, product: ProductSaveDTO): Observable<ProductShowDTO> {
    const headers = this.getHeaders();
    return this.http.put<ProductShowDTO>(`${this.apiUrl}/${id}`, product, { headers });
  }

  // Activate a product
  activateProduct(id: string): Observable<ProductShowDTO> {
    const headers = this.getHeaders();
    return this.http.put<ProductShowDTO>(`${this.apiUrl}/active/${id}`, { headers });
  }

  // Deactivate a product
  deactivateProduct(id: string): Observable<ProductShowDTO> {
    const headers = this.getHeaders();
    return this.http.put<ProductShowDTO>(`${this.apiUrl}/deactive/${id}`, { headers });
  }

  uploadExcelFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    const headers = this.getHeaders_2();
    return this.http.post<any>(`${this.apiUrl}/upload`, formData, { headers });
  }
}
