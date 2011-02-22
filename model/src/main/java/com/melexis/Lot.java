package com.melexis;

import java.util.HashSet;
import java.util.Set;

public class Lot {

    private String name;
    private String item;
    private String organization;
    private String probelocation;
    private String subcontractor;
    private Set<Wafer> wafers = new HashSet<Wafer>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getProbelocation() {
        return probelocation;
    }

    public void setProbelocation(String probelocation) {
        this.probelocation = probelocation;
    }

    public String getSubcontractor() {
        return subcontractor;
    }

    public void setSubcontractor(String subcontractor) {
        this.subcontractor = subcontractor;
    }

    public Set<Wafer> getWafers() {
        return wafers;
    }

    public void setWafers(Set<Wafer> wafers) {
        this.wafers = wafers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Lot");
        sb.append("{name='").append(name).append('\'');
        sb.append(", item='").append(item).append('\'');
        sb.append(", organization='").append(organization).append('\'');
        sb.append(", probelocation='").append(probelocation).append('\'');
        sb.append(", subcontractor='").append(subcontractor).append('\'');
        sb.append(", wafers=").append(wafers);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Lot lot = (Lot) o;

        if (item != null ? !item.equals(lot.item) : lot.item != null) {
            return false;
        }
        if (name != null ? !name.equals(lot.name) : lot.name != null) {
            return false;
        }
        if (organization != null ? !organization.equals(lot.organization) : lot.organization != null) {
            return false;
        }
        if (probelocation != null ? !probelocation.equals(lot.probelocation) : lot.probelocation != null) {
            return false;
        }
        if (subcontractor != null ? !subcontractor.equals(lot.subcontractor) : lot.subcontractor != null) {
            return false;
        }
        if (wafers != null ? !wafers.equals(lot.wafers) : lot.wafers != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        result = 31 * result + (probelocation != null ? probelocation.hashCode() : 0);
        result = 31 * result + (subcontractor != null ? subcontractor.hashCode() : 0);
        result = 31 * result + (wafers != null ? wafers.hashCode() : 0);
        return result;
    }
}
