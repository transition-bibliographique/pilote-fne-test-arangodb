package fr.fne.arangodb.model;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge
public class PersonneLink {
    @From
    private Personne fromPersonne;

    @To
    private Personne toPersonne;

    public PersonneLink(Personne fromPersonne, Personne toPersonne) {
        this.fromPersonne = fromPersonne;
        this.toPersonne = toPersonne;
    }
}
