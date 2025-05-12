package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.ExcelServiceLogbook;
import com.pti_sa.inventory_system.application.ExcelServiceMaintenance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/excel")
@Tag(name = "Exportación Excel", description = "Operaciones para generar y descargar archivos Excel")
@SecurityRequirement(name = "bearerAuth")
public class ExcelController {

    private final ExcelServiceMaintenance excelServiceMaintenance;
    private final ExcelServiceLogbook excelServiceLogbook;

    public ExcelController(ExcelServiceMaintenance excelServiceMaintenance, ExcelServiceLogbook excelServiceLogbook) {
        this.excelServiceMaintenance = excelServiceMaintenance;
        this.excelServiceLogbook = excelServiceLogbook;
    }

    @Operation(
            summary = "Exportar mantenimiento a Excel",
            description = "Genera y descarga el archivo Excel con el registro de mantenimento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al generar el archivo ")
    })

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

    @Operation(
            summary = "Exportar bitácora a Excel",
            description = "Genera y descarga el archivo Excel con la bitácora del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al generar el archivo")
    })

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
