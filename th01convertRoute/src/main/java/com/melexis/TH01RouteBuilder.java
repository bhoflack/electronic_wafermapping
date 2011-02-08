package com.melexis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class TH01RouteBuilder extends RouteBuilder {

    private final static String NS = "jbi:endpoint:http://melexis.com/electronicwafermapping/";

    public void configure() {
        from(NS + "wafermap-saved/route")
            .to(NS + "th01/convert");
    }
}
