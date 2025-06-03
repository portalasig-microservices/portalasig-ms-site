package com.portalasig.ms.site.utils.csv;

import com.opencsv.bean.AbstractBeanField;

import java.util.List;

/**
 * Converts a comma-separated string into a list of strings.
 * Used with OpenCSV for mapping CSV fields to List<String>.
 */
public class CommaSeparatedToListConverter extends AbstractBeanField<List<String>, String> {

    /**
     * Splits the input string by commas, trimming whitespace.
     * Returns an empty list if the input is null or empty.
     */
    @Override
    protected List<String> convert(String value) {
        if (value == null || value.isEmpty()) {
            return List.of();
        }
        return List.of(value.split("\\s*,\\s*"));
    }
}
