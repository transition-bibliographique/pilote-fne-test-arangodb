package fr.fne.testgdb.neo4j.model;

import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node("Personne")
public class PersonneNeo4j {

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "EST_LIE", direction = Relationship.Direction.INCOMING)
    private Set<PersonneNeo4j> personnes = new HashSet<>();

    @Property("nom")
    private String nom;
    private String prenom;
    private String isni;
    private String dateDeNaissance;
    private String dateDeDeces;
    private String urlPerenne;
    private String noteBibliographique;
    private String langue;
    private String activite;

    public void addPersonneNeo4j(PersonneNeo4j personneNeo4j) {
        personnes.add(personneNeo4j);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getIsni() {
        return isni;
    }

    public void setIsni(String isni) {
        this.isni = isni;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getDateDeDeces() {
        return dateDeDeces;
    }

    public void setDateDeDeces(String dateDeDeces) {
        this.dateDeDeces = dateDeDeces;
    }

    public String getUrlPerenne() {
        return urlPerenne;
    }

    public void setUrlPerenne(String urlPerenne) {
        this.urlPerenne = urlPerenne;
    }

    public String getNoteBibliographique() {
        return noteBibliographique;
    }

    public void setNoteBibliographique(String noteBibliographique) {
        this.noteBibliographique = noteBibliographique;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    @Override
    public String toString() {
        return "PersonneNeo4j{" +
                "id='" + id + '\'' +
                ", personnes=" + personnes +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", isni='" + isni + '\'' +
                ", dateDeNaissance='" + dateDeNaissance + '\'' +
                ", dateDeDeces='" + dateDeDeces + '\'' +
                ", urlPerenne='" + urlPerenne + '\'' +
                ", noteBibliographique='" + noteBibliographique + '\'' +
                ", langue='" + langue + '\'' +
                ", activite='" + activite + '\'' +
                '}';
    }
}
