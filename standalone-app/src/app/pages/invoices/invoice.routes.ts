import { Routes } from '@angular/router';
import { InvoiceListComponent } from './invoice-list/invoice-list.component';
import { InvoiceFormComponent } from './invoice-form/invoice-form.component';
import { InvoiceModalComponent } from './invoice-modal/invoice-modal.component';

export const invoiceRoutes: Routes = [
  { path: '', component: InvoiceListComponent },
  { path: 'dw', component: InvoiceModalComponent },
];
