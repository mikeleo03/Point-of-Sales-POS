import { Component } from '@angular/core';
import { BrnSheetContentDirective, BrnSheetTriggerDirective } from '@spartan-ng/ui-sheet-brain';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
import { HlmSheetComponent, HlmSheetContentComponent, HlmSheetDescriptionDirective, HlmSheetFooterComponent, HlmSheetHeaderComponent, HlmSheetTitleDirective } from '@spartan-ng/ui-sheet-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { CustomerFormComponent } from "../customer-form/customer-form.component";

@Component({
  selector: 'app-action-cell-customer-renderer',
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
    
    CustomerFormComponent
],
  template: `
    <hlm-sheet side="right">
        <button
          class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed"
          brnSheetTrigger
          [disabled]="params.data.status === 'Deactive'"
          (click)="onEditClick()">
          <i class="fas fa-pencil-alt"></i>&nbsp; Update
        </button>
        <hlm-sheet-content *brnSheetContent="let ctx">
            <hlm-sheet-header class="text-start md:mt-4 mt-6">
                <h1 hlmSheetTitle class="md:text-2xl text-xl font-bold text-green-1">{{ params.data ? 'Edit Customer' : 'Add New Customer' }}</h1>
                <p hlmSheetDescription>{{ params.data ? 'Edit the Customer details below.' : 'Fill out the form below to add a new Customer.' }}</p>
            </hlm-sheet-header>
            <app-customer-form [customer]="params.data" [isEditMode]="true" (customerSaved)="onCustomerSaved($event)" (formClosed)="ctx.close()"/>
        </hlm-sheet-content>
    </hlm-sheet>
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

    onCustomerSaved(customer: any) {
      this.params.context.componentParent.onAddCustomer(customer);
    }

    onEditClick() {
      this.params.context.componentParent.onEditCustomer(this.params.data);
    }
}