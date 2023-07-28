package org.xer.beerfermcontrol.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.web.util.WebConstants;

/**
 * 
 * 
 * @author Achlys
 */
@Controller
@SessionAttributes({WebConstants.USER})
public class ConfigListController {
    
    private static final Logger LOGGER = LogManager.getLogger(ConfigListController.class);
    
    @Autowired
    private BeerFermControlFacade beerFermControlFacade;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loadLogin(Model model){
        model.addAttribute(WebConstants.CONFIG_LIST, beerFermControlFacade.getUsersConfigs(((User)model.asMap().get(WebConstants.USER)).getId()));
        return "configList";
    }

}
