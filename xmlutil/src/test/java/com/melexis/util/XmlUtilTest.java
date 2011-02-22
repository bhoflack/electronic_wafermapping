package com.melexis.util;

import com.melexis.Lot;
import com.melexis.Wafer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class XmlUtilTest {

    private XmlUtil xmlUtil;

    @Before public void setUp() {
        xmlUtil = new XmlUtil();
    }

    @Test public void toLot() throws Exception{
        final Lot expected = createLot();

        final String xml = "<lot name=\"A12345\" item=\"201210600\" organization=\"MLX_IEP_OPS_IO\" probelocation=\"MLX_IEP_OPS_IO\" subcontractor=\"O_AMKOR_1\">\n" +
            "  <wafer number=\"1\">\n" +
            "    <wafermap name=\"abc123\" />\n" +
            "    <wafermap name=\"abc456\" />\n" +
            "  </wafer>\n" +  
            "  <wafer number=\"2\"/>\n" +
            "  <wafer number=\"3\"/>\n" +
            "  <wafer number=\"4\"/>\n" +
            "</lot>";

        final Lot lot = xmlUtil.toLot(xml);

        assertEquals(expected, lot);
    }

    @Test public void toXml() throws Exception {
        final Lot expected = createLot();
        final String xml = xmlUtil.toXml(expected);
        final Lot l = xmlUtil.toLot(xml);

        assertEquals(expected, l);
    }

    private Lot createLot() {
        final Lot expected = new Lot();

        final Set<Wafer> wafers = new HashSet<Wafer>();
        final Wafer wafer = new Wafer(1);
        final Map<String, byte[]> wafermaps  = new HashMap<String, byte[]>();
        wafermaps.put("abc123", null);
        wafermaps.put("abc456", null);

        wafer.setWafermaps(wafermaps);

        wafers.add(wafer);
        wafers.add(new Wafer(2));
        wafers.add(new Wafer(3));
        wafers.add(new Wafer(4));

        expected.setName("A12345");
        expected.setItem("201210600");
        expected.setOrganization("MLX_IEP_OPS_IO");
        expected.setProbelocation("MLX_IEP_OPS_IO");
        expected.setSubcontractor("O_AMKOR_1");

        expected.setWafers(wafers);
        return expected;
    }

}
