package com.portalasig.ms.site.constant;

import com.portalasig.ms.commons.constants.RestConstants;

public final class SiteRestConstant {

    public static final String CSV_PATH = "/csv";

    public static final class Course {
        public static final class Path {
            public static final String COURSE = "/course";
            public static final String BASE = RestConstants.VERSION_ONE + COURSE;
            public static final String COURSE_CODE = "/{courseCode}";
        }
    }

    public static final class Semester {
        public static final class Path {
            public static final String SEMESTER = "/semester";
            public static final String BASE = RestConstants.VERSION_ONE + SEMESTER;
            public static final String SEMESTER_ID = "/{semesterId:\\d+}";
            public static final String ACTIVE = "/active";
            public static final String SUGGESTED = "/suggested";
        }
    }

    public static final class Site {
        public static final class Path {
            public static final String SITE = "/site";
            public static final String BASE = RestConstants.VERSION_ONE + SITE;
        }
    }

    public static final class SiteObjective {
        public static final class Path {
            public static final String OBJECTIVE = "/objective";
            public static final String BASE = RestConstants.VERSION_ONE + Site.Path.SITE + OBJECTIVE;
        }
    }

}
