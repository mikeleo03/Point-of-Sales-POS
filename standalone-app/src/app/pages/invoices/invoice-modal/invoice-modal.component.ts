import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-invoice-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './invoice-modal.component.html',
  styleUrl: './invoice-modal.component.css',
})
export class InvoiceModalComponent implements OnInit {
  @Input() invoice: any;

  ngOnInit() {
    console.log(this.invoice);
  }
}
