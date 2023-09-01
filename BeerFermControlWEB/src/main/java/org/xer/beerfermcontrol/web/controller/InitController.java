package org.xer.beerfermcontrol.web.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
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
    public String loadLogin(Model model) {
        if (!model.asMap().containsKey(WebConstants.USER)) {
            model.addAttribute(WebConstants.USER, new User());
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid @ModelAttribute(WebConstants.USER) User logUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        User user = beerFermControlFacade.getUser(logUser.getUsername(), logUser.getPassword());
        if (user == null) {
            bindingResult.rejectValue("username", "error.login");
            bindingResult.rejectValue("password", "error.vacio");
            return "login";
        } else {
            model.addAttribute(WebConstants.USER, user);
            return "redirect:/configList";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, SessionStatus status) {
        status.setComplete();
        return "redirect:/login";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model) {
        return "error";
    }

    @RequestMapping(value = "/newReading/{deviceName}", method = RequestMethod.POST)
    @ResponseBody
    public String reading(@PathVariable("deviceName") String deviceName, @RequestParam("temp") Double temperature,
            @RequestParam("sg") Double stGravity, HttpServletRequest request) throws Exception {
        String json = "{\"device\": \"" + deviceName + "\", \"temp\": " + temperature + ", \"sg\": " + stGravity + "}";
        beerFermControlFacade.newReading(deviceName, temperature, stGravity, json);
        return "ok";
    }

}
