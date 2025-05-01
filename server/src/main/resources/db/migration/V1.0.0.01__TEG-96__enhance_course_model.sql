CREATE TABLE course_objective_link
(
    course_objective_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id                INT       NOT NULL COMMENT 'Course ID',
    course_objective_id      INT       NOT NULL COMMENT 'Course Objective ID',
    created_date             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_objective_link_id)
) COMMENT 'Join table between course and its objectives';

CREATE TABLE course_reference_link
(
    course_reference_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id                INT       NOT NULL COMMENT 'Course ID',
    reference_id             INT       NOT NULL COMMENT 'Reference ID',
    created_date             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_reference_link_id)
) COMMENT 'Join table between course and its references';

CREATE TABLE course_topic_link
(
    course_topic_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id            INT       NOT NULL COMMENT 'Course ID',
    course_topic_id      INT       NOT NULL COMMENT 'Course Topic ID',
    created_date         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_topic_link_id)
) COMMENT 'Join table between course and its topics';

CREATE TABLE site_class_schedule_link
(
    site_class_schedule_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id                     INT       NOT NULL COMMENT 'site ID',
    site_class_schedule_id      INT       NOT NULL COMMENT 'site class schedule ID',
    created_date                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date                TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_class_schedule_link_id)
) COMMENT 'Join table between site and its class schedules';

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

CREATE TABLE site_user_link
(
    site_user_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    site_id           INT       NOT NULL COMMENT 'Site ID',
    user_id           INT       NOT NULL COMMENT 'Site User ID',
    created_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_user_link_id)
) COMMENT 'Join table between site and its related users';

CREATE TABLE reference
(
    reference_id   INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    reference_type VARCHAR(32)  NOT NULL COMMENT 'Reference type',
    title          VARCHAR(255) NOT NULL COMMENT 'Reference title',
    url            VARCHAR(255) COMMENT 'Reference URL',
    author         VARCHAR(255) COMMENT 'Reference author',
    priority       INT          NOT NULL DEFAULT 0 COMMENT 'Reference Priority',
    is_required    TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is this reference required',
    created_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (reference_id)
) COMMENT 'A student reference';

CREATE TABLE course_objective
(
    course_objective_id INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    title               VARCHAR(255) NOT NULL COMMENT 'Course Objective title',
    description         VARCHAR(255) COMMENT 'Course Objective description',
    priority            INT          NOT NULL DEFAULT 0 COMMENT 'Course Objective Priority',
    created_date        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_objective_id)
) COMMENT 'A course objective';

CREATE TABLE course_topic
(
    course_topic_id INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    title           VARCHAR(255) NOT NULL COMMENT 'Course Topic title',
    description     VARCHAR(255) COMMENT 'Course Topic description',
    created_date    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_topic_id)
) COMMENT 'A course topic';

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

CREATE TABLE site_class_schedule
(
    site_class_schedule_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    user_id                INT       NOT NULL COMMENT 'Professor related to the schedule',
    class_section          VARCHAR(8) COMMENT 'Class schedule section',
    start_date             TIMESTAMP COMMENT 'date and time this class schedule starts',
    end_date               TIMESTAMP COMMENT 'date and time this class schedule ends',
    classroom              VARCHAR(255) COMMENT 'Place where class will occur',
    class_schedule_type    VARCHAR(32) COMMENT 'Type of class schedule',
    created_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_class_schedule_id)
) COMMENT 'A site class schedule';

CREATE TABLE media
(
    media_id     INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    name         VARCHAR(255) NOT NULL COMMENT 'Media public name',
    description  VARCHAR(255) COMMENT 'Media file description',
    url          VARCHAR(255) COMMENT 'Media URL',
    filename     VARCHAR(255) NOT NULL COMMENT 'Media filename',
    filesize     INT          NOT NULL COMMENT 'Media file size',
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

CREATE TABLE site_user
(
    site_user_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    user_id      INT       NOT NULL COMMENT 'ms-uaa user id',
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (site_user_id),
    KEY          site_user_idx1(user_id)
) COMMENT 'A site user';

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