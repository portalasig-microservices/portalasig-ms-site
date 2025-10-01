package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

/**
 * Enum representing academic period types such as FIRST, SECOND, or INTENSIVE.
 * Includes a numerical order and falls back to INVALID for unknown codes.
 */
public enum AcademicPeriodType implements Codeable<String> {

    FIRST("FIRST", 0),
    SECOND("SECOND", 1),
    INTENSIVE("INTENSIVE", 2),
    UNIQUE("UNIQUE", 3),
    INVALID("", null);

    private static final CodeToEnumMapper<String, AcademicPeriodType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(AcademicPeriodType.class);

    final String code;
    final Integer order;

    AcademicPeriodType(String code, Integer order) {
        this.code = code;
        this.order = order;
    }

    /**
     * Maps a string code to its corresponding AcademicPeriodType.
     * Returns INVALID if the code is unknown.
     */
    @JsonCreator
    public static AcademicPeriodType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

    /**
     * Returns a short label for the academic period type.
     * For INTENSIVE returns "I", for UNIQUE returns "U", otherwise returns the order as a string.
     */
    public String getPeriodTypeLabel() {
        if (this == INTENSIVE) {
            return "I";
        }
        if (this == UNIQUE) {
            return "U";
        }
        return order.toString();
    }


}
