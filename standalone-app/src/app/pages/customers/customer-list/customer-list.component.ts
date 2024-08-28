import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { Customer } from '../../../models/customer.model';
import { ColDef, GridApi, GridOptions } from 'ag-grid-community';
import { CustomerService } from '../../../services/customer/customer.service';
import { StatusCellRendererComponent } from './status-cell-customer-renderer.component';

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

  public gridOptions: GridOptions = {
    getRowStyle: (params) => {
      if (params.data.status === 'Deactive') {
        return { backgroundColor: '#f5f5f5', color: '#aaa' }; // Dark background for entire row when inactive
      }
      return undefined;
    }
  };

  private gridApi!: GridApi;

  constructor(private customerService: CustomerService) {}
  
  ngOnInit(): void {
    this.initColumnDefs();
    this.loadCustomers();
    window.addEventListener('resize', this.adjustGridForScreenSize.bind(this)); // Listen for resize events

  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.adjustGridForScreenSize(); // Initial check
  }

  onCustomerToggle(event: any) {
    const updatedCustomer = event.data;
    this.customerService.updateCustomer(updatedCustomer).subscribe(() => {
      this.loadCustomers();
    });
  }

  onStatusToggle(customer: any) {
    this.customerService.updateCustomerStatus(customer.id, customer.status).subscribe(() => {
      this.loadCustomers();
    });
  }

  initColumnDefs() {
    this.colDefs = [
      {field: 'id', headerName: 'Id'},
      {field: 'name', headerName: 'Name'},
      {field: 'phoneNumber', headerName: 'Phone Number'},
      {
        field: 'status',
        headerName: 'Status',
        cellClass: 'text-center',
        cellRenderer: StatusCellRendererComponent
      }
    ];
  }

  loadCustomers() {
    this.customerService.getCustomers().subscribe( (customers) => {
      this.customers = customers;
    })
  }

  adjustGridForScreenSize() {
    if (this.gridApi) {
      this.gridApi.sizeColumnsToFit(); // Adjust columns for screen size
    }
  }
}
