package org.xer.beerfermcontrol.web.controller;

import java.util.Locale;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xer.beerfermcontrol.core.bean.Range;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.web.util.DecimalPropertyEditor;
import org.xer.beerfermcontrol.web.util.WebConstants;

/**
 *
 *
 * @author Achlys
 */
@Controller
@SessionAttributes({WebConstants.USER})
@RequestMapping("/config/{configId}/range")
public class RangeController {

    private static final Logger LOGGER = LogManager.getLogger(RangeController.class);

    @Autowired
    private BeerFermControlFacade beerFermControlFacade;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder, Locale locale) {
        dataBinder.registerCustomEditor(Double.class, new DecimalPropertyEditor(locale));
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String loadAdd(@PathVariable("configId") Integer configId, Model model) {
        Range range = new Range();
        range.setConfigId(configId);
        model.addAttribute(WebConstants.RANGE, range);
        return "rangeAddEdit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addRange(@PathVariable("configId") Integer configId, @Valid @ModelAttribute(WebConstants.RANGE) Range range,
            BindingResult bindingResult, Model model, RedirectAttributes ra) {
        range.setConfigId(configId);
        if (bindingResult.hasErrors()) {
            return "rangeAddEdit";
        }
        if (range.getBottomGravity() > range.getTopGravity()) {
            bindingResult.rejectValue("topGravity", "error.top.gravitty.le.bottom.gravity");
            return "rangeAddEdit";
        }
        if (beerFermControlFacade.overlapsOtherRange(range, false)) {
            bindingResult.rejectValue("topGravity", "error.overlaps.other.range");
            bindingResult.rejectValue("bottomGravity", "error.overlaps.other.range");
            return "rangeAddEdit";
        }
        beerFermControlFacade.addRange(range, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.range.added");
        return "redirect:/config/" + configId;
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String remove(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        beerFermControlFacade.removeRange(id, configId, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.range.removed");
        return "redirect:/config/" + configId;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String loadEdit(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, Model model) {
        model.addAttribute(WebConstants.RANGE, beerFermControlFacade.getRange(id, configId, ((User) model.asMap().get(WebConstants.USER)).getId()));
        return "rangeAddEdit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, @Valid @ModelAttribute(WebConstants.RANGE) Range range,
            BindingResult bindingResult, Model model, RedirectAttributes ra) {
        range.setId(id);
        range.setConfigId(configId);
        if (bindingResult.hasErrors()) {
            return "rangeAddEdit";
        }
        if (range.getBottomGravity() > range.getTopGravity()) {
            bindingResult.rejectValue("topGravity", "error.top.gravitty.le.bottom.gravity");
            return "rangeAddEdit";
        }
        if (beerFermControlFacade.overlapsOtherRange(range, true)) {
            bindingResult.rejectValue("topGravity", "error.overlaps.other.range");
            bindingResult.rejectValue("bottomGravity", "error.overlaps.other.range");
            return "rangeAddEdit";
        }
        beerFermControlFacade.updateRange(range, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.range.updated");
        return "redirect:/config/" + configId;
    }

}
