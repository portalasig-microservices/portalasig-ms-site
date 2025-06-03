package com.portalasig.ms.site.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.portalasig.ms.commons.persistence.CodeToEnumMapper;
import com.portalasig.ms.commons.persistence.Codeable;

/**
 * Enum representing user roles in a course site, such as professor, coordinator, or student.
 * Falls back to INVALID if the code is unknown.
 */
public enum PartyRole implements Codeable<String> {

    PROFESSOR("PROFESSOR"),
    COORDINATOR("COORDINATOR"),
    TEACHING_ASSISTANT("TEACHING_ASSISTANT"),
    STUDENT("STUDENT"),
    INVALID("");

    private static final CodeToEnumMapper<String, PartyRole> CODE_TO_ENUM_MAPPER =
            new CodeToEnumMapper<>(PartyRole.class);

    final String code;

    PartyRole(String code) {
        this.code = code;
    }

    /**
     * Maps a string code to a PartyRole.
     * Returns INVALID if the code is unknown.
     */
    @JsonCreator
    public static PartyRole fromCode(String code) {
        return CODE_TO_ENUM_MAPPER.fromCode(code).isPresent() ? CODE_TO_ENUM_MAPPER.fromCode(code).get() : INVALID;
    }

    @JsonValue
    @Override
    public String getCode() {
        return code;
    }
}
