<p>Ce répertoire a pour but de tester l'insertion de notice d'autorité dans des bases de donnée graphe</p>

<p>Pour lancer ArangoDB</p>
<pre><code>docker run -e ARANGO_ROOT_PASSWORD=pwd -p 8529:8529 -d arangodb</code></pre>

<p>Pour lancer Neo4j</p>
<pre><code>docker run --publish=7474:7474 --publish=7687:7687 --volume=$HOME/neo4j/data:/data --env=NEO4J_AUTH=none -d neo4j</code></pre>
