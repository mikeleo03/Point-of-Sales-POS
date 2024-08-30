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
import {
  BrnDialogModule,
  BrnDialogContentDirective,
  BrnDialogTriggerDirective,
} from '@spartan-ng/ui-dialog-brain';
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective,
  HlmDialogFooterComponent,
  HlmDialogHeaderComponent,
  HlmDialogTitleDirective,
} from '@spartan-ng/ui-dialog-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { InvoiceFormComponent } from '../invoice-form/invoice-form.component';
import { InvoiceModalComponent } from '../invoice-modal/invoice-modal.component';

@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  imports: [
    InvoiceFormComponent,
    InvoiceModalComponent,
    BrnSheetTriggerDirective,
    BrnSheetContentDirective,
    BrnDialogTriggerDirective,
    BrnDialogModule,
    BrnDialogContentDirective,
    HlmSheetComponent,
    HlmSheetContentComponent,
    HlmSheetHeaderComponent,
    HlmSheetTitleDirective,
    HlmSheetDescriptionDirective,
    HlmDialogComponent,
    HlmDialogContentComponent,
    HlmDialogHeaderComponent,
    HlmDialogFooterComponent,
    HlmDialogTitleDirective,
    HlmDialogDescriptionDirective,
    HlmLabelDirective,
  ],
  template: `
    <!-- Dialog Content for Viewing Invoice -->
    <hlm-dialog>
      <button
        class="bg-gray-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-gray-600 mr-1.5 disabled:bg-gray-300 disabled:cursor-not-allowed"
        brnDialogTrigger
        hlmBtn
        [disabled]="!params.data"
      >
        <i class="fas fa-eye"></i>&nbsp; View
      </button>
      <hlm-dialog-content class="max-w-[300px] lg:max-w-[700px] text-left h-[90vh]" *brnDialogContent="let ctx">
        <hlm-dialog-header class="w-full text-left">
          <h3 hlmDialogTitle>Invoice Details</h3>
          <p hlmDialogDescription>Invoice Details</p>
        </hlm-dialog-header>
        <app-invoice-modal [invoice]="params.data"></app-invoice-modal>
        <hlm-dialog-footer>
          <button hlmButton hlmDialogClose>Close</button>
        </hlm-dialog-footer>
      </hlm-dialog-content>
    </hlm-dialog>

    <hlm-sheet side="right">
      <button
        class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed"
        brnSheetTrigger
        [disabled]="!params.data || isTimeAgoMoreThan10Minutes()"
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
                ? 'Edit the invoice details below. Can be edit only 10 Minutes'
                : 'Fill out the form below to add a new invoice.'
            }}
          </p>
        </hlm-sheet-header>
        <app-invoice-form
          [invoice]="params.data"
          [isEditMode]="true"
          (invoiceSaved)="onInvoiceEdited($event)"
          (formClosed)="ctx.close()"
        ></app-invoice-form>
      </hlm-sheet-content>
    </hlm-sheet>
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

  onInvoiceEdited(invoice: any) {
    this.params.context.componentParent.onInvoiceEdited(invoice);
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
