import { Customer } from './customer.model';
import { InvoiceDetail } from './invoiceDetails.model';

export interface Invoice {
  id: string;
  amount: number;
  date: Date;
  customer: Customer;
  invoiceDetails: InvoiceDetail[];
}

