package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

public enum AssessmentType implements Codeable<String> {

    LECTURE("LECTURE"),
    PRACTICE("PRACTICE"),
    LABORATORY("LABORATORY"),
    OTHER("OTHER"),
    INVALID("");

    private static final CodeToEnumMapper<String, AssessmentType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(AssessmentType.class);

    final String code;

    AssessmentType(String code) {
        this.code = code;
    }

    @JsonCreator
    public static AssessmentType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
