CREATE TABLE course
(
    course_id    INT          NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    code         VARCHAR(128) NOT NULL COMMENT 'Course code. Serves as a identifier',
    name         VARCHAR(128) NOT NULL COMMENT 'Course name',
    credit_units SMALLINT UNSIGNED NOT NULL COMMENT 'Number of credit units',
    type         VARCHAR(128) NOT NULL COMMENT 'Course type',
    requirements VARCHAR(255) COMMENT 'Course requirements',
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_id),
    UNIQUE KEY (code),
    KEY          course_idx1 (type)
) COMMENT 'A course that is offered by college';

CREATE TABLE course_semester
(
    course_semester_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id          INT       NOT NULL COMMENT 'Course ID',
    semester_id        INT       NOT NULL COMMENT 'Semester ID',
    created_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_semester_id)
) COMMENT 'A course that is offered in a semester';

CREATE TABLE course_career
(
    course_career_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id        INT       NOT NULL COMMENT 'Course ID',
    career_id        INT       NOT NULL COMMENT 'Career ID',
    created_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_career_id)
) COMMENT 'Join table between course and career';

CREATE TABLE course_classification
(
    course_classification_id INT       NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    course_id                INT       NOT NULL COMMENT 'Course ID',
    classification_id        INT       NOT NULL COMMENT 'Classification ID',
    created_date             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (course_classification_id)
) COMMENT 'Join table between course and classification';

CREATE TABLE career
(
    career_id    INT         NOT NULL AUTO_INCREMENT COMMENT 'Career ID',
    name         VARCHAR(32) NOT NULL COMMENT 'Career name',
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (career_id)
) COMMENT 'Career offered by college';

CREATE TABLE classification
(
    classification_id INT         NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    name              VARCHAR(32) NOT NULL COMMENT 'Classification name',
    created_date      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (classification_id)
) COMMENT 'Classification of a course based on the semester where its supposed to be taken';

CREATE TABLE semester
(
    semester_id     INT         NOT NULL AUTO_INCREMENT COMMENT 'primary key, auto increment',
    academic_period VARCHAR(32) NOT NULL COMMENT 'Academic period',
    description     VARCHAR(128) COMMENT 'Semester description',
    name            VARCHAR(128) COMMENT 'Semester name',
    start_date      DATE COMMENT 'Semester start date',
    end_date        DATE COMMENT 'Semester end date',
    is_active       TINYINT(1)       DEFAULT FALSE COMMENT 'Is this semester active?',
    created_date    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'date and time this row was created',
    updated_date    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'date and time this row was last updated',
    PRIMARY KEY (semester_id),
    KEY             semester_idx1 (academic_period)
) COMMENT 'A semester in one academic period';

INSERT INTO classification (name)
VALUES ("Semestre I"),
       ("Semestre II"),
       ("Semestre III"),
       ("Semestre IV"),
       ("Semestre V"),
       ("Semestre VI"),
       ("Semestre VII"),
       ("Semestre VIII"),
       ("Semestre IX");

INSERT INTO career (name)
VALUES ("Computación"),
       ("Biología"),
       ("Matemática"),
       ("Física"),
       ("Geoquímica"),
       ("Química");