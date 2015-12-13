package com.nailgun.jhtest.repository;

import com.nailgun.jhtest.domain.CoverLetterTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the CoverLetterTemplate entity.
 */
public interface CoverLetterTemplateRepository extends MongoRepository<CoverLetterTemplate,String> {

}
