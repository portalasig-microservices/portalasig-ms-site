package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

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
