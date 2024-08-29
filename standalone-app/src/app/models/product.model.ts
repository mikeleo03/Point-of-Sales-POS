export interface Product {
    id: string;
    name: string;
    price: number;
    status: boolean;
    quantity: number;
    createdAt: string;
    updatedAt: string;
}  

export interface ProductDTO {
    id: string;
    name: string;
    price: number;
    status: boolean;
    quantity: number;
}  

export interface ProductDTO {
    id: string;
    name: string;
    price: number;
    status: boolean;
    quantity: number;
} 

export interface ProductSaveDTO {
    name: string;
    price: number;
    quantity: number;
} 

export interface ProductSearchCriteriaDTO {
    name?: string;
    sortByName?: string;
    sortByPrice?: string;
    minPrice?: number;
    maxPrice?: number;
} 

export interface ProductShowDTO {
    id: string;
    name: string;
    price: number;
    quantity: number;
}  