import { Component } from '@angular/core';
import { ICellRendererParams } from 'ag-grid-community';
import { HlmSwitchComponent } from '@spartan-ng/ui-switch-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-cell-renderer',
  standalone: true,
  imports: [HlmLabelDirective, HlmSwitchComponent, CommonModule],
  template: `
    <label class="flex items-center w-full justify-center mt-1">
        <hlm-switch class="mr-2 mt-1" [checked]="params.value === 'Active'" (change)="onStatusToggle()" />
        <span class="text-sm font-semibold mt-1" [ngClass]="{'text-green-500': params.value === 'Active', 'text-red-500': params.value === 'Deactive'}">
            {{ params.value }}
        </span>
    </label>
  `
})
export class StatusCellRendererComponent {
  params!: ICellRendererParams;

  agInit(params: ICellRendererParams): void {
    this.params = params;
  }

  onStatusToggle() {
    const updatedCustomer = this.params.data;
    if(updatedCustomer.status === 'Active') {
      updatedCustomer.status = 'Deactive';
    } else {
      updatedCustomer.status = 'Active';
    }
    
    this.params.context.componentParent.onStatusToggle(updatedCustomer);
  }
}