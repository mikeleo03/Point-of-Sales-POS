-- Initialize table with DDL
-- Create `Customer` table
CREATE TABLE customer (
    ID CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    status ENUM('Active', 'Deactivate') NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME
);

-- Create `Product` table
CREATE TABLE product (
    ID CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status ENUM('Active', 'Deactivate') NOT NULL,
    quantity INT(10),
    createdAt DATETIME,
    updatedAt DATETIME
);


-- Create `Invoice` table
CREATE TABLE invoice (
    ID CHAR(36) PRIMARY KEY,
    amount INT(10) NOT NULL,
    date DATE NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME,
    customerId CHAR(36),
    FOREIGN KEY (customerId) REFERENCES customer(ID)
);

-- Create `InvoiceDetails` table
CREATE TABLE invoice_details (
    invoiceID CHAR(36),
    productID CHAR(36),
    quantity INT(10),
    productPrice INT(10),
    productName VARCHAR(255),
    amount INT(10),
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES invoice(ID),
    FOREIGN KEY (productID) REFERENCES product(ID)
);