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
import { InvoiceDetail } from '../../../models/invoiceDetails.model';

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
  @Input() isEditMode = false; // Flag to differentiate between add and edit mode
  @Output() invoiceSaved = new EventEmitter<any>();
  @Output() formClosed = new EventEmitter<void>();

  invoiceForm!: FormGroup;
  products: Product[] = [];

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private invoiceService: InvoiceService // Inject InvoiceService here
  ) {}

  ngOnInit() {
    this.invoiceForm = this.fb.group({
      customerId: ['', Validators.required],
      invoiceDetails: this.fb.array([this.createInvoiceDetail()]),
      status: [true],
      date: [new Date()],
    });

    if (this.isEditMode) {
      // Correct way to patch the form with existing invoice data
      this.invoiceForm.patchValue({
        customerId: this.invoice.customerId,
        status: this.invoice.status,
      });

      // Patch the FormArray separately
      this.invoiceForm.setControl(
        'invoiceDetails',
        this.fb.array(
          this.invoice.invoiceDetails.map((detail: any) =>
            this.fb.group({
              productId: [detail.productId, Validators.required],
              quantity: [
                detail.quantity,
                [Validators.required, Validators.min(1)],
              ],
            })
          )
        )
      );
    }

    this.loadProducts();
  }

  createInvoiceDetail(): FormGroup {
    return this.fb.group({
      productId: ['', Validators.required],
      quantity: [0, [Validators.required, Validators.min(1)]],
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
    const invoiceData = this.invoiceForm.value;
    if (this.isEditMode) {
      this.invoiceService.updateInvoice(invoiceData).subscribe(() => {
        this.invoiceSaved.emit(invoiceData);
        this.formClosed.emit(); // Close the form
      });
    } else {
      this.invoiceService.createInvoice(invoiceData).subscribe(() => {
        invoiceData.date;
        alert('Successfully Created Data');
        this.invoiceSaved.emit(invoiceData);
        this.formClosed.emit(); // Close the form
      });
    }
  }

  loadProducts() {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
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
