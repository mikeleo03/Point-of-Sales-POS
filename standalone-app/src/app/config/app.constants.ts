export const AppConstants = {
    APPLICATION_NAME: 'Javascript Application Framework',
    BASE_API_URL: '/my-api',
    LOG_OFF_ICON: 'sign-out'
};
  
export interface RouteLink {
    path: string;
    link: string;
}
  
export const RouterConfig = {
    HOME: {path: '', link: '/'},
    LOGIN: {path: 'login', link: '/login', title: 'Login', data: {header: true}},
    PRODUCT: {path: 'products', link: '/product', title: 'List of Products', data: {header: true}},
    NOT_FOUND: {path: '**', link: null, title: 'Page Not Found'}
};