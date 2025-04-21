package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.ExcelServiceLogbook;
import com.pti_sa.inventory_system.application.ExcelServiceMaintenance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/excel")
public class ExcelController {

    private final ExcelServiceMaintenance excelServiceMaintenance;
    private final ExcelServiceLogbook excelServiceLogbook;

    public ExcelController(ExcelServiceMaintenance excelServiceMaintenance, ExcelServiceLogbook excelServiceLogbook) {
        this.excelServiceMaintenance = excelServiceMaintenance;
        this.excelServiceLogbook = excelServiceLogbook;
    }

    @GetMapping("/update")
    public ResponseEntity<byte[]> updateMaintenanceExcel() {
        try {
            byte[] fileBytes = excelServiceMaintenance.updateExcel();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=FO-GTI-01.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/logbook")
    public ResponseEntity<byte[]> updateLogbookExcel(){
        try {
            byte[] fileBytes = excelServiceLogbook.updateExcel();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=FO-GTI-02.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }


}
