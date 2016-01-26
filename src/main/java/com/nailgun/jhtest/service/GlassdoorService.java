package com.nailgun.jhtest.service;

import com.nailgun.jhtest.service.dto.GlassdoorEmployer;
import com.nailgun.jhtest.service.dto.GlassdoorResponse;
import com.nailgun.jhtest.web.rest.dto.GlassdoorEmployerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

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
            String.format(GLASSDOOR_URL_TEMPLATE, employer.getName(), employer.getId()), employer.getOverallRating());
    }

    public static class GlassdoorException extends Exception {}

}
