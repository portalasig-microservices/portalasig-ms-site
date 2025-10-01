CREATE TABLE site_section
(
    section_id   INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id      INT          NOT NULL COMMENT 'Site ID',
    code         VARCHAR(128) NOT NULL COMMENT 'code that identifies the section. e.g: Laboratory I, A1, A2, etc...',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (section_id),
    UNIQUE KEY (code),
    KEY          section_idx1 (code)
) COMMENT 'A site course section, meant to split students into functional groups';

CREATE TABLE site_section_schedule
(
    schedule_id  INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    section_id   INT          NOT NULL COMMENT 'Site section id',
    party_id     INT          NOT NULL COMMENT 'Site user party id',
    read_only    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT 'Describes if this schedule is meant to be displayed only or not.',
    schedule_type VARCHAR(16) NOT NULL COMMENT 'Type of activity that will be dictated in the specified schedule',
    day          VARCHAR(16)  NOT NULL COMMENT 'Day of week represented as a string: MONDAY, TUESDAY, etc...',
    start_time   TIME         NOT NULL COMMENT 'When a class starts',
    end_time     TIME         NOT NULL COMMENT 'When a class ends',
    location     VARCHAR(255) NOT NULL COMMENT 'Location where class will occur',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (schedule_id),
    KEY          site_section_schedule_idx1 (section_id)
) COMMENT 'A site section schedule. It describes when a class starts and finish in a certain day.';