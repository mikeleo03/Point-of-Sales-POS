import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HttpClientModule } from '@angular/common/http';
import { InvoiceService } from '../../../services/invoice.service'; // Adjust the path to your service
import { Revenue } from '../../../models/revenue.model'; // Adjust the path to your model

@Component({
  selector: 'app-invoice-revenue',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    HlmButtonDirective,
    HlmInputDirective,
    HlmIconComponent,
    HlmLabelDirective,
  ],
  templateUrl: './invoice-revenue.component.html',
  styleUrls: ['./invoice-revenue.component.css'],
})
export class InvoiceRevenueComponent implements OnInit {
  revenueData: Revenue | null = null;
  revenueForm: FormGroup;

  constructor(private fb: FormBuilder, private invoiceService: InvoiceService) {
    this.revenueForm = this.fb.group({
      date: ['', Validators.required],
      revenueBy: ['', Validators.required], // Adding required validation for revenueBy
    });
  }

  ngOnInit(): void {}

  getRevenueReport(): void {
    if (this.revenueForm.invalid) {
      return;
    }

    const { date, revenueBy } = this.revenueForm.value;
    const formattedDate = this.formatDate(date);

    this.invoiceService.getRevenue(formattedDate, revenueBy).subscribe(
      (data: Revenue) => {
        this.revenueData = data;
      },
      (error) => {
        console.error('Error fetching revenue data:', error);
      }
    );
  }

  private formatDate(date: string): string {
    const [year, month, day] = date.split('-');
    return `${year}/${month}/${day}`;
  }
}
