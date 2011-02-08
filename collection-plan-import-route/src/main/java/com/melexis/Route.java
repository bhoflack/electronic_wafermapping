package com.melexis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    public void configure() {
        from("jbi:endpoint:http://melexis.com/electronicwafermapping/lot_moved_to_postprocessing/route")
            .to("jbi:endpoint:http://melexis.com/electronicwafermapping/postprocesslot/postprocessing");
    }
}
