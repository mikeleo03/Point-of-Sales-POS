import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../../services/customer/customer.service';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';

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
      id: [''],
      name: [this.customer?.name || '', Validators.required],
      phoneNumber: [this.customer?.phoneNumber || '', Validators.required],
      status: [this.customer?.status || 'Active'],
      createdAt: [this.customer?.createdAt || new Date()],
      updatedAt: [new Date()],
    });

    if(this.isEditMode) {
      this.customerForm.patchValue(this.customer);
    } else {
      this.customerService.getLastCustomerId().subscribe((lastId: number) => {
        this.lastCustomerId = lastId;
        this.customerForm.patchValue({
          id: (this.lastCustomerId + 1).toString()
        });
      });
    }
  }

  onSubmit() {
    const customerData = this.customerForm.value;
    const checkPhone = (customerData.phoneNumber).length;
    if(checkPhone > 16) {
      customerData.phoneNumber = (customerData.phoneNumber).slice(0, 16);
    }

    if(this.isEditMode) {
      customerData.updatedAt = new Date();
      this.customerService.updateCustomer(customerData).subscribe(() => {
        this.customerSaved.emit(customerData);
        this.formClosed.emit();
      });
    } else {
      this.customerService.addCustomer(customerData).subscribe(() => {
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
