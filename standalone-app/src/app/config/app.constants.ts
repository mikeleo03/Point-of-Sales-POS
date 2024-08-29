export const AppConstants = {
  APPLICATION_NAME: 'Javascript Application Framework',
  BASE_API_URL: '/my-api',
  LOG_OFF_ICON: 'sign-out',
};

export interface RouteLink {
  path: string;
  link: string;
}

export const RouterConfig = {
  HOME: { path: '', link: '/', title: 'Home' },
  LOGIN: {
    path: 'login',
    link: '/login',
    title: 'Login',
    data: { header: true },
  },
  PRODUCT: {
    path: 'products',
    link: '/product',
    title: 'List of Products',
    data: { header: true },
  },
  CUSTOMER: {
    path: 'customers',
    link: '/customer',
    title: 'List of Customers',
    data: { header: true }},
  INVOICE: {
    path: 'invoices',
    link: '/invoice',
    title: 'List of Invoices',
    data: { header: true },
  },
  NOT_FOUND: { path: '**', link: null, title: 'Page Not Found' },
};
