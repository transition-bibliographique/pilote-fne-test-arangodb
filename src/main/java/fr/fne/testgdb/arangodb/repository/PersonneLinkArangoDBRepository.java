package fr.fne.testgdb.arangodb.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import fr.fne.testgdb.arangodb.model.PersonneLink;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneLinkArangoDBRepository extends ArangoRepository<PersonneLink, String> {}
