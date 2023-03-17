<p>Ce répertoire a pour but de tester l'insertion de notice d'autorité dans des bases de donnée graphe</p>

<p>Pour lancer ArangoDB</p>
<pre><code>docker run -e ARANGO_ROOT_PASSWORD=pwd -p 8529:8529 -d arangodb</code></pre>

<p>Pour lancer Neo4j</p>
<pre><code>docker run --publish=7474:7474 --publish=7687:7687 --volume=$HOME/neo4j/data:/data --env=NEO4J_AUTH=none -d neo4j</code></pre>

<p>Pour lancer ApacheAGE</p>
<pre><code>docker run --p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=postgres -d apache/age</code></pre>