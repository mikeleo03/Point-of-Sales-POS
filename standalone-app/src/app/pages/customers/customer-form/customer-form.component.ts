import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../../services/customer/customer.service';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { CustomerSaveDTO } from '../../../models/customer.model';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HlmButtonDirective,
    HlmInputDirective,
    HlmIconComponent,
    HlmLabelDirective,
  ],
  templateUrl: './customer-form.component.html',
  styleUrl: './customer-form.component.css'
})
export class CustomerFormComponent implements OnInit {
  @Input() customer: any;
  @Input() isEditMode = false;
  @Output() customerSaved = new EventEmitter<any>();
  @Output() formClosed = new EventEmitter<void>();

  customerForm!: FormGroup;
  lastCustomerId!: number;

  constructor(private fb: FormBuilder, private customerService: CustomerService) {}

  ngOnInit() {
    this.customerForm = this.fb.group({
      id: [this.customer?.id],
      name: [this.customer?.name || '', Validators.required],
      phoneNumber: [this.customer?.phoneNumber || '', Validators.required],
      status: [this.customer?.status || 'Active'],
      createdAt: [this.customer?.createdAt || new Date()],
      updatedAt: [new Date()],
    });

    if(this.isEditMode) {
      this.customerForm.patchValue(this.customer);
    }
  }

  onSubmit() {
    const customerData = this.customerForm.value;
    const checkPhone = (customerData.phoneNumber).length;
    if(checkPhone > 16) {
      customerData.phoneNumber = (customerData.phoneNumber).slice(0, 16);
    }

    const saveData: CustomerSaveDTO = {
      name: customerData.name,
      phoneNumber: customerData.phoneNumber
    };

    if(this.isEditMode) {
      this.customerService.updateCustomer(customerData.id, saveData).subscribe(() => {
        this.customerSaved.emit(customerData);
        this.formClosed.emit();
      });
    } else {
      this.customerService.addCustomer(saveData).subscribe(() => {
        this.customerSaved.emit(customerData);
        this.formClosed.emit();
      })
    }
  }

  get name() {
    return this.customerForm.get('name');
  }

  get phoneNumber() {
    return this.customerForm.get('phoneNumber');
  }
}
