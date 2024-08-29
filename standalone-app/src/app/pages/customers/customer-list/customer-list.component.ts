import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { Customer } from '../../../models/customer.model';
import { ColDef, FirstDataRenderedEvent, GridApi, GridOptions, GridSizeChangedEvent } from 'ag-grid-community';
import { CustomerService } from '../../../services/customer/customer.service';
import { StatusCellRendererComponent } from './status-cell-customer-renderer.component';
import { PhoneNumberFormatPipe } from '../../../core/pipes/phone-number/phone-number-format.pipe';
import { ActionCellRendererComponent } from './action-cell-customer-renderer.component';
import { BrnSheetContentDirective, BrnSheetTriggerDirective } from '@spartan-ng/ui-sheet-brain';
import { HlmSheetComponent, HlmSheetContentComponent, HlmSheetDescriptionDirective, HlmSheetFooterComponent, HlmSheetHeaderComponent, HlmSheetTitleDirective } from '@spartan-ng/ui-sheet-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomerFormComponent } from '../customer-form/customer-form.component';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    CommonModule,
    AgGridAngular,
    PhoneNumberFormatPipe,
    ReactiveFormsModule,
    CustomerFormComponent,

    BrnSheetTriggerDirective,
    BrnSheetContentDirective,
    HlmSheetComponent,
    HlmSheetContentComponent,
    HlmSheetHeaderComponent,
    HlmSheetFooterComponent,
    HlmSheetTitleDirective,
    HlmSheetDescriptionDirective,
    HlmLabelDirective,
    ActionCellRendererComponent,
    StatusCellRendererComponent
  ],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  colDefs: ColDef[] = [];

  public defaultColDef: ColDef = {
    floatingFilter: true
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

  onGridSizeChanged(params: GridSizeChangedEvent) {
    params.api.sizeColumnsToFit();
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }

  onAddCustomer(product: any) {
    this.loadCustomers(); // Reload products after adding
  }

  initColumnDefs() {
    this.colDefs = [
      {
        field: 'name',
        headerName: 'Name',
        filter: 'agTextColumnFilter'},
      {
        field: 'phoneNumber',
        headerName: 'Phone Number',
        filter: 'agTextColumnFilter',
        valueFormatter: (params: any) => new PhoneNumberFormatPipe().transform(params.value)
      },
      {
        field: 'status',
        headerName: 'Status',
        cellClass: 'text-center',
        cellRenderer: StatusCellRendererComponent
      },
      {
        headerName: 'Actions',
        cellClass: 'text-center',
        cellRenderer: ActionCellRendererComponent
      },
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
