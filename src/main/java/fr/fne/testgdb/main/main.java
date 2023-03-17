package fr.fne.testgdb.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import fr.fne.testgdb.apacheage.model.PersonneRowMapper;
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
import org.json.JSONObject;
import org.postgresql.jdbc.PgConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.apache.age.jdbc.base.Agtype;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class main implements CommandLineRunner {

    @Autowired
    private DtoAutoriteToPersonne dtoAutoriteToPersonne;

    /*
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
*/

    @Value("${abes.dump}")
    private String dump;

    @Value("${spring.datasource.url}")
    private String apacheageUrl;
    @Value("${spring.datasource.username}")
    private String apacheageUser;
    @Value("${spring.datasource.password}")
    private String apacheagePwd;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        this.main(args);
    }

    private void main(String[] args) throws Exception {

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
            insertApacheAGE(collection);
        }
        System.out.println("Fin de batch");
    }

    private void insertApacheAGE (Collection collection) throws SQLException {

        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS age;");
        jdbcTemplate.execute("LOAD 'age'");
        jdbcTemplate.execute("SET search_path = ag_catalog, \"$user\", public;");


        //Suppression et création du graph "Personnes"
        jdbcTemplate.execute("SELECT * from ag_catalog.drop_graph('personnes',true)");
        jdbcTemplate.execute("SELECT * from ag_catalog.create_graph('personnes')");

        //Création des personnes
        for (Record r : collection.getRecordList()) {
            Personne personne = dtoAutoriteToPersonne.unmarshallerNotice(r);

            jdbcTemplate.execute("select * from ag_catalog.cypher ('personnes', $$\n" +
                    "        create (:Person {" +
                    "               ppn:'"+personne.getPpn()+"'," +
                    "               urlPerenne:'"+personne.getUrlPerenne()+"'," +
                    "               idISNI:'"+personne.getIsni()+"'," +
                    "               nom:'"+personne.getNom()+"'," +
                    "               prenom:'"+personne.getPrenom()+"'," +
                    "               dateNaissance:'"+personne.getDateDeNaissance()+"'," +
                    "               dateDeces:'"+personne.getDateDeDeces()+"'," +
                    "               activite:'"+personne.getActivite().replaceAll("'","\\\\'")+"'," +
                    "               noteBio:'"+personne.getNoteBibliographique()+"'," +
                    "               langue:'"+personne.getLangue()+"'," +
                    "               pointAcces:'"+personne.getPointAcces()+"'" +
                    "        })\n" +
                    "$$) as (person ag_catalog.agtype)");
        }

        //Pour chaque personne, création des liens PPN -> PointAccess
        List<Personne> rows = jdbcTemplate.query("select * from ag_catalog.cypher('personnes', $$\n" +
                " MATCH(v)" +
                " return v \n" +
                "$$) as (v ag_catalog.agtype)",new PersonneRowMapper());

        for (Personne personne : rows) {
            jdbcTemplate.execute("select * from ag_catalog.cypher('personnes', $$\n" +
                    " MATCH (a:Person), (b:Person)" +
                    " where a.ppn = '"+personne.getPpn()+"' and b.ppn = '"+personne.getPointAcces()+"'" +
                    " create (a)-[e:LIE_A { type:'pointAcces' }]->(b)" +
                    " return e \n" +
                    "$$) as (r ag_catalog.agtype)");
        }

        //Affichage des données, en utilisant le type AGType :
        PgConnection connection = DriverManager.getConnection(apacheageUrl, apacheageUser, apacheagePwd).unwrap(PgConnection.class);
        connection.addDataType("agtype", Agtype.class);

        Statement stmt = connection.createStatement();
        stmt.execute("LOAD 'age'");
        stmt.execute("SET search_path = ag_catalog, \"$user\", public;");

        // Run cypher
        ResultSet rs = stmt.executeQuery("SELECT * from cypher('personnes', $$ MATCH (n) RETURN n $$) as (n agtype);");
        while (rs.next()) {
            Agtype returnedAgtype = rs.getObject(1, Agtype.class);

            String nodeLabel = returnedAgtype.getMap().getObject("label").toString();
            String nodeProp =  returnedAgtype.getMap().getObject("properties").toString();

            System.out.println("Vertex : " + nodeLabel + ", \tProps : " + nodeProp);
        }
    }

    /*
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

     */
}
