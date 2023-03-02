package fr.fne.testgdb.neo4j.repository;

import fr.fne.testgdb.neo4j.model.PersonneNeo4j;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PersonneNeo4jRepository extends Neo4jRepository<PersonneNeo4j, Long> {
}
