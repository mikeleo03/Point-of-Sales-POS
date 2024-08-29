import { ChangeDetectorRef, Component } from '@angular/core';
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
        <hlm-switch class="mr-2 mt-1" [checked]="params.value === 'ACTIVE'" (change)="onStatusToggle()" />
        <span class="text-sm font-semibold mt-1" [ngClass]="{'text-green-500': params.value === 'ACTIVE', 'text-red-500': params.value === 'DEACTIVE'}">
            {{ params.value }}
        </span>
    </label>
  `
})
export class StatusCellRendererComponent {
  params!: ICellRendererParams;

  constructor(private cdr: ChangeDetectorRef) {}

  agInit(params: ICellRendererParams): void {
    this.params = params;
    this.cdr.markForCheck();
  }

  onStatusToggle() {
    const updatedCustomer = this.params.data;
    if(updatedCustomer.status === 'ACTIVE') {
      updatedCustomer.status = 'DEACTIVE';
    } else if (updatedCustomer.status === 'DEACTIVE') {
      updatedCustomer.status = 'ACTIVE';
    }

    this.params.context.componentParent.onStatusToggle(updatedCustomer);
    this.cdr.detectChanges();
  }
}