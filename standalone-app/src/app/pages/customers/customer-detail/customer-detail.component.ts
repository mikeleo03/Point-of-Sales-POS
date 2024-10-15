import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { DateFormatPipe } from '../../../core/pipes/date-format.pipe';
import { PhoneNumberFormatPipe } from '../../../core/pipes/phone-number/phone-number-format.pipe';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [
    CommonModule,
    HlmLabelDirective,
    HlmInputDirective,
    DateFormatPipe,
    PhoneNumberFormatPipe
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
