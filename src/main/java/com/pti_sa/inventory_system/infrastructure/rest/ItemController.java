package com.pti_sa.inventory_system.infrastructure.rest;

import com.pti_sa.inventory_system.application.ItemService;
import com.pti_sa.inventory_system.application.dto.response.ItemResponseDTO;
import com.pti_sa.inventory_system.domain.model.Item;
import com.pti_sa.inventory_system.infrastructure.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items", description = "Operaciones relacionadas con los ítems")
@SecurityRequirement(name = "bearerAuth")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(
            summary = "Registrar nuevo ítem",
            description = "Crea un nuevo ítem. Requiere rol Admin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítem creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud Inválida"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> createItem(@RequestBody Item item) {
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del ítem es requerido.");
        }

        Integer createdBy = getAuthenticatedUserId();
        if (createdBy == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        try {
            ItemResponseDTO savedItem = itemService.saveItem(item);
            return ResponseEntity.ok(savedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener ítem por ID", description = "Devuelve los detalles de un ítem dado con su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítem encontrado"),
            @ApiResponse(responseCode = "404", description = "Ítem no encontrado")
    })

    @GetMapping("/detail/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Integer id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los ítems", description = "Devuelve la lista de todos los ítems registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @Operation(summary = "Actualizar ítem", description = "Actualiza un ítem por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ítem actualizado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Ítem no encontrado")
    })

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Integer id, @RequestBody Item item) {
        Integer updatedBy = getAuthenticatedUserId();
        if (updatedBy == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        System.out.println("Usuario autenticado para updateBy: " + updatedBy);

        return itemService.updateItem(id, item)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar ítem", description = "Elimina un ítem por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ítem eliminado exitosamente")
    })
    // ✅ Eliminar un ítem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Método privado para obtener el ID del usuario autenticado
    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        return (principal instanceof CustomUserDetails) ? ((CustomUserDetails) principal).getId() : null;
    }
}
