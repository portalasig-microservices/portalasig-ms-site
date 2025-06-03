package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

/**
 * Enum representing types of courses, such as mandatory, elective, or optional mandatory.
 * Returns INVALID for unknown codes.
 */
public enum CourseType implements Codeable<String> {

    OPTIONAL_MANDATORY("OPTIONAL_MANDATORY"),
    MANDATORY("MANDATORY"),
    ELECTIVE("ELECTIVE"),
    OTHER("OTHER"),
    INVALID("");

    private static final CodeToEnumMapper<String, CourseType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(CourseType.class);

    final String code;

    CourseType(String code) {
        this.code = code;
    }

    /**
     * Maps a string code to a CourseType.
     * Returns INVALID if no match is found.
     */
    @JsonCreator
    public static CourseType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
