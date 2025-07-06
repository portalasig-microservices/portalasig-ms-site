package com.portalasig.ms.site.service;

import com.portalasig.ms.notify.constant.EmailTemplate;
import com.portalasig.ms.notify.dto.EmailRequest;
import com.portalasig.ms.notify.operation.EmailOperations;
import com.portalasig.ms.site.constant.PartyRole;
import com.portalasig.ms.site.domain.entity.RestPaths;
import com.portalasig.ms.site.domain.entity.site.SiteEntity;
import com.portalasig.ms.site.email.template.SitePartyEmailTemplate;
import com.portalasig.ms.site.record.UserInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Service for sending notifications related to site party associations.
 * Handles email notifications when a user is assigned a role in a site.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SiteNotificationService {

    @Qualifier("clientCredentialsEmailClientV1")
    private final EmailOperations emailOperations;

    @Value("${portalasig.fe.url}")
    private final String frontEndUrl;

    private final Map<PartyRole, String> partyRoleLabelMap = Map.of(
            PartyRole.COORDINATOR, "coordinador",
            PartyRole.PROFESSOR, "profesor",
            PartyRole.STUDENT_TEACHER, "preparador"
    );

    /**
     * Sends an email notification to a user when they are associated with a site party role.
     *
     * @param userInformation Information about the user being notified.
     * @param siteEntity      The site entity to which the user is being associated.
     */
    public void notifyPartyAssociation(UserInformation userInformation, SiteEntity siteEntity) {
        var emailRequest = createPartyAssociationEmail(userInformation, siteEntity);
        emailOperations.sendEmail(emailRequest);
    }

    private EmailRequest createPartyAssociationEmail(UserInformation userInformation, SiteEntity siteEntity) {
        String url = UriComponentsBuilder.fromHttpUrl(frontEndUrl)
                .path(RestPaths.FrontEnd.COURSE)
                .pathSegment(siteEntity.getCourse().getCode())
                .pathSegment(siteEntity.getSemester().getAcademicPeriod())
                .toUriString();
        SitePartyEmailTemplate sitePartyEmailTemplate = SitePartyEmailTemplate
                .builder()
                .title(getEmailTitle(userInformation, siteEntity))
                .primaryBody("Ingrese al sitio a través del siguiente enlace:")
                .url(url)
                .urlLabel("Ingresar")
                .closingMessage("Gracias.")
                .build();
        return EmailRequest
                .builder()
                .emailTo(userInformation.user().getEmail())
                .subject(getEmailSubject(userInformation, siteEntity))
                .template(EmailTemplate.APP_NOTIFICATION)
                .templateConfiguration(sitePartyEmailTemplate)
                .build();
    }

    private String getEmailTitle(UserInformation userInformation, SiteEntity siteEntity) {
        PartyRole partyRole = userInformation.siteParty().getPartyRole();
        return String.format(
                "Usted ha sido asignado/a como %s del sitio web de la asignatura: %s para el período académico: %s",
                partyRoleLabelMap.get(partyRole),
                siteEntity.getCourse().getName(),
                siteEntity.getSemester().getAcademicPeriod()
        );
    }

    private String getEmailSubject(UserInformation userInformation, SiteEntity siteEntity) {
        return String.format(
                "Hola, %s. Has sido asignado/a a la asignatura: %s",
                userInformation.user().getFirstName(),
                siteEntity.getCourse().getName()
        );
    }
}