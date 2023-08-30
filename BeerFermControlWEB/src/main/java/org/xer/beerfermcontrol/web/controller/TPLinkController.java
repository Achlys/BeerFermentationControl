package org.xer.beerfermcontrol.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xer.beerfermcontrol.core.bean.Tplink;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.core.util.CoreConstants;
import org.xer.beerfermcontrol.web.util.WebConstants;

/**
 *
 *
 * @author Achlys
 */
@Controller
@SessionAttributes({WebConstants.USER})
@RequestMapping("/config/{configId}/tplink")
public class TPLinkController {

    private static final Logger LOGGER = LogManager.getLogger(TPLinkController.class);

    @Autowired
    private BeerFermControlFacade beerFermControlFacade;

    @RequestMapping(value = "/add/cold", method = RequestMethod.GET)
    public String loadAddCold(@PathVariable("configId") Integer configId, Model model) {
        Tplink tplink = new Tplink();
        tplink.setConfigId(configId);
        tplink.setType(CoreConstants.TPLINK_TYPE_COLD);
        model.addAttribute(WebConstants.TPLINK, tplink);
        return "tplinkAddEdit";
    }

    @RequestMapping(value = "/add/warm", method = RequestMethod.GET)
    public String loadAddWarm(@PathVariable("configId") Integer configId, Model model) {
        Tplink tplink = new Tplink();
        tplink.setConfigId(configId);
        tplink.setType(CoreConstants.TPLINK_TYPE_WARM);
        model.addAttribute(WebConstants.TPLINK, tplink);
        return "tplinkAddEdit";
    }

    @RequestMapping(value = "/add/cold", method = RequestMethod.POST)
    public String addCold(@PathVariable("configId") Integer configId, @Valid @ModelAttribute(WebConstants.TPLINK) Tplink tplink, BindingResult bindingResult,
            Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "tplinkAddEdit";
        }
        tplink.setConfigId(configId);
        tplink.setType(CoreConstants.TPLINK_TYPE_COLD);
        beerFermControlFacade.addTplink(tplink, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.tplink.added");
        return "redirect:/config/" + configId;
    }

    @RequestMapping(value = "/add/warm", method = RequestMethod.POST)
    public String addWarm(@PathVariable("configId") Integer configId, @Valid @ModelAttribute(WebConstants.TPLINK) Tplink tplink, BindingResult bindingResult,
            Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "tplinkAddEdit";
        }
        tplink.setConfigId(configId);
        tplink.setType(CoreConstants.TPLINK_TYPE_WARM);
        beerFermControlFacade.addTplink(tplink, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.tplink.added");
        return "redirect:/config/" + configId;
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String remove(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        beerFermControlFacade.removeTplink(id, configId, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.tplink.removed");
        return "redirect:/config/" + configId;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String loadEdit(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, Model model) {
        model.addAttribute(WebConstants.TPLINK, beerFermControlFacade.getTplink(id, configId,
                ((User) model.asMap().get(WebConstants.USER)).getId()));
        return "tplinkAddEdit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id, @Valid @ModelAttribute(WebConstants.TPLINK) Tplink tplink,
            BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "tplinkAddEdit";
        }
        tplink.setId(id);
        tplink.setConfigId(configId);
        beerFermControlFacade.updateTplink(tplink, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.tplink.updated");
        return "redirect:/config/" + configId;
    }

    @RequestMapping(value = "/{id}/encender", method = RequestMethod.GET)
    @ResponseBody
    public String encender(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id) throws Exception {
        return beerFermControlFacade.encender(id, configId);
    }

    @RequestMapping(value = "/{id}/apagar", method = RequestMethod.GET)
    @ResponseBody
    public String apagar(@PathVariable("configId") Integer configId, @PathVariable("id") Integer id) throws Exception {
        return beerFermControlFacade.apagar(id, configId);
    }

}
