// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ExpressionClause;
import org.apache.camel.model.RecipientListDefinition;



public class Recipientlist extends RouteBuilder {
  @Override
  public void configure() throws Exception {

    from("timer:recipientlist?repeatCount=1")
      .setHeader("destination", constant("direct:route-a;direct:route-c"))
      .recipientList(header("destination"), ";");

    from("direct:route-a")
      .log("route-a invoked");

    from("direct:route-b")
      .log("route-b invoked");
      
    from("direct:route-c")
      .log("route-c invoked");
      
  }
}