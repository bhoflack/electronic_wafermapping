package com.melexis;

import java.util.HashMap;
import java.util.Map;

public class Wafer {

    private Integer wafernumber;
    private Integer passdies;
    private Map<String, byte[]> wafermaps = new HashMap<String, byte[]>();

    public Wafer() {

    }

    public Wafer(final Integer wafernumber) {
        this();
        this.wafernumber = wafernumber;
    }

    public Integer getWafernumber() {
        return wafernumber;
    }

    public void setWafernumber(Integer wafernumber) {
        this.wafernumber = wafernumber;
    }

    public Integer getPassdies() {
        return passdies;
    }

    public void setPassdies(Integer passdies) {
        this.passdies = passdies;
    }

    public Map<String, byte[]> getWafermaps() {
        return wafermaps;
    }

    public void setWafermaps(Map<String, byte[]> wafermaps) {
        this.wafermaps = wafermaps;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Wafer");
        sb.append("{wafernumber=").append(wafernumber);
        sb.append(", passdies=").append(passdies);
        sb.append(", wafermaps=").append(wafermaps);
        sb.append('}');
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Wafer wafer = (Wafer) o;

        if (passdies != null ? !passdies.equals(wafer.passdies) : wafer.passdies != null) {
            return false;
        }
        if (wafermaps != null ? !wafermaps.equals(wafer.wafermaps) : wafer.wafermaps != null) {
            return false;
        }
        if (wafernumber != null ? !wafernumber.equals(wafer.wafernumber) : wafer.wafernumber != null) {
            return false;
        }

        return true;
    }

    @Override public int hashCode() {
        int result = wafernumber != null ? wafernumber.hashCode() : 0;
        result = 31 * result + (passdies != null ? passdies.hashCode() : 0);
        result = 31 * result + (wafermaps != null ? wafermaps.hashCode() : 0);
        return result;
    }
}
