package fr.fne.arangodb.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import fr.fne.arangodb.model.PersonneLink;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneLinkRepository extends ArangoRepository<PersonneLink, String> {}
