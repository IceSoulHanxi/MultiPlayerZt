package com.ixnah.mpzt.setting;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MpztConfig {

    private final StringProperty multiplayerToken = new SimpleStringProperty();
    private final StringProperty networkId = new SimpleStringProperty();
    // TODO: always orbit moon


    public String getMultiplayerToken() {
        return multiplayerToken.get();
    }

    public StringProperty multiplayerTokenProperty() {
        return multiplayerToken;
    }

    public void setMultiplayerToken(String multiplayerToken) {
        this.multiplayerToken.set(multiplayerToken);
    }

    public String getNetworkId() {
        return networkId.get();
    }

    public StringProperty networkIdProperty() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId.set(networkId);
    }
}
