package com.portalasig.ms.site.constant;

import com.portalasig.ms.commons.constants.RestConstants;

/**
 * REST path constants for the site module.
 */
public final class SiteRestConstant {

    /**
     * Base path for CSV import endpoints.
     */
    public static final String CSV_PATH = "/csv";

    /**
     * Paths related to course endpoints.
     */
    public static final class Course {
        /**
         * Path definitions for course operations.
         */
        public static final class Path {
            public static final String COURSE = "/course";
            public static final String BASE = RestConstants.VERSION_ONE + COURSE;
            public static final String COURSE_CODE = "/{courseCode}";
        }
    }

    /**
     * Paths related to semester endpoints.
     */
    public static final class Semester {
        /**
         * Path definitions for semester operations.
         */
        public static final class Path {
            public static final String SEMESTER = "/semester";
            public static final String BASE = RestConstants.VERSION_ONE + SEMESTER;
            public static final String SEMESTER_ID = "/{semesterId:\\d+}";
            public static final String ACTIVE = "/active";
            public static final String SUGGESTED = "/suggested";
        }
    }

    /**
     * Paths related to site endpoints.
     */
    public static final class Site {
        /**
         * Path definitions for site operations.
         */
        public static final class Path {
            public static final String SITE = "/site";
            public static final String BASE = RestConstants.VERSION_ONE + SITE;
            public static final String ELEMENT = "/{siteId:\\d+}";
        }
    }

    /**
     * Paths related to course objectives within a site.
     */
    public static final class SiteObjective {
        /**
         * Path definitions for site objective operations.
         */
        public static final class Path {
            public static final String OBJECTIVE = "/objective";
            public static final String BASE = RestConstants.VERSION_ONE + Site.Path.SITE + OBJECTIVE;
            public static final String ELEMENT = "/{courseObjectiveId:\\d+}";
        }
    }

    /**
     * Paths related to course references within a site.
     */
    public static final class SiteReference {
        /**
         * Path definitions for site reference operations.
         */
        public static final class Path {
            public static final String REFERENCE = "/reference";
            public static final String BASE = RestConstants.VERSION_ONE + Site.Path.SITE + REFERENCE;
            public static final String ELEMENT = "/{referenceId:\\d+}";
        }
    }

    /**
     * Paths related to site course topics within a site.
     */
    public static final class SiteCourseTopic {
        /**
         * Path definitions for site course topics operations.
         */
        public static final class Path {
            public static final String COURSE_TOPIC = "/course-topic";
            public static final String BASE = RestConstants.VERSION_ONE +
                    Site.Path.SITE +
                    Site.Path.ELEMENT +
                    COURSE_TOPIC;
            public static final String ELEMENT = "/{siteCourseTopicId:\\d+}";
        }
    }

    /**
     * Paths related to site parties within a site.
     */
    public static final class SiteParty {
        /**
         * Path definitions for site party operations.
         */
        public static final class Path {
            public static final String PARTY = "/party";
            public static final String STUDENT = "/student";
            public static final String LIST = "/list";
            public static final String BASE =
                    RestConstants.VERSION_ONE + Site.Path.SITE + Site.Path.ELEMENT + PARTY;
            public static final String ELEMENT = "/{partyId:\\d+}";
            public static final String IDENTITY = "/{identity:\\d+}";
            public static final String FIND = "/find";
            public static final String CSV = "/csv";
        }
    }

    /**
     * Paths related to site sections within a site.
     */
    public static final class SiteSection {
        /**
         * Path definitions for site section operations.
         */
        public static final class Path {
            public static final String SECTION = "/section";
            public static final String BASE = RestConstants.VERSION_ONE +
                    Site.Path.SITE +
                    Site.Path.ELEMENT +
                    SECTION;
            public static final String ELEMENT = "/{sectionId:\\d+}";
            public static final String SEARCH = "/search";
        }
    }

    /**
     * Paths related to site section schedules within a site section.
     */
    public static final class SiteSectionSchedule {
        /**
         * Path definitions for site section schedule operations.
         */
        public static final class Path {
            public static final String SCHEDULE = "/schedule";
            public static final String BASE = RestConstants.VERSION_ONE +
                    Site.Path.SITE +
                    Site.Path.ELEMENT +
                    SiteSection.Path.SECTION;
            public static final String ELEMENT = "/{scheduleId:\\d+}";
        }
    }
}
