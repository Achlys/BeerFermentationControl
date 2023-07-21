package org.xer.beerfermcontrol.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 
 * @author Achlys
 */
@Controller
public class InitController {
    
    private static final Logger LOGGER = LogManager.getLogger(InitController.class);

    @RequestMapping("/prueba")
    public String prueba(){
        return "prueba";
    }
    
}
