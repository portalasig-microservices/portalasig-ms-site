package com.portalasig.ms.site.dto.site;

import com.portalasig.ms.site.constant.ScheduleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO representing a schedule for a section within a site.
 * Contains day of week, start time, end time, and section id.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "A schedule for a section within a site")
public class SiteScheduleRequest {

    @ApiModelProperty(value = "Section id")
    private Integer scheduleId;

    @ApiModelProperty(value = "This schedule use is to be displayed only")
    @NotNull
    private Boolean readOnly;

    @ApiModelProperty(value = "Schedule type")
    @NotNull
    private ScheduleType scheduleType;

    @ApiModelProperty(value = "Day of week")
    @NotNull
    private DayOfWeek day;

    @ApiModelProperty(value = "Start time of course class for a section")
    @NotNull
    private LocalTime startTime;

    @ApiModelProperty(value = "End time of course class for a section")
    @NotNull
    private LocalTime endTime;

    @ApiModelProperty(value = "Location of course class for a section")
    @NotNull
    private String location;

    @ApiModelProperty(value = "Instructor in charge of giving class in this schedule")
    @NotNull
    private SiteParty instructor;

    @ApiModelProperty(value = "Section where schedule will be upserted")
    @NotNull
    private SiteSectionRequest section;

}
