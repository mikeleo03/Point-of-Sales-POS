import {
  Component,
  EventEmitter,
  Output,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  ReactiveFormsModule,
  FormArray,
} from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { CustomerService } from '../../../services/customer/customer.service';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { InvoiceService } from '../../../services/invoice.service';
import { Product } from '../../../models/product.model';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import { Customer } from '../../../models/customer.model';

@Component({
  selector: 'app-invoice-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    HlmButtonDirective,
    HlmInputDirective,
    HlmIconComponent,
    HlmLabelDirective,
  ],
  templateUrl: './invoice-form.component.html',
  styleUrls: ['./invoice-form.component.css'],
})
export class InvoiceFormComponent implements OnInit {
  @Input() invoice: any;
  @Input() isEditMode = false;
  @Input() isViewMode = false; // New flag for View Mode
  @Output() invoiceSaved = new EventEmitter<any>();
  @Output() formClosed = new EventEmitter<void>();

  invoiceForm!: FormGroup;
  products: Product[] = [];
  customers: Customer[] = [];

  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer!: ToastContainerDirective;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private customerService: CustomerService,
    private invoiceService: InvoiceService,
    private toastrService: ToastrService
  ) {}

  ngOnInit() {
    this.invoiceForm = this.fb.group({
      customerId: [
        { value: '', disabled: this.isViewMode },
        Validators.required,
      ],
      invoiceDetails: this.fb.array([this.createInvoiceDetail()]),
    });
    if (this.isEditMode) {
      this.patchFormWithData();
    }
    this.loadProducts();
    this.loadCustomers();
  }

  patchFormWithData() {
    this.invoiceForm.patchValue({
      customerId: this.invoice.customer.id,
    });

    this.invoiceForm.setControl(
      'invoiceDetails',
      this.fb.array(
        this.invoice.invoiceDetails.map((detail: any) =>
          this.fb.group({
            productId: [
              { value: detail.productId, disabled: this.isViewMode },
              Validators.required,
            ],
            quantity: [
              { value: detail.quantity, disabled: this.isViewMode },
              [Validators.required, Validators.min(1)],
            ],
          })
        )
      )
    );
  }

  createInvoiceDetail(): FormGroup {
    return this.fb.group({
      productId: [
        { value: '', disabled: this.isViewMode },
        Validators.required,
      ],
      quantity: [
        { value: 0, disabled: this.isViewMode },
        [Validators.required, Validators.min(1)],
      ],
    });
  }

  addInvoiceDetail() {
    this.invoiceDetails.push(this.createInvoiceDetail());
  }

  removeInvoiceDetail(index: number) {
    this.invoiceDetails.removeAt(index);
  }

  onSubmit() {
    if (this.isViewMode) {
      return; // Do nothing if in view mode
    }

    const invoiceData = this.invoiceForm.value;
    if (this.isEditMode) {
      this.invoiceService
        .updateInvoice(this.invoice.id, invoiceData)
        .subscribe({
          next: (response) => {
            if (response) {
              this.toastrService.success('Update exist data successful!');
              this.invoiceSaved.emit(invoiceData);
              this.formClosed.emit();
            }
          },
          error: (error) => {
            if (error.status === 400) {
              this.toastrService.error(error.error.error);
            }
          },
        });
    } else {
      this.invoiceService.createInvoice(invoiceData).subscribe({
        next: (response) => {
          if (response) {
            this.toastrService.success('Added new data successful!');
            this.invoiceSaved.emit(invoiceData);
            this.formClosed.emit();
          }
        },
        error: (error) => {
          if (error.status === 400) {
            this.toastrService.error(error.error.error);
          }
        },
      });
    }
  }

  loadProducts() {
    this.productService.getProducts({}, 0, 20).subscribe((products) => {
      this.products = products.content;
    });
  }

  loadCustomers() {
    this.customerService.getCustomers(0, 20).subscribe((customers) => {
      this.customers = customers.content;
    });
  }

  get invoiceDetails(): FormArray {
    return this.invoiceForm.get('invoiceDetails') as FormArray;
  }

  get customerId() {
    return this.invoiceForm.get('customerId');
  }

  get productId() {
    return this.invoiceForm.get('productId');
  }

  get quantity() {
    return this.invoiceForm.get('quantity');
  }
}
