package com.soubao.config.web;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.bind.ServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomServletRequestDataBinder extends ServletRequestDataBinder {

    private final Pattern underLinePattern = Pattern.compile("_(\\w)");

    public CustomServletRequestDataBinder(final Object target) {
        super(target);
    }

    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        List<PropertyValue> pvs = mpvs.getPropertyValueList();
        List<PropertyValue> adds = new LinkedList<>();
        for (PropertyValue pv : pvs) {
            String name = pv.getName();
            String camel = underLineToCamel(name);
            if (!name.equals(camel)) {
                adds.add(new PropertyValue(camel, pv.getValue()));
            }
        }
        pvs.addAll(adds);
    }

    private String underLineToCamel(final String value) {
        final StringBuffer sb = new StringBuffer();
        Matcher m = underLinePattern.matcher(value);
        while (m.find()){
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
