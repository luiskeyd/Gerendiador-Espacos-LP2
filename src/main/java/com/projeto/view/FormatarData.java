package com.projeto.view;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatarData extends JFormattedTextField.AbstractFormatter {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Date date = (Date) value;
            return dateFormatter.format(date);
        }
        return"";
    }
}