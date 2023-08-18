package org.xer.beerfermcontrol.web.controller;

import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xer.beerfermcontrol.core.bean.Hydrom;
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
@RequestMapping("/config/{configId}/hydrom")
public class HydromController {

    private static final Logger LOGGER = LogManager.getLogger(HydromController.class);

    @Autowired
    private BeerFermControlFacade beerFermControlFacade;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String loadAdd(@PathVariable("configId") Integer configId, Model model) {
        Hydrom hydrom = new Hydrom();
        hydrom.setConfigId(configId);
        model.addAttribute(WebConstants.HYDROM, hydrom);
        return "hydromAddEdit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addHydrom(@PathVariable("configId") Integer configId, @Valid @ModelAttribute(WebConstants.HYDROM) Hydrom newHydrom, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "hydromAddEdit";
        }
        newHydrom.setConfigId(configId);
        beerFermControlFacade.addHydrom(newHydrom, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.hydrom.added");
        return "redirect:/config/" + configId;
    }
    
    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String remove(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        beerFermControlFacade.removeHydrom(id, configId, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.hydrom.removed");
        return "redirect:/config/" + configId;
    }
    
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String loadEdit(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, Model model) {
        model.addAttribute(WebConstants.HYDROM, beerFermControlFacade.getHydrom(id, configId, ((User) model.asMap().get(WebConstants.USER)).getId()));
        return "hydromAddEdit";
    }
    
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, @Valid @ModelAttribute(WebConstants.HYDROM) Hydrom hydrom, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "hydromAddEdit";
        }
        hydrom.setId(id);
        hydrom.setConfigId(configId);
        beerFermControlFacade.updateHydrom(hydrom, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.hydrom.updated");
        return "redirect:/config/" + configId;
    }

}
