import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
import {
  BrnSheetContentDirective,
  BrnSheetTriggerDirective,
} from '@spartan-ng/ui-sheet-brain';
import {
  HlmSheetComponent,
  HlmSheetContentComponent,
  HlmSheetHeaderComponent,
  HlmSheetTitleDirective,
  HlmSheetDescriptionDirective,
} from '@spartan-ng/ui-sheet-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { InvoiceFormComponent } from '../invoice-form/invoice-form.component';

@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  imports: [
    InvoiceFormComponent,
    BrnSheetTriggerDirective,
    BrnSheetContentDirective,
    HlmSheetComponent,
    HlmSheetContentComponent,
    HlmSheetHeaderComponent,
    HlmSheetTitleDirective,
    HlmSheetDescriptionDirective,
    HlmLabelDirective,
  ],
  template: `
    <hlm-sheet side="right">
      <button
        class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed"
        brnSheetTrigger
        [disabled]="
          !params.data || !params.data.status || isTimeAgoMoreThan10Minutes()
        "
      >
        <i class="fas fa-pencil-alt"></i>&nbsp; Update
      </button>
      <hlm-sheet-content *brnSheetContent="let ctx">
        <hlm-sheet-header class="text-start md:mt-4 mt-6">
          <h1 hlmSheetTitle class="md:text-2xl text-xl font-bold text-green-1">
            {{ params.data ? 'Edit Invoice' : 'Add New Invoice' }}
          </h1>
          <p hlmSheetDescription>
            {{
              params.data
                ? 'Edit the invoice details below.'
                : 'Fill out the form below to add a new invoice.'
            }}
          </p>
        </hlm-sheet-header>
        <app-invoice-form
          [invoice]="params.data"
          [isEditMode]="true"
          (invoiceSaved)="onInvoiceSaved($event)"
          (formClosed)="ctx.close()"
        ></app-invoice-form>
      </hlm-sheet-content>
    </hlm-sheet>

    <button
      class="bg-red-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-red-600 disabled:bg-red-300 disabled:cursor-not-allowed"
      (click)="onDeleteClick()"
      [disabled]="!params.data || !params.data.status"
    >
      <i class="fas fa-trash"></i>&nbsp; Delete
    </button>
  `,
})
export class ActionCellRendererComponent implements ICellRendererAngularComp {
  params!: ICellRendererParams;

  agInit(params: ICellRendererParams): void {
    this.params = params;
  }

  refresh(params: ICellRendererParams): boolean {
    this.params = params;
    return true;
  }

  onInvoiceSaved(invoice: any) {
    this.params.context.componentParent.onInvoiceSaved(invoice);
  }

  onDeleteClick() {
    this.params.context.componentParent.onDeleteInvoice(this.params.data);
  }

  isTimeAgoMoreThan10Minutes(): boolean {
    if (!this.params.data || !this.params.data.date) {
      return false;
    }

    const invoiceDate = new Date(this.params.data.date);
    const now = new Date();
    const diffInMinutes = Math.floor(
      (now.getTime() - invoiceDate.getTime()) / 60000
    );
    return diffInMinutes > 10;
  }
}
