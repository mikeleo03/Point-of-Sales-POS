import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  ReactiveFormsModule,
  FormArray,
} from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { InvoiceService } from '../../../services/invoice.service';
import { Product } from '../../../models/product.model';

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

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit() {
    this.invoiceForm = this.fb.group({
      customerId: [
        { value: '', disabled: this.isViewMode },
        Validators.required,
      ],
      invoiceDetails: this.fb.array([this.createInvoiceDetail()]),
    });

    if (!this.isViewMode && this.isEditMode) {
      this.patchFormWithData();
    } else if (this.isViewMode) {
      this.patchFormWithData();
      this.invoiceForm.disable(); // Disable all controls for view mode
    }

    this.loadProducts();
  }

  patchFormWithData() {
    this.invoiceForm.patchValue({
      customerId: this.invoice.customerId,
      status: this.invoice.status,
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

  get invoiceDetails(): FormArray {
    return this.invoiceForm.get('invoiceDetails') as FormArray;
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
      this.invoiceService.updateInvoice(invoiceData).subscribe(() => {
        this.invoiceSaved.emit(invoiceData);
        this.formClosed.emit();
      });
    } else {
      this.invoiceService.createInvoice(invoiceData).subscribe(() => {
        alert('Successfully Created Data');
        this.invoiceSaved.emit(invoiceData);
        this.formClosed.emit();
      });
    }
  }

  loadProducts() {
    this.productService.getProducts({}, 0, 20).subscribe((products) => {
      this.products = products.content;
      console.log(products);
    });
  }

  get customerId() {
    return this.invoiceForm.get('customerId');
  }

  get status() {
    return this.invoiceForm.get('status');
  }

  get productId() {
    return this.invoiceForm.get('productId');
  }

  get quantity() {
    return this.invoiceForm.get('quantity');
  }
}
