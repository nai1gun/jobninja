package com.nailgun.jhtest.repository;

import com.nailgun.jhtest.domain.CoverLetterTemplate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Spring Data MongoDB repository for the CoverLetterTemplate entity.
 */
public interface CoverLetterTemplateRepository extends MongoRepository<CoverLetterTemplate,String> {
    @Query("{ 'user.$id' : ?0 }")
    List<CoverLetterTemplate> findByUser(ObjectId userId);
}
