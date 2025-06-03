package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

/**
 * Enum representing types of study references such as books, websites, or articles.
 * Includes fallback to INVALID for unknown values.
 */
public enum ReferenceType implements Codeable<String> {

    BOOK("BOOK"),
    ONLINE("ONLINE"),
    ARTICLE("ARTICLE"),
    DOCUMENT("DOCUMENT"),
    OTHER("OTHER"),
    INVALID("");

    private static final CodeToEnumMapper<String, ReferenceType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(ReferenceType.class);

    final String code;

    ReferenceType(String code) {
        this.code = code;
    }

    /**
     * Maps a string code to a ReferenceType.
     * Defaults to INVALID if the code doesn't match.
     */
    @JsonCreator
    public static ReferenceType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
