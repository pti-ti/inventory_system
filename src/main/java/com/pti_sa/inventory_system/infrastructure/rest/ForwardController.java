package com.pti_sa.inventory_system.infrastructure.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

    @RequestMapping(value = "/{[path:[^\\.]*}")
    public String forward(){
        //Redirige cualquier ruta no gestionada por Spring al index.html
        return "forward:/index.html";
    }
}
