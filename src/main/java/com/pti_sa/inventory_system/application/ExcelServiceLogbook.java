package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.LogbookResponseDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelServiceLogbook {
    private final LogbookService logbookService;
    private static final String FILE_PATH = "static/FO-GTI-02.xlsx";

    public ExcelServiceLogbook(LogbookService logbookService) {
        this.logbookService = logbookService;
    }

    public byte[] updateExcel(Long logbookId) throws IOException {
        System.out.println("Generando Excel para logbookId: " + logbookId);
        LogbookResponseDTO logbookResponseDTO = logbookService.findById(logbookId); // <-- Usa el ID recibido
        System.out.println("DTO generado: " + logbookResponseDTO);
        String createdAt = logbookResponseDTO.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String userEmail = logbookResponseDTO.getUserEmail();
        String location = logbookResponseDTO.getDeviceLocation();
        String code = logbookResponseDTO.getDeviceCode();
        String serial = logbookResponseDTO.getDeviceSerial();
        String brand = logbookResponseDTO.getDeviceBrand();
        String note = logbookResponseDTO.getNote();
        return updateExcelFile(createdAt, userEmail, location, serial, brand, code, note);
    }

    private byte[] updateExcelFile(String createdAt, String userEmail, String location, String serial, String brand,
            String code, String note) throws IOException {
        ClassPathResource resource = new ClassPathResource(FILE_PATH);
        File tempFile = File.createTempFile("temp_logbook_excel", "xlsx");
        Files.copy(resource.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try (FileInputStream fis = new FileInputStream(tempFile);
                Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            updateCell(sheet, 5, 9, createdAt);
            updateCell(sheet, 7, 3, userEmail);
            updateCell(sheet, 8, 3, location);
            updateCell(sheet, 13, 1, code);
            updateCell(sheet, 13, 3, serial);
            updateCell(sheet, 13, 5, brand);
            updateCell(sheet, 27, 3, note);

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }

        }

        return Files.readAllBytes(tempFile.toPath());
    }

    public LogbookResponseDTO getLogbookDTO(Long logbookId) {
        return logbookService.findById(logbookId);
    }

    private void updateCell(Sheet sheet, int rowIndex, int colIndex, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        cell.setCellValue(value);

    }
}
