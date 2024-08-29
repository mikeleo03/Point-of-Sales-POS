export interface Customer {
    id: string;
    name: string;
    phoneNumber: string;
    status: string;
    createdAt: Date;
    updatedAt: Date;
}

export interface CustomerDTO {
    id: string;
    name: string;
    phoneNumber: string;
    status: string;
}

export interface CustomerSaveDTO {
    name: string;
    phoneNumber: string;
}

export interface CustomerShowDTO {
    id: string;
    name: string;
    phoneNumber: string;
}