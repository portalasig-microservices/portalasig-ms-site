package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

/**
 * Enum representing types of evaluations such as lectures, practices, or labs.
 * Defaults to INVALID for unknown codes.
 */
public enum EvaluationType implements Codeable<String> {

    LECTURE("LECTURE"),
    PRACTICE("PRACTICE"),
    LABORATORY("LABORATORY"),
    OTHER("OTHER"),
    INVALID("");

    private static final CodeToEnumMapper<String, EvaluationType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(EvaluationType.class);

    final String code;

    EvaluationType(String code) {
        this.code = code;
    }

    /**
     * Maps a string code to an AssessmentType.
     * Defaults to INVALID if not recognized.
     */
    @JsonCreator
    public static EvaluationType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
