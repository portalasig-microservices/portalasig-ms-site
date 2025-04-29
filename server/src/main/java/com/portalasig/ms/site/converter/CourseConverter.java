package com.portalasig.ms.site.converter;

import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CourseConverter {

    public void updateCareers(CourseEntity course, Set<CareerEntity> incomingCareers) {
        if (incomingCareers.isEmpty()) {
            return;
        }

        if (course.getCareers() == null) {
            Set<CareerEntity> careers = new HashSet<>();
            course.setCareers(careers);
        }

        course.getCareers().forEach(career -> {
            if (!incomingCareers.contains(career)) {
                career.setShouldBeRemoved(true);
            }
        });

        incomingCareers.forEach(career ->
                career.setCourses(new HashSet<>(Set.of(course)))
        );

        course.getCareers().removeIf(CareerEntity::isShouldBeRemoved);
        course.getCareers().addAll(incomingCareers);
    }
}
