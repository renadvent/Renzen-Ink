package io.renzen.ink.Repositories;

import io.renzen.ink.DomainObjects.ImageDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<ImageDO, String> {
}
