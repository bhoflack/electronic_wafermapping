package com.melexis;

import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    private final static String NS = "jbi:endpoint:http://melexis.com/electronicwafermapping/";

    public void configure() {
        // Send all lots that are moved in Oracle to the postprocessing
        from(NS + "lot_moved_to_postprocessing/route")
            .inOut()
            .to(NS + "postprocesslot/postprocessing")
            .to(NS + "numberofdies/numberofdies")
            .inOnly()
            .to(NS + "fill_collectionplan/postprocessing_producer");
    }
}
