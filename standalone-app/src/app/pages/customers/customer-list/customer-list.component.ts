import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { Customer } from '../../../models/customer.model';
import { ColDef, FirstDataRenderedEvent, GridApi, GridOptions, GridSizeChangedEvent, IDatasource, IGetRowsParams } from 'ag-grid-community';
import { CustomerService } from '../../../services/customer/customer.service';
import { StatusCellRendererComponent } from './status-cell-customer-renderer.component';
import { PhoneNumberFormatPipe } from '../../../core/pipes/phone-number/phone-number-format.pipe';
import { ActionCellRendererComponent } from './action-cell-customer-renderer.component';
import { BrnSheetContentDirective, BrnSheetTriggerDirective } from '@spartan-ng/ui-sheet-brain';
import { HlmSheetComponent, HlmSheetContentComponent, HlmSheetDescriptionDirective, HlmSheetFooterComponent, HlmSheetHeaderComponent, HlmSheetTitleDirective } from '@spartan-ng/ui-sheet-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomerFormComponent } from '../customer-form/customer-form.component';
import { CustomerDetailComponent } from '../customer-detail/customer-detail.component';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [
    CommonModule,
    AgGridAngular,
    PhoneNumberFormatPipe,
    ReactiveFormsModule,
    CustomerFormComponent,
    CustomerDetailComponent,

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
  defaultPageSize = 15;

  private gridApi!: GridApi;

  public defaultColDef: ColDef = {
    floatingFilter: true,
    flex: 1
  };

  dataSource: IDatasource = {
    getRows: (params: IGetRowsParams) => {
      this.customerService.getCustomers(this.gridApi.paginationGetCurrentPage(), this.gridApi.paginationGetPageSize()).subscribe( response => {
        params.successCallback(
          response["content"], response["page"]["totalElements"]
        );
      })
    }
  }

  public gridOptions: GridOptions = {
    getRowStyle: (params) => {
      if (params.data && params.data.status === 'DEACTIVE') {
        return { backgroundColor: '#f5f5f5', color: '#aaa' }; // Dark background for entire row when inactive
      }
      return { backgroundColor: '#FFFFFF', color: '#000000' };
    },
    rowModelType: 'infinite',
    datasource: this.dataSource,
    context: { componentParent: this }
  };

  constructor(private customerService: CustomerService) {}
  
  ngOnInit(): void {
    this.initColumnDefs();
    this.loadCustomers();
    window.addEventListener('resize', this.adjustGridForScreenSize.bind(this)); // Listen for resize events
  }

  loadCustomers() {
    this.customerService.getCustomers(0, 20).subscribe( (customer) => {
      console.log(customer);
      this.customers = customer;
    })
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.adjustGridForScreenSize(); // Initial check
  }

  onStatusToggle(customer: any) {
    if (customer.status === 'ACTIVE') {
      this.customerService.updateCustomerStatusActive(customer.id).subscribe(() => {
        setTimeout(() => {
          this.gridApi.refreshInfiniteCache();
        }, 0);
      });
    } else if (customer.status === 'DEACTIVE') {
      this.customerService.updateCustomerStatusDeactive(customer.id).subscribe(() => {
        setTimeout(() => {
          this.gridApi.refreshInfiniteCache();
        }, 0);
      });
    }
  }
  
  onGridSizeChanged(params: GridSizeChangedEvent) {
    params.api.sizeColumnsToFit();
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }

  onAddCustomer(customer: any) {
    this.gridApi.refreshInfiniteCache(); // Refresh the data cache after adding a product
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
        // valueFormatter: (params: any) => new PhoneNumberFormatPipe().transform(params.value)
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

  adjustGridForScreenSize() {
    if (this.gridApi) {
      this.gridApi.sizeColumnsToFit(); // Adjust columns for screen size
    }
  }
}
