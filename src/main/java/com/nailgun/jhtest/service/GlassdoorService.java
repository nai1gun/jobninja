package com.nailgun.jhtest.service;

import com.nailgun.jhtest.domain.Position;
import com.nailgun.jhtest.repository.PositionRepository;
import com.nailgun.jhtest.service.dto.GlassdoorEmployer;
import com.nailgun.jhtest.service.dto.GlassdoorResponse;
import com.nailgun.jhtest.web.rest.dto.GlassdoorEmployerDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;

/**
 * @author nailgun
 * @since 25.01.16
 */
@Service
public class GlassdoorService {

    private static final String GET_EMPLOYER_TEMPLATE = "http://api.glassdoor.com/api/api.htm?" +
            "t.p=53407&t.k=bweP7wRQX0G&format=json&v=1&action=employers&ps=1&q={companyName}";

    private static final String GLASSDOOR_URL_TEMPLATE = "https://www.glassdoor.com/Reviews/%s-Reviews-E%s.htm";

    private final Logger log = LoggerFactory.getLogger(GlassdoorService.class);

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private PositionRepository positionRepository;

    public GlassdoorEmployerDTO glassDoorUrl(String companyName) throws GlassdoorException {
        GlassdoorResponse glassdoorResponse = restTemplate.getForObject(GET_EMPLOYER_TEMPLATE,
            GlassdoorResponse.class, companyName);
        if (!glassdoorResponse.isSuccess()) {
            log.error("Glassdoor API call failed with status: '{}'", glassdoorResponse.getStatus());
            throw new GlassdoorException();
        }
        if (glassdoorResponse.getResponse() == null) {
            return null;
        }
        List<GlassdoorEmployer> employers = glassdoorResponse.getResponse().getEmployers();
        if (employers == null || employers.size() == 0) {
            return null;
        }
        GlassdoorEmployer employer = employers.get(0);
        return new GlassdoorEmployerDTO(
            String.format(GLASSDOOR_URL_TEMPLATE, employer.getName(), employer.getId()), employer.getOverallRating(),
            employer.getSquareLogo());
    }

    @Async
    public void getAndSaveCompanyLogoUrl(Position position) {
        if (position.getId() == null) {
            throw new IllegalArgumentException("No position id");
        }
        if (StringUtils.isEmpty(position.getCompany())) {
            return;
        }
        GlassdoorEmployerDTO glassdoorEmployer;
        try {
            glassdoorEmployer = glassDoorUrl(position.getCompany());
        } catch (GlassdoorException e) {
            throw new RuntimeException(e);
        }
        position.setCompanyLogoUrl(glassdoorEmployer.getLogoUrl());
        positionRepository.save(position);
    }

    public static class GlassdoorException extends Exception {}

}
