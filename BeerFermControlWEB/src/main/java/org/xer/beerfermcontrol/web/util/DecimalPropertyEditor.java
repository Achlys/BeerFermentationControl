package org.xer.beerfermcontrol.web.util;

import java.beans.PropertyEditorSupport;
import java.text.DecimalFormat;
import java.util.Locale;
import java.text.NumberFormat;

/**
 *
 * @author Achlys
 */
public class DecimalPropertyEditor extends PropertyEditorSupport {

    private final DecimalFormat df;

    public DecimalPropertyEditor(Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        this.df = (DecimalFormat) nf;
    }

    @Override
    public String getAsText() {
        if(this.getValue() == null){
            return "";
        }else{
            return this.df.format(this.getValue());
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(text == null || "".equals(text.trim())){
            this.setValue(null);
        }else{
            this.setValue(Double.valueOf(text));
        }
    }

}
