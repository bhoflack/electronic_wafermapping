package com.melexis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    private final static String NS = "jbi:endpoint:http://melexis.com/electronicwafermapping/";

    public void configure() {
        from(NS + "lot_moved_to_postprocessing/route")
            .to(NS + "postprocesslot/postprocessing");
    }
}
