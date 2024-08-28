import { Component } from '@angular/core';
import { BrnSheetContentDirective, BrnSheetTriggerDirective } from '@spartan-ng/ui-sheet-brain';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
import { HlmSheetComponent, HlmSheetContentComponent, HlmSheetDescriptionDirective, HlmSheetFooterComponent, HlmSheetHeaderComponent, HlmSheetTitleDirective } from '@spartan-ng/ui-sheet-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';

@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  imports: [    
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
  template: `
    <button
      class="bg-gray-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-gray-600 mr-1.5 disabled:bg-gray-300 disabled:cursor-not-allowed">
      <i class="fas fa-pencil-alt"></i>&nbsp; Info</button>
      
    <hlm-sheet side="right">
        <button
          class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed">
          <i class="fas fa-pencil-alt"></i>&nbsp; Update
        </button>
    </hlm-sheet>

    <button 
      class="bg-red-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-red-600 disabled:bg-red-300 disabled:cursor-not-allowed">
      <i class="fas fa-trash"></i>&nbsp; Delete
    </button>
  `
})
export class ActionCellRendererComponent implements ICellRendererAngularComp {
    params!: ICellRendererParams;

    agInit(params: ICellRendererParams): void {
      this.params = params;
    }

    refresh(params: ICellRendererParams) {
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