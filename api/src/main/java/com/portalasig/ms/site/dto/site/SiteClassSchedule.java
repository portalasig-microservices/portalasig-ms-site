package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.AssessmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A Course class schedule for a site")
public class SiteClassSchedule {

    @ApiModelProperty(value = "Site class schedule id")
    private Long siteClassScheduleId;

    @ApiModelProperty(value = "Class section")
    private String classSection;

    @ApiModelProperty(value = "Start date for the class")
    private Instant startDate;

    @ApiModelProperty(value = "end date for the class")
    private Instant endDate;

    @ApiModelProperty(value = "classroom (if applies)")
    private String classroom;

    @ApiModelProperty(value = "Type of class")
    private AssessmentType classScheduleType;

    @ApiModelProperty(value = "Professor in charge of the class")
    private SiteParty professor;

}
