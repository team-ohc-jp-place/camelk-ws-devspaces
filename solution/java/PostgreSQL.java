// camel-k: language=java
// camel-k: dependency=camel-jackson
// camel-k: dependency=camel-kamelet

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import org.apache.camel.builder.RouteBuilder;

public class PostgreSQL extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("timer:java?repeatCount=1")
        .setBody().constant("{\"id\":4, \"name\":\"melon\"}")
        .to("kamelet:postgresql-sink?"
          + "serverName=postgresql.user1-dev.svc.cluster.local&"
          + "serverPort=5432&"
          + "username=demo&"
          + "password=demo&"
          + "query=insert into products (id, name) values (:#id, :#name)&"
          + "databaseName=sampledb")
        .marshal().json(JsonLibrary.Jackson)
        .to("kamelet:postgresql-sink?"
          + "serverName=postgresql.user1-dev.svc.cluster.local&"
          + "serverPort=5432&"
          + "username=demo&"
          + "password=demo&"
          + "query=select * from products&databaseName=sampledb")
        .log("${body}");
      
  }
}