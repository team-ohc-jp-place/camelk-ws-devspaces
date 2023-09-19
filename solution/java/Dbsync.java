// camel-k: language=java

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
            .routeId("route-b00a")
            .setBody(jsonpath("$.payload"))
            .choice()
                .when(simple("${body['__op']} == 'u'"))
                    .log("Update: ${body}")
                    .marshal().json()
                    .to("kamelet:postgresql-sink?"
                        + "serverName=postgresql-replica.user1-dev.svc.cluster.local&"
                        + "serverPort=5432&"
                        + "username=demo&"
                        + "password=demo&"
                        + "query=UPDATE products SET name=:#name where id=:#id&"
                        + "databaseName=sampledb")
                .when(simple("${body['__op']} == 'd'"))
                    .log("Delete: ${body}")
                    .marshal().json()
                    .to("kamelet:postgresql-sink?"
                        + "serverName=postgresql-replica.user1-dev.svc.cluster.local&"
                        + "serverPort=5432&"
                        + "username=demo&"
                        + "password=demo&"
                        + "query=DELETE from products where id=:#id&"
                        + "databaseName=sampledb")
                .otherwise()
                    .log("Create: ${body}")
                    .marshal().json()
                    .to("kamelet:postgresql-sink?"
                        + "serverName=postgresql-replica.user1-dev.svc.cluster.local&"
                        + "serverPort=5432&"
                        + "username=demo&"
                        + "password=demo&"
                        + "query=INSERT INTO products (id, name) VALUES (:#id, :#name)&"
                        + "databaseName=sampledb")
            .endChoice();
    }
}