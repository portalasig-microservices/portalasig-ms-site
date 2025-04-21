package com.portalasig.ms.site.dto.course;

import com.portalasig.ms.site.constant.CourseType;
import com.portalasig.ms.site.dto.Reference;
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
@ApiModel(description = "A college course")
public class Course {

    @ApiModelProperty(value = "Course id")
    private Integer courseId;

    @ApiModelProperty(value = "Course identifier")
    private String code;

    @ApiModelProperty(value = "Course name")
    private String name;

    @ApiModelProperty(value = "Course amount of credit units")
    private Integer creditUnits;

    @ApiModelProperty(value = "Type of courses")
    private CourseType type;

    @ApiModelProperty(value = "Course requirements")
    private String requirements;

    @ApiModelProperty(value = "List of careers ids associated with the course")
    private List<Integer> careers;

    @ApiModelProperty(value = "List of classifications ids associated with the course")
    private List<Integer> classifications;

    @ApiModelProperty(value = "List of classifications ids associated with the course")
    private List<CourseObjective> objectives;

    @ApiModelProperty(value = "List of classifications ids associated with the course")
    private List<Reference> references;

    @ApiModelProperty(value = "List of classifications ids associated with the course")
    private List<CourseTopic> topics;

    @ApiModelProperty(value = "List of classifications ids associated with the course")
    private List<Semester> semesters;
}
