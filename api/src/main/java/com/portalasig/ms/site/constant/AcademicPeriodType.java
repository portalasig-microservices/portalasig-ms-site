package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

public enum AcademicPeriodType implements Codeable<String> {

    FIRST("1", 2),
    SECOND("2", 1),
    INTENSIVE("I", 0),
    UNIQUE("U", 0),
    INVALID("", null);

    private static final CodeToEnumMapper<String, AcademicPeriodType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(AcademicPeriodType.class);

    final String code;
    final Integer order;

    AcademicPeriodType(String code, Integer order) {
        this.code = code;
        this.order = order;
    }

    @JsonCreator
    public static AcademicPeriodType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

}
