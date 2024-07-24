package com.example.fpt_midterm_pos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.fpt_midterm_pos.data.model.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;

public class FileUtils {
    public static String TYPE = "text/csv";
    static String[] HEADERS = {"name", "price", "quantity" };
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            logger.error("Invalid file type: " + file.getContentType());
            return false;
        }
        return true;
    }

    public static List<Product> readProductsFromCSV(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                Product product = fromCSV(attributes);
                products.add(product);
            }
        } catch (IOException e) {
            throw new IOException("Error reading CSV file: " + e.getMessage(), e);
        }
        return products;
    }

    public static Product fromCSV(String[] attributes) {
        // Ensure the length of attributes array matches the CSV header length
        if (attributes.length < HEADERS.length) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        Product product = new Product();
        product.setName(attributes[0]);
        product.setPrice((double) Integer.parseInt(attributes[1]));
        product.setQuantity(Integer.parseInt(attributes[2]));
        product.setStatus(Status.Active);
        product.setCreatedAt(LocalDate.now());
        product.setUpdatedAt(LocalDate.now());
        return product;
    }
}
