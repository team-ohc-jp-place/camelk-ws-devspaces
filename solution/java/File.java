// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;

public class File extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("file:data/input")
        .log("${body}")
        .setBody(simple("${body} It's ${date-with-timezone:now:JST:HH:mm:ss} now."))
        .to("file:data/output");
      
  }
}