import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PriceFormatPipe } from '../../../core/pipes/price-format.pipe';
import { DateFormatPipe } from '../../../core/pipes/date-format.pipe';
@Component({
  selector: 'app-invoice-modal',
  standalone: true,
  imports: [CommonModule, PriceFormatPipe, DateFormatPipe],
  templateUrl: './invoice-modal.component.html',
  styleUrl: './invoice-modal.component.css',
})
export class InvoiceModalComponent implements OnInit {
  @Input() invoice: any;

  ngOnInit() {
    console.log(this.invoice);
  }
}
