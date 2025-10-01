package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

/**
 * Enum for different schedule types: THEORY, PRACTICE, LABORATORY, OTHER, and INVALID.
 * Implements Codeable for code-based mapping and JSON serialization.
 */
public enum ScheduleType implements Codeable<String> {

    THEORY("THEORY"),
    PRACTICE("PRACTICE"),
    LABORATORY("LABORATORY"),
    OTHER("OTHER"),
    INVALID("");

    private static final CodeToEnumMapper<String, ScheduleType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(ScheduleType.class);

    final String code;

    ScheduleType(String code) {
        this.code = code;
    }

    /**
     * Maps a string code to its corresponding AcademicPeriodType.
     * Returns INVALID if the code is unknown.
     */
    @JsonCreator
    public static ScheduleType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }

}
