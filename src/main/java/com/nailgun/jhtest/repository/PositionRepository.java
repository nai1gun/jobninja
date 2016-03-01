package com.nailgun.jhtest.repository;

import com.nailgun.jhtest.domain.Position;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Position entity.
 */
public interface PositionRepository extends MongoRepository<Position,String> {

    @Query("{ 'user.$id' : ?0 }")
    Page<Position> findByUser(ObjectId userId, Pageable pageable);

    @Query("{ 'user.$id' : ?0, 'link' : ?1 }")
    List<Position> findByLink(ObjectId userId, String link);

}
