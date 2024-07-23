-- Initialize table with DDL
-- Create `Customer` table
CREATE TABLE Customer (
    ID CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    status ENUM('Active', 'Deactivate') NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME
);

-- Create `Product` table
CREATE TABLE Product (
    ID CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status ENUM('Active', 'Deactivate') NOT NULL,
    quantity INT(10),
    createdAt DATETIME,
    updatedAt DATETIME
);


-- Create `Invoice` table
CREATE TABLE Invoice (
    ID CHAR(36) PRIMARY KEY,
    amount INT(10) NOT NULL,
    date DATE NOT NULL,
    createdAt DATETIME,
    updatedAt DATETIME,
    customerId CHAR(36),
    FOREIGN KEY (customerId) REFERENCES Customer(ID)
);

-- Create `InvoiceDetails` table
CREATE TABLE InvoiceDetails (
    invoiceID CHAR(36),
    productID CHAR(36),
    quantity INT(10),
    productPrice INT(10),
    amount INT(10),
    createdAt DATETIME,
    updatedAt DATETIME,
    PRIMARY KEY (invoiceID, productID),
    FOREIGN KEY (invoiceID) REFERENCES Invoice(ID),
    FOREIGN KEY (productID) REFERENCES Product(ID)
);