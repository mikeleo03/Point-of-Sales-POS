export interface InvoiceDTO {
  id: string;
  amount: number;
  date: Date;
  customer: CustomerInvoiceDTO;
  invoiceDetails: InvoiceDetailDTO[];
  status: boolean;
}

export interface CustomerInvoiceDTO {
  id: string;
  name: string;
}

export interface InvoiceDetailDTO {
  productId: string;
  productName: string;
  quantity: number;
  price: number;
  amount: number;
}

export interface InvoiceSaveDTO {
  customerId: CustomerInvoiceDTO;
  invoiceDetails: InvoiceDetailDTO[];
}

export interface InvoiceSearchCriteriaDTO {
  customerName?: string;
  customerId?: string;

  startDate?: Date;
  endDate?: Date;

  month?: number;
  sortByDate?: string;
  sortByAmount?: string;
}
