package com.pti_sa.inventory_system.infrastructure.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

    @RequestMapping(value = {
            "/dashboard",
            "/usuarios",
            "/dispositivos",
            "/marcas",
            "/modelos",
            "/ubicaciones",
            "/estados",
            "/bitacoras",
            "/mantenimientos",
            "/admin",
            "/registrar-usuario",
            "/registrar-dispositivo",
            "/registrar-bitacora",
            "/registrar-mantenimiento"
    })
    public String redirect() {
        return "forward:/index.html";
    }
}
