package com.portalasig.ms.site.dto.site;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents a schedule for a section within a site, including day, start and end times, and instructor.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A schedule for a section within a site")
public class SiteSchedule {

    @ApiModelProperty(value = "Section id")
    private Integer scheduleId;

    @ApiModelProperty(value = "Day of week")
    private DayOfWeek day;

    @ApiModelProperty(value = "Start time of course class for a section")
    private LocalTime startTime;

    @ApiModelProperty(value = "End time of course class for a section")
    private LocalTime endTime;

    @ApiModelProperty(value = "Site party associated to this schedule")
    private SiteParty instructor;

}
