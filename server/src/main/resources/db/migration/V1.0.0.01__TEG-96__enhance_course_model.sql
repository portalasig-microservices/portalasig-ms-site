CREATE TABLE site_objective_link
(
    site_objective_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id                INT       NOT NULL COMMENT 'Site ID',
    course_objective_id    INT       NOT NULL COMMENT 'Site Objective ID',
    created_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_objective_link_id)
) COMMENT 'Join table between site and its objectives';

CREATE TABLE site_reference_link
(
    site_reference_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id                INT       NOT NULL COMMENT 'Site ID',
    reference_id           INT       NOT NULL COMMENT 'Reference ID',
    created_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_reference_link_id)
) COMMENT 'Join table between site and its references';


CREATE TABLE site_assessment_link
(
    site_assessment_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id                 INT       NOT NULL COMMENT 'site ID',
    site_assessment_id      INT       NOT NULL COMMENT 'site assessment ID',
    created_date            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_assessment_link_id)
) COMMENT 'Join table between site and its assessments';

CREATE TABLE site_news_link
(
    site_news_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id           INT       NOT NULL COMMENT 'Site ID',
    site_news_id      INT       NOT NULL COMMENT 'site news ID',
    created_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_news_link_id)
) COMMENT 'Join table between site and its news';

CREATE TABLE site_media_link
(
    site_media_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id            INT       NOT NULL COMMENT 'Site ID',
    media_id           INT       NOT NULL COMMENT 'Media ID',
    created_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_media_link_id)
) COMMENT 'Join table between site and its media';

CREATE TABLE reference
(
    reference_id   INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    reference_type VARCHAR(32)  NOT NULL COMMENT 'Reference type',
    title          VARCHAR(255) NOT NULL COMMENT 'Reference title',
    url            VARCHAR(255) COMMENT 'Reference URL',
    author         VARCHAR(255) COMMENT 'Reference author',
    description    VARCHAR(255) COMMENT 'Reference description',
    priority       INT          NOT NULL DEFAULT 0 COMMENT 'Reference Priority',
    is_required    TINYINT      NOT NULL DEFAULT 0 COMMENT 'Is this reference required',
    created_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (reference_id)
) COMMENT 'A student reference';

CREATE TABLE course_objective
(
    course_objective_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    title               VARCHAR(255) COMMENT 'Course Objective title',
    description         VARCHAR(255) COMMENT 'Course Objective description',
    priority            INT       NOT NULL DEFAULT 0 COMMENT 'Course Objective Priority',
    created_date        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_objective_id)
) COMMENT 'A course objective';

CREATE TABLE site_course_topic
(
    site_course_topic_id INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id              INT          NOT NULL COMMENT 'Site ID',
    title                VARCHAR(255) NOT NULL COMMENT 'Site course Topic title',
    description          VARCHAR(255) COMMENT 'Site course Topic description',
    created_date         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_course_topic_id)
) COMMENT 'A course topic instance for a site';

CREATE TABLE site
(
    site_id      INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id    INT       NOT NULL COMMENT 'Course id',
    semester_id  INT       NOT NULL COMMENT 'Semester id',
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_id)
) COMMENT 'A site';

CREATE TABLE site_assessment
(
    site_assessment_id INT           NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    weight             DECIMAL(5, 2) NOT NULL DEFAULT 0 COMMENT 'Site Assessment weight',
    name               VARCHAR(255)  NOT NULL COMMENT 'Site Assessment name',
    assessment_type    VARCHAR(32) COMMENT 'Site Assessment type',
    start_date         TIMESTAMP COMMENT 'date and time this assessment starts',
    end_date           TIMESTAMP COMMENT 'date and time this assessment ends',
    created_date       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_assessment_id)
) COMMENT 'A site assessment';

CREATE TABLE media
(
    media_id     INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    name         VARCHAR(255) NOT NULL COMMENT 'Media public name',
    description  VARCHAR(255) COMMENT 'Media file description',
    url          VARCHAR(255) COMMENT 'Media URL',
    file_name    VARCHAR(255) NOT NULL COMMENT 'Media filename',
    file_size    INT          NOT NULL COMMENT 'Media file size',
    media_type   VARCHAR(32)  NOT NULL COMMENT 'Media type',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (media_id)
) COMMENT 'A media record. All from pictures, videos, audio to hyperlinks, etc.';

CREATE TABLE site_news
(
    site_news_id INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    title        VARCHAR(255) NOT NULL COMMENT 'Site News title',
    description  VARCHAR(255) COMMENT 'Site News description',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_news_id)
) COMMENT 'A site news';

CREATE TABLE site_party
(
    party_id     INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id      INT          NOT NULL COMMENT 'Site ID',
    identity     BIGINT       NOT NULL COMMENT 'identity number',
    email        VARCHAR(250) NOT NULL COMMENT 'email',
    first_name   VARCHAR(100) NOT NULL COMMENT 'first name',
    last_name    VARCHAR(100) NOT NULL COMMENT 'last name',
    party_role   VARCHAR(32)  NOT NULL COMMENT 'Site User Role',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (party_id),
    KEY          site_party_idx1(`identity`)
) COMMENT 'A site party';

CREATE TABLE site_user_role
(
    site_user_role_id   INT         NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id             INT         NOT NULL COMMENT 'Site ID',
    user_id             INT         NOT NULL COMMENT 'ms-uaa user id',
    site_user_role_type VARCHAR(32) NOT NULL COMMENT 'Site User Role',
    created_date        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_user_role_id)
) COMMENT 'A site user role';