import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [
    CommonModule,
    HlmLabelDirective,
    HlmInputDirective
  ],
  templateUrl: './customer-detail.component.html',
  styleUrl: './customer-detail.component.css'
})
export class CustomerDetailComponent {
  @Input() customer: any;

  fields: string[] = ['id', 'name', 'phoneNumber', 'status', 'createdAt', 'updatedAt'];

  getFieldLabel(field: string): string {
    return field.charAt(0).toUpperCase() + field.slice(1).replace(/([A-Z])/g, ' $1').trim();
  }
}
