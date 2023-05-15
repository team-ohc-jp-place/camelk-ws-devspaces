// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;

public class Direct extends RouteBuilder {
  @Override
  public void configure() throws Exception {

    from("timer:java?repeatCount=1")
      .to("direct:secondRoute");
    
    from("direct:secondRoute")
      .log("secoundRoute invoked");
      
  }
}