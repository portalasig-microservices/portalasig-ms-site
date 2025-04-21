package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

public enum MediaType implements Codeable<String> {

    LINK("LINK"),
    VIDEO("VIDEO"),
    IMAGE("IMAGE"),
    DOCUMENT("DOCUMENT"),
    AUDIO("AUDIO"),
    OTHER("OTHER"),
    INVALID("");

    private static final CodeToEnumMapper<String, MediaType> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(MediaType.class);

    final String code;

    MediaType(String code) {
        this.code = code;
    }

    @JsonCreator
    public static MediaType fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
