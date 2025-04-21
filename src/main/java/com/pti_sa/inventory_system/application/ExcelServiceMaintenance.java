package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.ItemResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.MaintenanceResponseDTO;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelServiceMaintenance {
    private final MaintenanceService maintenanceService;
    private static final String FILE_PATH = "static/FO-GTI-01.xlsx"; // Ruta dentro de resources

    public ExcelServiceMaintenance(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    public byte[] updateExcel() throws IOException {
        // Obtener la información del mantenimiento
        MaintenanceResponseDTO maintenanceDTO = maintenanceService.findLatestMaintenance();

        String maintenanceDate = maintenanceDTO.getMaintenanceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String maintenanceType = maintenanceDTO.getMaintenanceType(); // "Preventivo", "Correctivo" o "Garantía"
        String deviceCode = maintenanceDTO.getDeviceCode(); // Código del equipo en mantenimiento
        String userEmail = maintenanceDTO.getUserEmail(); // Email del usuario
        String userLocation = maintenanceDTO.getUserLocation(); // Localización del usuario
        String comment = maintenanceDTO.getComment(); // Comentarios
        String createdByEmail = maintenanceDTO.getCreatedByEmail(); // Correo del creador del mantenimiento
        List<ItemResponseDTO> items = maintenanceDTO.getItems(); // Lista de ítems seleccionados

        System.out.println("📌 Email antes de actualizar Excel: " + userEmail);

        return updateExcelFile(maintenanceDate, maintenanceType, deviceCode, userEmail, userLocation, comment, items, createdByEmail);
    }

    private byte[] updateExcelFile(String maintenanceDate, String maintenanceType, String deviceCode, String userEmail,
                                   String userLocation, String comment, List<ItemResponseDTO> items, String createdByEmail) throws IOException {

        // Cargar el archivo desde la carpeta `resources/static/`
        ClassPathResource resource = new ClassPathResource(FILE_PATH);
        File tempFile = File.createTempFile("temp_excel", ".xlsx");
        Files.copy(resource.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try (FileInputStream fis = new FileInputStream(tempFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            // 🔹 Actualizar la celda J6 con la fecha de mantenimiento
            updateCell(sheet, 5, 9, maintenanceDate); // J6 = fila 5, columna 9 (base 0)

            // 🔹 Agregar el `deviceCode` en la celda D9
            updateCell(sheet, 8, 3, deviceCode); // D9 = fila 8, columna 3 (base 0)

            // 🔹 Agregar el `userEmail` en la celda D10
            updateCell(sheet, 9, 3, userEmail); // D10 = fila 9, columna 3 (base 0)
            updateCell(sheet, 33, 9, userEmail); // D10 = fila 9, columna 3 (base 0)


            // 🔹 Agregar el email del creador en la celda D34
            updateCell(sheet, 33, 2, createdByEmail);
            updateCell(sheet, 33, 3, createdByEmail);

            // Agregar la ubicación
            updateMergedCell(sheet, 7, 3, userLocation); // D8 = fila 7, columna 3 (base 0)

            // 🔹 Agregar los comentarios en la celda D29
            updateCell(sheet, 28, 3, comment); // D29 = fila 28, columna 3 (base 0)

            // 🔹 Marcar con "X" el tipo de mantenimiento
            if ("Preventivo".equalsIgnoreCase(maintenanceType)) {
                updateCell(sheet, 11, 4, "X"); // E12 = fila 11, columna 4 (base 0)
            } else if ("Correctivo".equalsIgnoreCase(maintenanceType)) {
                updateCell(sheet, 11, 6, "X"); // G12 = fila 11, columna 6 (base 0)
            } else if ("Garantía".equalsIgnoreCase(maintenanceType)) {
                updateCell(sheet, 11, 8, "X"); // I12 = fila 11, columna 8 (base 0)
            }

            // 🔹 Agregar los ítems en la columna B (de B16 a B27)
            addItemsToColumn(sheet, items, 15, 1); // B16 = fila 15, columna 1 (base 0)

            // Guardar los cambios en el archivo temporal
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }
        }

        // Leer el archivo modificado y devolverlo como byte[]
        return Files.readAllBytes(tempFile.toPath());
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

    private void addItemsToColumn(Sheet sheet, List<ItemResponseDTO> items, int startRow, int colIndex) {
        for (int i = 0; i < Math.min(items.size(), 12); i++) { // Máximo 12 ítems (B16 - B27)
            updateCell(sheet, startRow + i, colIndex, items.get(i).getName());
        }
    }

    private void updateMergedCell(Sheet sheet, int rowIndex, int colIndex, String value) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);


            if (mergedRegion.isInRange(rowIndex, colIndex)) {
                int firstRow = mergedRegion.getFirstRow();
                int firstCol = mergedRegion.getFirstColumn();


                updateCell(sheet, firstRow, firstCol, value);
                return; //
            }
        }
    }
}
