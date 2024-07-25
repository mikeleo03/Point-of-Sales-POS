package com.example.fpt_midterm_pos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

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

    public static List<ProductSaveDTO> readProductsFromCSV(MultipartFile file) throws IOException {
        List<ProductSaveDTO> productSaveDTOs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                ProductSaveDTO productSaveDTO = fromCSV(attributes);
                productSaveDTOs.add(productSaveDTO);
            }
        } catch (IOException e) {
            throw new IOException("Error reading CSV file: " + e.getMessage(), e);
        }
        return productSaveDTOs;
    }

    public static ProductSaveDTO fromCSV(String[] attributes) {
        // Ensure the length of attributes array matches the CSV header length
        if (attributes.length < HEADERS.length) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        ProductSaveDTO productSaveDTO = new ProductSaveDTO();
        productSaveDTO.setName(attributes[0]);
        productSaveDTO.setPrice(Double.parseDouble(attributes[1]));
        productSaveDTO.setQuantity(Integer.parseInt(attributes[2]));
        return productSaveDTO;
    }
}
