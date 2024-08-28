import { Component, OnInit } from '@angular/core';
import { InvoiceService } from '../../../services/invoice.service';
import { AgGridAngular, AgGridModule } from 'ag-grid-angular';
import {
  ColDef,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
  GridOptions,
  GridApi,
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
import { Invoice } from '../../../models/invoice.model';
import { InvoiceFormComponent } from '../invoice-form/invoice-form.component';
import { ActionCellRendererComponent } from './action-cell-renderer.component';
import { StatusCellRendererComponent } from './status-cell-renderer.component';

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
})
export class InvoiceListComponent implements OnInit {
  invoices: Invoice[] = [];
  colDefs: ColDef[] = [
    { field: 'id', headerClass: 'text-center', minWidth: 200 },
    { field: 'customer.name', headerClass: 'text-center', minWidth: 200 },
    {
      field: 'amount',
      sortable: true,
      filter: 'agNumberColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        new PriceFormatPipe().transform(params.value),
    },
    {
      field: 'status',
      headerClass: 'text-center',
      cellClass: 'text-center',
      minWidth: 200,
      cellRenderer: StatusCellRendererComponent,
    },
    {
      field: 'date',
      sortable: true,
      filter: 'agDateColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        new DateFormatPipe().transform(params.value),
    },
    {
      headerName: 'Time Ago',
      field: 'date',
      sortable: true,
      filter: 'agDateColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        new TimeAgoPipe().transform(params.value), // Use the new pipe here
    },
    {
      headerName: 'Actions',
      cellRenderer: ActionCellRendererComponent,
      headerClass: 'text-center',
      minWidth: 225,
      cellClass: 'text-center',
    },
  ];

  public defaultColDef: ColDef = {
    filter: 'agTextColumnFilter',
    floatingFilter: true,
    resizable: true,
  };

  public gridOptions: GridOptions = {
    getRowStyle: (params) => {
      if (!params.data.status) {
        return { backgroundColor: '#f5f5f5', color: '#aaa' }; // Dark background for entire row when inactive
      }
      return undefined;
    },
  };

  private gridApi!: GridApi;

  constructor(private invoiceService: InvoiceService, private router: Router) {}

  ngOnInit() {
    this.loadInvoices();
    window.addEventListener('resize', this.adjustGridForScreenSize.bind(this)); // Listen for resize events
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.adjustGridForScreenSize(); // Initial check
  }

  loadInvoices() {
    this.invoiceService.getInvoices().subscribe((invoices) => {
      this.invoices = invoices;
    });
  }

  onAddInvoice(invoice: any) {
    this.loadInvoices(); // Reload invoices after adding
  }

  onInvoiceEdited(invoice: any) {
    this.loadInvoices(); // Reload invoices after edited
  }

  onViewInvoice(invoiceData: any) {
    // Open a modal or navigate to a page to view the invoice details
    console.log('Viewing invoice:', invoiceData);
    // Your logic to view the invoice details here
  }

  onDeleteInvoice(invoice: any) {
    if (confirm(`Are you sure you want to delete this invoice?`)) {
      this.invoiceService.deleteInvoice(invoice.id).subscribe(() => {
        this.loadInvoices();
      });
    }
  }

  onStatusToggle(invoice: any) {
    this.invoiceService
      .updateInvoiceStatus(invoice.id, invoice.status)
      .subscribe(() => {
        this.loadInvoices();
      });
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
