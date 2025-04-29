package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

public enum CourseLevelType implements Codeable<String> {

    FIRST("FIRST"),
    SECOND("SECOND"),
    THIRD("THIRD"),
    FOURTH("FOURTH"),
    FIFTH("FIFTH"),
    SIXTH("SIXTH"),
    SEVENTH("SEVENTH"),
    EIGHTH("EIGHTH"),
    NINTH("NINTH"),
    OPTIONAL("OPTIONAL"),
    INVALID("");

    private static final CodeToEnumMapper<String, CourseLevelType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(CourseLevelType.class);

    final String code;

    CourseLevelType(String code) {
        this.code = code;
    }

    @JsonCreator
    public static CourseLevelType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
