import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
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
import { ProductFormComponent } from '../product-form/product-form.component';
import { StatusCellRendererComponent } from './status-cell-renderer.component';
import { ActionCellRendererComponent } from './action-cell-renderer.component';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    AgGridAngular,
    DateFormatPipe,
    PriceFormatPipe,
    CommonModule,
    ReactiveFormsModule,
    AgGridModule,
    ProductFormComponent,
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
    StatusCellRendererComponent,
  ],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {
  private gridApi!: GridApi;
  defaultPageSize = 15;

  products: Product[] = [];
  colDefs: ColDef[] = [
    { field: 'name', headerClass: 'text-center', minWidth: 200 },
    {
      field: 'price',
      sortable: true,
      filter: 'agNumberColumnFilter',
      headerClass: 'text-center',
      minWidth: 150,
      valueFormatter: (params: any) =>
        new PriceFormatPipe().transform(params.value),
    },
    {
      field: 'quantity',
      sortable: true,
      filter: 'agNumberColumnFilter',
      headerClass: 'text-center',
      cellClass: 'text-center',
      minWidth: 150,
    },
    {
      field: 'status',
      headerClass: 'text-center',
      cellClass: 'text-center',
      minWidth: 200,
      cellRenderer: StatusCellRendererComponent,
    },
    {
      field: 'createdAt',
      sortable: true,
      filter: 'agDateColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        new DateFormatPipe().transform(params.value),
    },
    {
      field: 'updatedAt',
      sortable: true,
      filter: 'agDateColumnFilter',
      headerClass: 'text-center',
      minWidth: 200,
      valueFormatter: (params: any) =>
        new DateFormatPipe().transform(params.value),
    },
    {
      headerName: 'Actions',
      cellRenderer: ActionCellRendererComponent,
      headerClass: 'text-center',
      minWidth: 150,
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
  //     const page = params.startRow / this.defaultPageSize;
  //     const pageSize = params.endRow - params.startRow;

  //     this.productService.getProducts({}, page, pageSize).subscribe(response => {
  //       params.successCallback(response["content"], response["page"]["totalElements"]);
  //     }, error => {
  //       params.failCallback();
  //     });
  //   }
  // }

  onPaginationChanged(event: any) {
    // Ensure gridApi is available and then refresh data if needed
    if (this.gridApi) {
      this.gridApi.refreshInfiniteCache();
    }
  }

  public gridOptions: GridOptions = {
    getRowStyle: (params) => {
      if (params.data && params.data.status === 'DEACTIVE') {
        return { backgroundColor: '#f5f5f5', color: '#aaa' }; // Dark background for entire row when inactive
      }
      return undefined; // Return undefined to apply default styling
    },
    pagination: true, // Enable client-side pagination
    paginationPageSize: 10, // Default page size
    context: { componentParent: this },
  };

  constructor(private productService: ProductService, private router: Router) {}

  ngOnInit() {
    this.loadProducts();
    window.addEventListener('resize', this.adjustGridForScreenSize.bind(this)); // Listen for resize events
  }

  loadProducts() {
    this.productService.getProducts({}, 0, 100).subscribe((response) => {
      console.log(response);
      this.products = response.content;
    });
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
  }

  onAddProduct(product: any) {
    this.loadProducts(); // Refresh the data cache after adding a product
  }

  onProductEdited(product: any) {
    this.loadProducts(); // Refresh the data cache after editing a product
  }

  onStatusToggle(product: any) {
    if (product.status === 'DEACTIVE') {
      this.productService.deactivateProduct(product.id).subscribe(() => {
        // Optionally refresh the cache for the current page
        this.gridApi.refreshInfiniteCache();
      });
    } else if (product.status === 'ACTIVE') {
      this.productService.activateProduct(product.id).subscribe(() => {
        // Optionally refresh the cache for the current page
        this.gridApi.refreshInfiniteCache();
      });
    }
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
