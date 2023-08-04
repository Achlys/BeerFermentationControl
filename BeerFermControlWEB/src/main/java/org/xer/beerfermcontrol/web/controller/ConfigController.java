package org.xer.beerfermcontrol.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.web.util.WebConstants;

/**
 * 
 * 
 * @author Achlys
 */
@Controller
@SessionAttributes({WebConstants.USER})
@RequestMapping("/config")
public class ConfigController {
    
    private static final Logger LOGGER = LogManager.getLogger(ConfigController.class);
    
    @Autowired
    private BeerFermControlFacade beerFermControlFacade;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String loadAdd(Model model){
        model.addAttribute(WebConstants.CONFIG, new Config());
        return "configAddEdit";
    }

}
