package fr.fne.arangodb.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import fr.fne.arangodb.model.Personne;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends ArangoRepository<Personne, String> {}