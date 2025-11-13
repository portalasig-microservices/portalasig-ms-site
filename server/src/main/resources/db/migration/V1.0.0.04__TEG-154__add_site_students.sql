CREATE TABLE site_student
(
    party_id         INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    section_id       INT          NOT NULL COMMENT 'section id',
    identity         BIGINT       NOT NULL COMMENT 'identity number',
    email            VARCHAR(250) NOT NULL COMMENT 'email',
    first_name       VARCHAR(100) NOT NULL COMMENT 'first name',
    last_name        VARCHAR(100) NOT NULL COMMENT 'last name',
    party_role       VARCHAR(32)  NOT NULL COMMENT 'Site User Role',
    party_site_title VARCHAR(128) COMMENT 'title for a party in a specific site. e.g: Student Teacher I, II',
    created_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (party_id),
    KEY              site_student_idx1(`identity`)
) COMMENT 'A site party';