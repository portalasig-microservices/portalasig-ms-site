CREATE TABLE course
(
    course_id    INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    code         VARCHAR(128) NOT NULL COMMENT 'Course code. Serves as a identifier',
    name         VARCHAR(128) NOT NULL COMMENT 'Course name',
    credit_units SMALLINT UNSIGNED NOT NULL COMMENT 'Number of credit units',
    type         VARCHAR(128) NOT NULL COMMENT 'Course type',
    course_level VARCHAR(32)  NOT NULL COMMENT 'Course level where is required: Semester 1st, 2nd, etc...',
    requirements VARCHAR(255) COMMENT 'Course requirements',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_id),
    UNIQUE KEY (code),
    KEY          course_idx1 (type)
) COMMENT 'A course that is offered by college';

CREATE TABLE course_semester_link
(
    course_semester_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id          INT       NOT NULL COMMENT 'Course ID',
    semester_id        INT       NOT NULL COMMENT 'Semester ID',
    created_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_semester_link_id)
) COMMENT 'A course that is offered in a semester';

CREATE TABLE course_career_link
(
    course_career_link_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id        INT       NOT NULL COMMENT 'Course ID',
    career_id        INT       NOT NULL COMMENT 'Career ID',
    created_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_career_link_id)
) COMMENT 'Join table between course and career';

CREATE TABLE career
(
    career_id    INT         NOT NULL AUTO_INCREMENT COMMENT 'Career ID',
    name         VARCHAR(32) NOT NULL COMMENT 'Career name',
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (career_id)
) COMMENT 'Career offered by college';

CREATE TABLE semester
(
    semester_id  INT         NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    period_type  VARCHAR(16) NOT NULL COMMENT 'academic period type, such as FIRST, SECOND, UNIQUE, INTENSIVE',
    period_year  INT         NOT NULL COMMENT 'academic period year',
    start_date   DATE COMMENT 'Semester start date',
    end_date     DATE COMMENT 'Semester end date',
    is_active    TINYINT(1)       DEFAULT FALSE COMMENT 'Is this semester active?',
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (semester_id),
    KEY          semester_idx1 (semester_id),
    KEY          semester_idx2 (period_type, period_year)
) COMMENT 'A semester in one academic period';

INSERT INTO career (name)
VALUES ("Computación"),
       ("Biología"),
       ("Matemática"),
       ("Física"),
       ("Geoquímica"),
       ("Química");