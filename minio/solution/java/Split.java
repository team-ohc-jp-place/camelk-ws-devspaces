// camel-k: language=java
// camel-k: dependency=camel-csv
// camel-k: camel-jackson

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Split extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("file:data/input")
        .unmarshal(new CsvDataFormat().setUseMaps(true).setDelimiter(','))
        .marshal().json(JsonLibrary.Jackson)
        .split().jsonpath("$").parallelProcessing()
        .log("${body}");
      
  }
}