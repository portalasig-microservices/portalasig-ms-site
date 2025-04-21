package com.portalasig.ms.site.constant;

import com.portalasig.ms.commons.constants.RestConstants;

public final class SiteRestConstant {

    public static final String CSV_PATH = "/csv";

    public static final class Course {
        public static final String COURSE_PATH = "/course";
        public static final String BASE_PATH = RestConstants.VERSION_ONE + COURSE_PATH;
        public static final String COURSE_CODE_PATH = "/{courseCode}";
    }

    public static final class Semester {
        public static final String SEMESTER_PATH = "/semester";
        public static final String BASE_PATH = RestConstants.VERSION_ONE + SEMESTER_PATH;
        public static final String SEMESTER_ID_PATH = "/{semesterId:\\d+}";
        public static final String ACADEMIC_PERIOD_PATH = "/{academicPeriod}";
        public static final String ACTIVE_PATH = "/active";
    }

    public static final class Site {
        public static final String SITE_PATH = "/site";
        public static final String BASE_PATH = RestConstants.VERSION_ONE + SITE_PATH;
    }

}
