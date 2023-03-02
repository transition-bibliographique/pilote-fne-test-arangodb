package fr.fne.testgdb.arangodb.configuration;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = {"fr.fne.testgdb"})
public class ArangoDbConfiguration implements ArangoConfiguration {

    @Value("${arangodb.host}")
    private String host;

    @Value("${arangodb.port}")

    private int port;

    @Value("${arangodb.user}")

    private String user;

    @Value("${arangodb.password}")

    private String password;

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host(host, port)
                .user(user).password(password);
    }

    @Override
    public String database() {
        return "fne-database";
    }
}
