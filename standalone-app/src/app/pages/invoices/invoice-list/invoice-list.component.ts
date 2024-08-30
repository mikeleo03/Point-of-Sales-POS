import { Component, OnInit } from '@angular/core';
import { InvoiceService } from '../../../services/invoice.service';
import { AgGridAngular, AgGridModule } from 'ag-grid-angular';
import {
  ColDef,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
  GridOptions,
  GridApi,
  IDatasource,
  IGetRowsParams,
} from 'ag-grid-community';
import { Router } from '@angular/router';
import { DateFormatPipe } from '../../../core/pipes/date-format.pipe';
import { PriceFormatPipe } from '../../../core/pipes/price-format.pipe';
import { TimeAgoPipe } from '../../../core/pipes/time-ago.pipe';
import {
  HlmSheetComponent,
  HlmSheetContentComponent,
  HlmSheetHeaderComponent,
  HlmSheetFooterComponent,
  HlmSheetTitleDirective,
  HlmSheetDescriptionDirective,
} from '@spartan-ng/ui-sheet-helm';
import {
  BrnSheetContentDirective,
  BrnSheetTriggerDirective,
} from '@spartan-ng/ui-sheet-brain';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { InvoiceDTO } from '../../../models/invoice.model';
import { InvoiceFormComponent } from '../invoice-form/invoice-form.component';
import { ActionCellRendererComponent } from './action-cell-renderer.component';
@Component({
  selector: 'app-invoice-list',
  standalone: true,
  imports: [
    AgGridAngular,
    TimeAgoPipe,
    DateFormatPipe,
    PriceFormatPipe,
    CommonModule,
    ReactiveFormsModule,
    AgGridModule,
    InvoiceFormComponent,

    BrnSheetTriggerDirective,
    BrnSheetContentDirective,
    HlmSheetComponent,
    HlmSheetContentComponent,
    HlmSheetHeaderComponent,
    HlmSheetFooterComponent,
    HlmSheetTitleDirective,
    HlmSheetDescriptionDirective,
    HlmLabelDirective,
  ],
  templateUrl: './invoice-list.component.html',
  styleUrl: './invoice-list.component.css',
  providers: [InvoiceService],
})
export class InvoiceListComponent implements OnInit {
  invoices: InvoiceDTO[] = [];
  colDefs: ColDef[] = [
    {
      headerName: 'Customer',
      field: 'customer.name',
      headerClass: 'text-center',
      minWidth: 200,
    },
    {
      headerName: 'Total Amount',
      field: 'amount',
      sortable: true,
      filter: 'agNumberColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        new PriceFormatPipe().transform(params.value),
    },
    {
      headerName: 'Times',
      field: 'date',
      sortable: true,
      filter: 'agDateColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        `
        ${new DateFormatPipe().transform(
          params.value
        )} - ${new TimeAgoPipe().transform(params.value)},
        `,
    },
    {
      headerName: 'Actions',
      cellRenderer: ActionCellRendererComponent,
      headerClass: 'text-center',
      minWidth: 250,
      cellClass: 'text-center',
    },
  ];

  public defaultColDef: ColDef = {
    floatingFilter: true,
    flex: 1,
    sortable: true, // Enable client-side sorting
    filter: true, // Enable client-side filtering
  };

  // dataSource: IDatasource = {
  //   getRows: (params: IGetRowsParams) => {
  //     this.invoiceService
  //       .getInvoices(
  //         {},
  //         this.gridApi.paginationGetCurrentPage(),
  //         this.gridApi.paginationGetPageSize()
  //       )
  //       .subscribe(
  //         (response) => {
  //           params.successCallback(
  //             response['content'],
  //             response['page']['totalElements']
  //           );
  //         },
  //         (error) => {
  //           console.error('Error fetching invoices:', error);
  //         }
  //       );
  //   },
  // };

  public gridOptions: GridOptions = {
    getRowStyle: (params) => {
      return undefined;
    },
    pagination: true, // Enable client-side pagination
    paginationPageSize: 10, // Default page size
    context: { componentParent: this },
  };

  private gridApi!: GridApi;

  constructor(private invoiceService: InvoiceService, private router: Router) {}

  ngOnInit() {
    this.loadInvoices();
    window.addEventListener('resize', this.adjustGridForScreenSize.bind(this)); // Listen for resize events
  }

  loadInvoices() {
    this.invoiceService.getInvoices({}, 0, 100).subscribe((response) => {
      console.log(response);
      this.invoices = response.content;
    });
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.adjustGridForScreenSize(); // Initial check
  }

  onAddInvoice(invoice: any) {
    this.loadInvoices();// Reload invoices after adding
  }

  onInvoiceEdited(invoice: any) {
    this.loadInvoices();// Reload invoices after edited
  }

  onViewInvoice(invoiceData: any) {
    // Open a modal or navigate to a page to view the invoice details
    console.log('Viewing invoice:', invoiceData);
    // Your logic to view the invoice details here
  }

  onGridSizeChanged(params: GridSizeChangedEvent) {
    params.api.sizeColumnsToFit(); // Ensure columns fit the grid width
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit(); // Fit columns on initial render
  }

  adjustGridForScreenSize() {
    if (this.gridApi) {
      this.gridApi.sizeColumnsToFit(); // Adjust columns for screen size
    }
  }
}
