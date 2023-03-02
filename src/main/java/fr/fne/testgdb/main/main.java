package fr.fne.testgdb.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import fr.fne.testgdb.arangodb.model.MapperPersonneArangoDB;
import fr.fne.testgdb.arangodb.model.PersonneArangoDB;
import fr.fne.testgdb.arangodb.model.PersonneLink;
import fr.fne.testgdb.autorite.Collection;
import fr.fne.testgdb.autorite.Record;
import fr.fne.testgdb.arangodb.repository.PersonneLinkArangoDBRepository;
import fr.fne.testgdb.arangodb.repository.PersonneArangoDBRepository;
import fr.fne.testgdb.model.Personne;
import fr.fne.testgdb.model.DtoAutoriteToPersonne;
import fr.fne.testgdb.neo4j.model.MapperPersonneNeo4j;
import fr.fne.testgdb.neo4j.model.PersonneNeo4j;
import fr.fne.testgdb.neo4j.repository.PersonneNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class main implements CommandLineRunner {

    @Autowired
    private DtoAutoriteToPersonne dtoAutoriteToPersonne;

    @Autowired
    private PersonneArangoDBRepository personneArangoDBRepository;

    @Autowired
    private PersonneLinkArangoDBRepository personneLinkArangoDBRepository;

    @Autowired
    private MapperPersonneArangoDB mapperPersonneArangoDB;

    @Autowired
    private PersonneNeo4jRepository personneNeo4jRepository;

    @Autowired
    private MapperPersonneNeo4j mapperPersonneNeo4j;

    @Value("${abes.dump}")
    private String dump;

    @Override
    public void run(String... args) throws Exception {
        this.main(args);
    }

    private void main(String[] args) throws IOException {

        JacksonXmlModule xmlModule = new JacksonXmlModule();
        xmlModule.setDefaultUseWrapper(false);
        ObjectMapper objectMapper = new XmlMapper(xmlModule);
        objectMapper.registerModule(new JaxbAnnotationModule());

        List<File> files = Files.walk(Paths.get(dump))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        for (File f : files) {
            Collection collection = objectMapper.readValue(new FileInputStream(f), Collection.class);
//            insertArangoDB(collection);
//            insertNEo4j(collection);
        }
        System.out.println("Fin de batch");
    }

    private void insertArangoDB (Collection collection){
        int i = 0;
        PersonneArangoDB savedFromPersonne = null;
        PersonneArangoDB savedToPersonne = null;
        for (Record r : collection.getRecordList()) {
            Personne personne = dtoAutoriteToPersonne.unmarshallerNotice(r);
            PersonneArangoDB personneArangoDB = mapperPersonneArangoDB.convertPersonneToPersonneArangoDB(personne);
            if(i % 2 == 0){
                savedFromPersonne = personneArangoDBRepository.save(personneArangoDB);
            }else{
                savedToPersonne = personneArangoDBRepository.save(personneArangoDB);
                // Les liens ici non pas de sens, on lie juste les personnes 2 à 2
                PersonneLink savedLink = personneLinkArangoDBRepository.save(new PersonneLink(savedFromPersonne, savedToPersonne));
            }
            i++;
        }
    }

    private void insertNEo4j(Collection collection){
        int i = 0;
        PersonneNeo4j savedFromPersonne = null;

        for (Record r : collection.getRecordList()) {
            Personne personne = dtoAutoriteToPersonne.unmarshallerNotice(r);
            PersonneNeo4j personneNeo4j = mapperPersonneNeo4j.convertPersonneToPersonneNeo4j(personne);
            if(i % 2 == 0){
                savedFromPersonne = personneNeo4jRepository.save(personneNeo4j);
            }else{
                personneNeo4j.addPersonneNeo4j(savedFromPersonne);
                personneNeo4jRepository.save(personneNeo4j);
            }
            i++;
        }
    }
}
