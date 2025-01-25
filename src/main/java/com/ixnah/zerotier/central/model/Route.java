package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Route {

    @SerializedName("target")
    private String target = null;

    @SerializedName("via")
    private String via = null;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(target, route.target) && Objects.equals(via, route.via);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, via);
    }

    @Override
    public String toString() {
        return "Route{" +
                "target='" + target + '\'' +
                ", via='" + via + '\'' +
                '}';
    }
}
