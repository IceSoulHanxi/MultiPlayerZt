package com.ixnah.mpzt.ui;

import javafx.beans.property.*;
import javafx.scene.control.Skin;
import org.jackhuang.hmcl.auth.Account;
import org.jackhuang.hmcl.auth.offline.OfflineAccount;
import org.jackhuang.hmcl.event.Event;
import org.jackhuang.hmcl.setting.Profile;
import org.jackhuang.hmcl.setting.Profiles;
import org.jackhuang.hmcl.ui.construct.PageAware;
import org.jackhuang.hmcl.ui.decorator.DecoratorAnimatedPage;
import org.jackhuang.hmcl.ui.decorator.DecoratorPage;
import org.jackhuang.hmcl.ui.versions.Versions;

import java.util.function.Consumer;

import static org.jackhuang.hmcl.ui.FXUtils.runInFX;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public class MultiplayerPage extends DecoratorAnimatedPage implements DecoratorPage, PageAware {
    private final ReadOnlyObjectWrapper<State> state = new ReadOnlyObjectWrapper<>(State.fromTitle(i18n("multiplayer")));

    private final IntegerProperty port = new SimpleIntegerProperty();
    private final StringProperty address = new SimpleStringProperty();

    private final ReadOnlyObjectWrapper<LocalServerBroadcaster> broadcaster = new ReadOnlyObjectWrapper<>();

    private Consumer<Event> onBroadcasterExit = null;

    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public LocalServerBroadcaster getBroadcaster() {
        return broadcaster.get();
    }

    public ReadOnlyObjectWrapper<LocalServerBroadcaster> broadcasterProperty() {
        return broadcaster;
    }

    public void setBroadcaster(LocalServerBroadcaster broadcaster) {
        this.broadcaster.set(broadcaster);
    }

    public void broadcast(String url) {
        LocalServerBroadcaster broadcaster = new LocalServerBroadcaster(url);
        this.onBroadcasterExit = broadcaster.onExit().registerWeak(this::onBroadcasterExit);
        broadcaster.start();
        this.broadcaster.set(broadcaster);
    }

    public void stopBroadcasting() {
        if (getBroadcaster() != null) {
            getBroadcaster().close();
            setBroadcaster(null);
        }
    }

    private void onBroadcasterExit(Event event) {
        runInFX(() -> {
            if (this.broadcaster.get() == event.getSource()) {
                this.broadcaster.set(null);
            }
        });
    }

    void launchGame() {
        Profile profile = Profiles.getSelectedProfile();
        Versions.launch(profile, profile.getSelectedVersion(), (launcherHelper) -> {
            launcherHelper.setKeep();
            Account account = launcherHelper.getAccount();
            if (account instanceof OfflineAccount && !(account instanceof MultiplayerOfflineAccount)) {
                OfflineAccount offlineAccount = (OfflineAccount) account;
                launcherHelper.setAccount(new MultiplayerOfflineAccount(
                        offlineAccount.getDownloader(),
                        offlineAccount.getUsername(),
                        offlineAccount.getUUID(),
                        offlineAccount.getSkin()
                ));
            }
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MultiplayerPageSkin(this);
    }

    @Override
    public ReadOnlyObjectProperty<State> stateProperty() {
        return state;
    }
}
