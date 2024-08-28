import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';

export const routes: Routes = [
  {
    path: RouterConfig.LOGIN.path,
    loadChildren: () =>
      import('./pages/login/login.routes').then((m) => m.loginRoutes),
  },
  {
    path: RouterConfig.PRODUCT.path,
    loadChildren: () =>
      import('./pages/products/product.routes').then((m) => m.productRoutes),
  },
  {
    path: RouterConfig.INVOICE.path,
    loadChildren: () =>
      import('./pages/invoices/invoice.routes').then((m) => m.invoiceRoutes),
  },
];
