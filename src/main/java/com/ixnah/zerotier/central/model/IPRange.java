package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class IPRange {

    @SerializedName("ipRangeStart")
    private String ipRangeStart = null;

    @SerializedName("ipRangeEnd")
    private String ipRangeEnd = null;

    public String getIpRangeStart() {
        return ipRangeStart;
    }

    public void setIpRangeStart(String ipRangeStart) {
        this.ipRangeStart = ipRangeStart;
    }

    public String getIpRangeEnd() {
        return ipRangeEnd;
    }

    public void setIpRangeEnd(String ipRangeEnd) {
        this.ipRangeEnd = ipRangeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPRange ipRange = (IPRange) o;
        return Objects.equals(ipRangeStart, ipRange.ipRangeStart) && Objects.equals(ipRangeEnd, ipRange.ipRangeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipRangeStart, ipRangeEnd);
    }

    @Override
    public String toString() {
        return "IPRange{" +
                "ipRangeStart='" + ipRangeStart + '\'' +
                ", ipRangeEnd='" + ipRangeEnd + '\'' +
                '}';
    }
}
