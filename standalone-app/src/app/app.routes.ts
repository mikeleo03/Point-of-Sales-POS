import { Routes } from '@angular/router';
import { RouterConfig } from './config/app.constants';
import { authGuard } from './main/guards/auth-guard.service';

export const routes: Routes = [
    {
        path: RouterConfig.LOGIN.path,
        loadChildren: () => import('./pages/login/login.routes').then(m => m.loginRoutes),
        title: RouterConfig.LOGIN.title,
        data: RouterConfig.LOGIN.data
    },
    {
        path: '',
        canActivate: [authGuard],
        children: [
            {
                path: RouterConfig.PRODUCT.path,
                loadChildren: () => import('./pages/products/product.routes').then(m => m.productRoutes),
                title: RouterConfig.PRODUCT.title,
                data: RouterConfig.PRODUCT.data,
            },
            /* {
                path: RouterConfig.CUSTOMER.path,
                loadChildren: () => import('./pages/customer/customer.routes').then(m => m.customerRoutes),
                title: RouterConfig.CUSTOMER.title,
                data: RouterConfig.CUSTOMER.data,
            },
            {
                path: RouterConfig.INVOICE.path,
                loadChildren: () => import('./pages/invoice/invoice.routes').then(m => m.invoiceRoutes),
                title: RouterConfig.INVOICE.title,
                data: RouterConfig.INVOICE.data,
            } */
        ]
    }
];