package com.melexis;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.melexis.th01.TH01WaferMap;
import org.apache.commons.codec.binary.Base64;
import org.jvyaml.YAML;

import java.io.IOException;
import java.util.Map;

import static java.lang.String.format;

public class PostprocessingTransformer {

    private final String url;
    private final HttpClient client;

    public PostprocessingTransformer(final String url, final HttpClient client) {
        this.url = url;
        this.client = client;
    }

    public Lot process(final Lot lot) throws IOException {
        final String lotname = lot.getName();
        final String response = client.get(format("%s?lotname=%s", url, lotname));

        final Map<String, Object> l = (Map<String, Object>) YAML.load(response);
        final Map<Long, String> wafermaps = (Map<Long, String>)l.get("wafermaps");
        final Map<Long, TH01WaferMap> thmaps = Maps.transformValues(wafermaps, new Function<String, TH01WaferMap>() {
            public TH01WaferMap apply(final String s) {
                final byte[] bs = Base64.decodeBase64(s);
                return new TH01WaferMap(bs);
            }
        });

        for (final Wafer w : lot.getWafers()) {
            w.getWafermaps().put("postprocessing", thmaps.get(Long.valueOf(w.getWafernumber())).toBytes());
        }

        return lot;
    }
}