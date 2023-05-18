// camel-k: language=java dependency=camel-atlasmap

import org.apache.camel.builder.RouteBuilder;

public class Dbsync extends RouteBuilder {
  @Override
  public void configure() throws Exception {

    from("kamelet:kafka-source?" 
        + "topic=debezium.public.products&"
        + "bootstrapServers=kafka-cluster-kafka-bootstrap.user1-dev.svc:9092&"
        + "securityProtocol=PLAINTEXT&"
        + "user=demo&"
        + "password=demo&"
        + "autoOffsetReset=earliest")
    .to("atlasmap:file:/etc/camel/resources/atlasmap-mapping.adm")
    .choice()
        .when(simple("${body.contains(\"op\":\"u\")}"))
            .log("UPDATE: ${body}")
            .to("kamelet:postgresql-sink?" 
                + "serverName=postgresql-replica.user1-dev.svc.cluster.local&" 
                + "serverPort=5432&" 
                + "username=demo&" 
                + "password=demo&" 
                + "query=UPDATE products SET name=:#name where id=:#id&" 
                + "databaseName=sampledb")
        .when(simple("${body.contains(\"op\":\"d\")}"))
            .log("DELETE: ${body}")
            .to("kamelet:postgresql-sink?" 
                + "serverName=postgresql-replica.user1-dev.svc.cluster.local&" 
                + "serverPort=5432&" 
                + "username=demo&" 
                + "password=demo&" 
                + "databaseName=sampledb&" 
                + "query=DELETE from products where id=:#id")
        .otherwise()
            .log("CREATE: ${body}")
            .to("kamelet:postgresql-sink?" 
                + "serverName=postgresql-replica.user1-dev.svc.cluster.local&" 
                + "serverPort=5432&" 
                + "username=demo&" 
                + "password=demo&" 
                + "databaseName=sampledb&" 
                + "query=INSERT INTO products (id, name) VALUES (:#id, :#name)");
      
  }
}