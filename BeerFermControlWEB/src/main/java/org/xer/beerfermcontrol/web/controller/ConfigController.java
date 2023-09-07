package org.xer.beerfermcontrol.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.Reading;
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
@RequestMapping("/config")
public class ConfigController {

    private static final Logger LOGGER = LogManager.getLogger(ConfigController.class);

    @Autowired
    private BeerFermControlFacade beerFermControlFacade;
    @Autowired
    private MessageSource messageSource;

    @InitBinder
    public void initBinder(Locale locale, WebDataBinder dataBinder) {
        dataBinder.addCustomFormatter(new DateFormatter(messageSource.getMessage("date.pattern", null, locale)));
        dataBinder.registerCustomEditor(Double.class, new DecimalPropertyEditor(locale));
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String loadAdd(Model model) {
        model.addAttribute(WebConstants.CONFIG, new Config());
        return "configAddEdit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addConfig(@Valid @ModelAttribute(WebConstants.CONFIG) Config newConfig, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "configAddEdit";
        }
        newConfig.setUserId(((User) model.asMap().get(WebConstants.USER)).getId());
        beerFermControlFacade.addConfig(newConfig);
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.config.added");
        return "redirect:/config/" + newConfig.getId();
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String remove(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        beerFermControlFacade.removeConfig(id, ((User) model.asMap().get(WebConstants.USER)).getId());
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.config.removed");
        return "redirect:/configList";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String loadEdit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute(WebConstants.CONFIG, beerFermControlFacade.getConfig(id, ((User) model.asMap().get(WebConstants.USER)).getId()));
        return "configAddEdit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute(WebConstants.CONFIG) Config config, BindingResult bindingResult, Model model, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "configAddEdit";
        }
        config.setId(id);
        config.setUserId(((User) model.asMap().get(WebConstants.USER)).getId());
        beerFermControlFacade.updateConfig(config);
        ra.addFlashAttribute(WebConstants.SUCCES_KEY, "succes.config.updated");
        return "redirect:/config/" + id;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String loadConfig(@PathVariable("id") Integer id, Model model, @ModelAttribute(WebConstants.SUCCES_KEY) String succes) {
        Config config = beerFermControlFacade.getFullConfig(id, ((User) model.asMap().get(WebConstants.USER)).getId());
        model.addAttribute(WebConstants.CONFIG, config);
        model.addAttribute(WebConstants.EVENT_LIST,
                beerFermControlFacade.getEventList(id, ((User) model.asMap().get(WebConstants.USER)).getId()));
        model.addAttribute(WebConstants.READING_LIST,
                beerFermControlFacade.getReadingList(id, ((User) model.asMap().get(WebConstants.USER)).getId(), null));
        if (succes != null) {
            model.addAttribute(WebConstants.SUCCES_KEY, succes);
        }
        return "config";
    }

    @RequestMapping(value = "/{id}/chart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String chart(@PathVariable("id") Integer id, @RequestParam("chartDate") Date chartDate, Model model, Locale locale) {
        List<Reading> readings = beerFermControlFacade.getReadingList(id, ((User) model.asMap().get(WebConstants.USER)).getId(), chartDate);
        JSONObject response = new JSONObject();
        if (readings != null && !readings.isEmpty()) {
            List<String> labels = new ArrayList<>();
            List<Double> gravities = new ArrayList<>();
            List<Double> temperatures = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat(messageSource.getMessage("timestamp.pattern", null, locale));
            for (int i = 1; i <= readings.size(); i++) {
                if (i % 12 == 1) {
                    Reading reading = readings.get(i - 1);
                    labels.add(sdf.format(reading.getMoment()));
                    gravities.add(reading.getGravity());
                    temperatures.add(reading.getTemp());
                }
            }
            response.put("labels", labels);
            response.put("temperatures", temperatures);
            response.put("gravities", gravities);
        }
        return response.toString();
    }

}
