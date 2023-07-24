package org.xer.beerfermcontrol.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.web.util.WebConstants;

/**
 * 
 * 
 * @author Achlys
 */
@Controller
public class InitController {
    
    private static final Logger LOGGER = LogManager.getLogger(InitController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLogin(Model model){
        if(!model.asMap().containsKey(WebConstants.USER)){
            model.addAttribute(WebConstants.USER, new User());
        }
        return "login";
    }
    
}
