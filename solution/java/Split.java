// camel-k: language=java
// camel-k: dependency=camel-csv

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;

public class Split extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("timer:split-pattern?period=1000&repeatCount=1")
        .setBody(simple("id,name\n1,apple\n2,orange\n3,lemon"))
        .unmarshal(new CsvDataFormat().setUseMaps(true).setDelimiter(','))
        .split(body()).parallelProcessing()
        .log("${body}");
      
  }
}