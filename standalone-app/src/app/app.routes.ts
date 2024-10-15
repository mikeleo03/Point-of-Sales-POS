import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';
import { authGuard } from './main/guards/auth-guard.service';

export const routes: Routes = [
  {
    path: RouterConfig.LOGIN.path,
    loadChildren: () =>
      import('./pages/login/login.routes').then((m) => m.loginRoutes),
    title: RouterConfig.LOGIN.title,
    data: RouterConfig.LOGIN.data,
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: RouterConfig.HOME.path,
        loadChildren: () =>
          import('./pages/home/home.routes').then((m) => m.homeRoutes),
        title: "Home Page",
        data: { header: true },
      },
      {
        path: RouterConfig.PRODUCT.path,
        loadChildren: () =>
          import('./pages/products/product.routes').then(
            (m) => m.productRoutes
          ),
        title: RouterConfig.PRODUCT.title,
        data: RouterConfig.PRODUCT.data,
      },
      {
          path: RouterConfig.CUSTOMER.path,
          loadChildren: () => 
            import('./pages/customers/customer.routes').then(
              m => m.customerRoutes
            ),
          title: RouterConfig.CUSTOMER.title,
          data: RouterConfig.CUSTOMER.data,
      },
      {
        path: RouterConfig.INVOICE.path,
        loadChildren: () =>
          import('./pages/invoices/invoice.routes').then(
            (m) => m.invoiceRoutes
          ),
        title: RouterConfig.INVOICE.title,
        data: RouterConfig.INVOICE.data,
      },
    ],
  },
];
