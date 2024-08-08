// camel-k: language=java
// camel-k: dependency=camel-csv
// camel-k: dependency=camel-jackson

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Dataformats extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("timer:split-pattern?period=1000&repeatCount=1")
        .setBody(simple("id,name\n1,apple\n2,orange\n3,lemon"))
        .unmarshal(new CsvDataFormat().setUseMaps(true).setDelimiter(','))
        .marshal().json(JsonLibrary.Jackson)
        .log("${body}");
      
  }
}