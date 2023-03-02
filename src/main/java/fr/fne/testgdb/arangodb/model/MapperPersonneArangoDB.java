package fr.fne.testgdb.arangodb.model;

import fr.fne.testgdb.model.Personne;
import org.springframework.stereotype.Component;

@Component
public class MapperPersonneArangoDB {

    public PersonneArangoDB convertPersonneToPersonneArangoDB(Personne personne){
        PersonneArangoDB personneArangoDB = new PersonneArangoDB();
        personneArangoDB.setIsni(personne.getIsni());
        personneArangoDB.setNom(personne.getNom());
        personneArangoDB.setPrenom(personne.getPrenom());
        personneArangoDB.setActivite(personne.getActivite());
        personneArangoDB.setLangue(personne.getLangue());
        personneArangoDB.setUrlPerenne(personne.getUrlPerenne());
        personneArangoDB.setDateDeNaissance(personne.getDateDeNaissance());
        personneArangoDB.setDateDeDeces(personne.getDateDeDeces());
        personneArangoDB.setNoteBibliographique(personne.getNoteBibliographique());

        return personneArangoDB;
    }
}
