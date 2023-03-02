package fr.fne.testgdb.arangodb.model;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge
public class PersonneLink {
    @From
    private PersonneArangoDB fromPersonneArangoDB;

    @To
    private PersonneArangoDB toPersonneArangoDB;

    public PersonneLink(PersonneArangoDB fromPersonneArangoDB, PersonneArangoDB toPersonneArangoDB) {
        this.fromPersonneArangoDB = fromPersonneArangoDB;
        this.toPersonneArangoDB = toPersonneArangoDB;
    }
}
