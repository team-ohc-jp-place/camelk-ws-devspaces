// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ExpressionClause;
import org.apache.camel.model.RecipientListDefinition;



public class Recipientlist extends RouteBuilder {
  @Override
  public void configure() throws Exception {

      from("file:data/input")
        .recipientList(simple("${bodyAs(String)}"),";");

      from("direct:a")
        .log("direct:a invoked");
      
      from("direct:b")
        .log("direct:b invoked");
      
      from("direct:c")
        .log("direct:c invoked");
      
  }
}