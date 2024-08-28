import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { Customer } from '../../../models/customer.model';
import { ColDef } from 'ag-grid-community';
import { CustomerService } from '../../../services/customer/customer.service';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    CommonModule,
    AgGridAngular
  ],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  colDefs: ColDef[] = [];

  public defaultColDef: ColDef = {
    filter: 'agTextColumnFilter',
    floatingFilter: true,
    flex: 1
  };

  constructor(private customerService: CustomerService) {}
  
  ngOnInit(): void {
    this.initColumnDefs();
    this.loadCustomers();
  }

  private initColumnDefs(): void {
    this.colDefs = [
      {field: 'id', headerName: 'Id'},
      {field: 'name', headerName: 'Name'},
      {field: 'phoneNumber', headerName: 'Phone Number'},
      {field: 'status', headerName: 'Status'},
      {field: 'createdAt', headerName: 'Created At'},
      {field: 'updatedAt', headerName: 'Updated At'}
    ];
  }

  loadCustomers() {
    this.customerService.getCustomers().subscribe( (customers) => {
      this.customers = customers;
    })
  }
}
