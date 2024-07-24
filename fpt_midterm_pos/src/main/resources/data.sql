-- Initialize table with DDL
-- Create `Customer` table
CREATE TABLE Customer (
    ID BINARY(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    status ENUM('Active', 'Deactivate') NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME
);

-- Create `Product` table
CREATE TABLE Product (
    ID BINARY(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status ENUM('Active', 'Deactivate') NOT NULL,
    quantity INT(10),
    createdAt DATETIME,
    updatedAt DATETIME
);


-- Create `Invoice` table
CREATE TABLE Invoice (
    ID BINARY(36) PRIMARY KEY,
    amount INT(10) NOT NULL,
    date DATE NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME,
    customerId BINARY(36),
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);

-- Create `InvoiceDetails` table
CREATE TABLE InvoiceDetails (
    invoiceID BINARY(36),
    productID BINARY(36),
    quantity INT(10),
    productPrice INT(10),
    productName VARCHAR(255),
    amount INT(10),
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES Invoice(ID),
    FOREIGN KEY (productID) REFERENCES Product(ID)
);