package fr.fne.testgdb.arangodb.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import fr.fne.testgdb.arangodb.model.PersonneArangoDB;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneArangoDBRepository extends ArangoRepository<PersonneArangoDB, String> {}