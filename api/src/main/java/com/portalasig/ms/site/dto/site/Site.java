package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.dto.Media;
import com.portalasig.ms.site.dto.course.Course;
import com.portalasig.ms.site.dto.semester.Semester;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A site for a course. Represents an instance of a course in a semester")
public class Site {

    @ApiModelProperty(value = "Site id")
    private Integer siteId;

    @ApiModelProperty(value = "Class schedules for course site")
    private List<SiteClassSchedule> classSchedules;

    @ApiModelProperty(value = "Site belongs to this course")
    private Course course;

    @ApiModelProperty(value = "Class assessments for course site")
    private List<SiteAssessment> assessments;

    @ApiModelProperty(value = "Ccourse site news")
    private List<SiteNews> news;

    @ApiModelProperty(value = "Media files associated with the course site")
    private List<Media> media;

    @ApiModelProperty(value = "Site related users")
    private List<SiteUser> relatedUsers;

    @ApiModelProperty(value = "Semester associated to the site")
    private Semester semester;
}
