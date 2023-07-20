package org.xer.beerfermcontrol.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 
 * @author Achlys
 */
@Controller
public class InitController {

    @RequestMapping("/prueba")
    public String prueba(){
        return "prueba";
    }
    
}
