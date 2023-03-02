package fr.fne.testgdb.neo4j.model;

import fr.fne.testgdb.model.Personne;
import org.springframework.stereotype.Component;

@Component
public class MapperPersonneNeo4j {

    public PersonneNeo4j convertPersonneToPersonneNeo4j(Personne personne){
        PersonneNeo4j personneNeop4j = new PersonneNeo4j();
        personneNeop4j.setIsni(personne.getIsni());
        personneNeop4j.setNom(personne.getNom());
        personneNeop4j.setPrenom(personne.getPrenom());
        personneNeop4j.setActivite(personne.getActivite());
        personneNeop4j.setLangue(personne.getLangue());
        personneNeop4j.setUrlPerenne(personne.getUrlPerenne());
        personneNeop4j.setDateDeNaissance(personne.getDateDeNaissance());
        personneNeop4j.setDateDeDeces(personne.getDateDeDeces());
        personneNeop4j.setNoteBibliographique(personne.getNoteBibliographique());

        return personneNeop4j;
    }

}
