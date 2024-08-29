import { Component } from '@angular/core';
import { BrnSheetContentDirective, BrnSheetTriggerDirective } from '@spartan-ng/ui-sheet-brain';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
import { ProductFormComponent } from '../product-form/product-form.component';
import { HlmSheetComponent, HlmSheetContentComponent, HlmSheetDescriptionDirective, HlmSheetFooterComponent, HlmSheetHeaderComponent, HlmSheetTitleDirective } from '@spartan-ng/ui-sheet-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  imports: [
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
    CommonModule, // Include CommonModule
  ],
  template: `
    <ng-container *ngIf="params?.data; else loadingTemplate">
      <hlm-sheet side="right">
          <button class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed"
            brnSheetTrigger 
            [disabled]="!params.data.status"
            (click)="onEditClick()">
            <i class="fas fa-pencil-alt"></i>&nbsp; Update
          </button>
          <hlm-sheet-content *brnSheetContent="let ctx">
              <hlm-sheet-header class="text-start md:mt-4 mt-6">
                  <h1 hlmSheetTitle class="md:text-2xl text-xl font-bold text-green-1">{{ params.data ? 'Edit Product' : 'Add New Product' }}</h1>
                  <p hlmSheetDescription>{{ params.data ? 'Edit the product details below.' : 'Fill out the form below to add a new product.' }}</p>
              </hlm-sheet-header>
              <app-product-form [product]="params.data" [isEditMode]="true" (productSaved)="onProductSaved($event)" (formClosed)="ctx.close()"></app-product-form>
          </hlm-sheet-content>
      </hlm-sheet>
    </ng-container>

    <ng-template #loadingTemplate>
      <p>Loading...</p>
    </ng-template>
  `
})
export class ActionCellRendererComponent implements ICellRendererAngularComp {
    params!: ICellRendererParams;

    agInit(params: ICellRendererParams): void {
      this.params = params;
    }

    refresh(params: ICellRendererParams) {
      this.params = params;
      return true;
    }

    onProductSaved(product: any) {
      this.params.context.componentParent.onAddProduct(product);
    }

    onEditClick() {
      this.params.context.componentParent.onEditProduct(this.params.data);
    }

    onDeleteClick() {
      this.params.context.componentParent.onDeleteProduct(this.params.data);
    }
}