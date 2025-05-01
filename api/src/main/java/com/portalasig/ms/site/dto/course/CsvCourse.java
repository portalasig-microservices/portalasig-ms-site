package com.portalasig.ms.site.dto.course;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.portalasig.ms.site.constant.CourseLevelType;
import com.portalasig.ms.site.utils.csv.CommaSeparatedToListConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a course in CSV format.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A college course csv")
public class CsvCourse {

    @ApiModelProperty(value = "Course code")
    @CsvBindByPosition(position = 0)
    private String code;

    @ApiModelProperty(value = "Course name")
    @CsvBindByPosition(position = 1)
    private String name;

    @ApiModelProperty(value = "Course amount of credit units")
    @CsvBindByPosition(position = 2)
    private Integer creditUnits;

    @ApiModelProperty(value = "Type of courses")
    @CsvBindByPosition(position = 3)
    private String type;

    @ApiModelProperty(value = "Course requirements")
    @CsvBindByPosition(position = 4)
    private String requirements;

    @ApiModelProperty(value = "List of careers ids associated with the course")
    @CsvCustomBindByPosition(position = 5, converter = CommaSeparatedToListConverter.class)
    private List<Integer> careers;

    @ApiModelProperty(value = "Indicates the semester in which the course is taken (e.g., FIRST, SECOND, etc.)")
    @CsvBindByPosition(position = 6)
    private CourseLevelType courseLevel;
}
