package com.portalasig.ms.site.converter;

import com.portalasig.ms.site.domain.entity.CareerEntity;
import com.portalasig.ms.site.domain.entity.course.CourseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Converter component for handling transformations related to CourseEntity.
 */
@Component
@RequiredArgsConstructor
public class CourseConverter {

    /**
     * Updates the careers associated with a given course entity.
     * Marks careers for removal if they are no longer present in the incoming set,
     * and adds new careers, maintaining bidirectional relationship consistency.
     *
     * @param course          the course entity to update
     * @param incomingCareers the new set of careers to associate
     */
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
