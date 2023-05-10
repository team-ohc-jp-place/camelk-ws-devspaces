// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;

public class Timer extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("timer:java?repeatCount=5")
        .log("Hello World! It's ${date-with-timezone:now:JST:HH:mm:ss}");
      
  }
}