package fr.fne.arangodb.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import fr.fne.arangodb.autorite.Collection;
import fr.fne.arangodb.autorite.Record;
import fr.fne.arangodb.model.DtoAutoriteToPersonne;
import fr.fne.arangodb.model.Personne;
import fr.fne.arangodb.model.PersonneLink;
import fr.fne.arangodb.repository.PersonneLinkRepository;
import fr.fne.arangodb.repository.PersonneRepository;
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
    private PersonneRepository personneRepository;

    @Autowired
    private PersonneLinkRepository personneLinkRepository;

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
            int i = 0;
            Personne savedFromPersonne = null;
            Personne savedToPersonne = null;
            for (Record r : collection.getRecordList()) {
                Personne personne = dtoAutoriteToPersonne.unmarshallerNotice(r);
                if(i % 2 == 0){
                    savedFromPersonne = personneRepository.save(personne);
                }else{
                    savedToPersonne = personneRepository.save(personne);
                    PersonneLink savedLink = personneLinkRepository.save(new PersonneLink(savedFromPersonne, savedToPersonne));
                }
                i++;
            }
        }
    }
}
