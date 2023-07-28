package org.xer.beerfermcontrol.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
public class InitController {
    
    private static final Logger LOGGER = LogManager.getLogger(InitController.class);
    
    @Autowired
    private BeerFermControlFacade beerFermControlFacade;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLogin(Model model){
        if(!model.asMap().containsKey(WebConstants.USER)){
            model.addAttribute(WebConstants.USER, new User());
        }
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute(WebConstants.USER) @Validated User logUser, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "login";
        }
        User user = beerFermControlFacade.getUser(logUser.getUsername(), logUser.getPassword());
        if (user == null) {
            bindingResult.rejectValue("username", "error.login");
            return "login";
        }else{
            return "redirect:/";
        }
    }

}
