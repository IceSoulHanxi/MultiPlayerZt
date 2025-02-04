package com.ixnah.mpzt.ui;

import com.ixnah.mpzt.auth.MultiplayerOfflineAccount;
import com.ixnah.mpzt.network.LocalClientForwarder;
import com.ixnah.mpzt.network.LocalServerBroadcaster;
import com.ixnah.mpzt.network.LocalServerDetector;
import com.ixnah.mpzt.network.NetworkManager;
import com.ixnah.zerotier.central.ZeroTierCentral;
import com.ixnah.zerotier.central.ZeroTierOidc;
import com.ixnah.zerotier.central.model.Member;
import com.ixnah.zerotier.central.model.MemberConfig;
import com.zerotier.sockets.ZeroTierNode;
import javafx.beans.property.*;
import javafx.scene.control.Skin;
import org.jackhuang.hmcl.auth.Account;
import org.jackhuang.hmcl.auth.offline.OfflineAccount;
import org.jackhuang.hmcl.event.Event;
import org.jackhuang.hmcl.setting.Profile;
import org.jackhuang.hmcl.setting.Profiles;
import org.jackhuang.hmcl.task.Schedulers;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.construct.PageAware;
import org.jackhuang.hmcl.ui.decorator.DecoratorAnimatedPage;
import org.jackhuang.hmcl.ui.decorator.DecoratorPage;
import org.jackhuang.hmcl.ui.versions.Versions;
import org.jackhuang.hmcl.util.Lang;

import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.jackhuang.hmcl.ui.FXUtils.runInFX;
import static org.jackhuang.hmcl.util.Lang.wrap;
import static org.jackhuang.hmcl.util.Lang.wrapConsumer;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public class MultiplayerPage extends DecoratorAnimatedPage implements DecoratorPage, PageAware {
    private final ReadOnlyObjectWrapper<State> state = new ReadOnlyObjectWrapper<>(State.fromTitle(i18n("multiplayer")));

    static final String SLAVE_MODE = "SLAVE";
    static final String MASTER_MODE = "MASTER";
    private final StringProperty mode = new SimpleStringProperty(SLAVE_MODE);
    private final StringProperty email = new SimpleStringProperty();

    private final IntegerProperty port = new SimpleIntegerProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty networkId = new SimpleStringProperty();

    private final ReadOnlyObjectWrapper<ZeroTierOidc> oidc = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<ZeroTierCentral> central = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<ZeroTierNode> node = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<LocalClientForwarder> forwarder = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<TimerTask> privateNetworkAcceptor = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<TimerTask> tokenRefreshTimer = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<LocalServerDetector> detector = new ReadOnlyObjectWrapper<>();

    private Consumer<Event> onForwarderExit = null;

    public StringProperty modeProperty() {
        return mode;
    }

    public String getMode() {
        return mode.get();
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty networkIdProperty() {
        return networkId;
    }

    public String getNetworkId() {
        return networkId.get();
    }

    public void setNetworkId(String networkId) {
        this.networkId.set(networkId);
    }

    public ReadOnlyObjectWrapper<ZeroTierOidc> oidcProperty() {
        return oidc;
    }

    public ZeroTierOidc getOidc() {
        return oidc.get();
    }

    public void setOidc(ZeroTierOidc oidc) {
        this.oidc.set(oidc);
    }

    public CompletableFuture<Void> startOidc() {
        if (oidc.get() != null) {
            synchronized (oidc) {
                if (oidc.get() != null) oidc.get().stop();
            }
        }
        return CompletableFuture.supplyAsync(wrap(() -> new ZeroTierOidc(0)), Schedulers.io())
                .thenAccept(wrapConsumer(oidc -> {
                    oidc.start();
                    this.oidc.set(oidc);
                    this.email.set(null);
                    // 登录完成回调
                    oidc.getFuture().thenAccept(v -> onOidcLogin());
                }));
    }

    private void onOidcLogin() {
        ZeroTierCentral api = getCentral();
        api.setToken(getOidc().getToken().getAccessToken());
        api.getStatus().thenAccept(status -> email.set(status.getUser().getEmail()));
    }

    public void openOidcLink() {
        ZeroTierOidc oidc = getOidc();
        if (oidc == null || !oidc.isAlive()) {
            startOidc().thenAcceptAsync(v -> FXUtils.openLink(getOidc().getAuthUri()), Schedulers.javafx());
        } else {
            FXUtils.openLink(oidc.getAuthUri());
        }
    }

    public ReadOnlyObjectWrapper<ZeroTierCentral> centralProperty() {
        return central;
    }

    public ZeroTierCentral getCentral() {
        if (central.get() == null) {
            synchronized (central) {
                if (central.get() == null) {
                    this.central.set(new ZeroTierCentral(""));
                }
            }
        }
        return central.get();
    }

    public void setCentral(ZeroTierCentral central) {
        this.central.set(central);
    }

    public void setCentralToken(String accessToken) {
        getCentral().setToken(accessToken);
    }

    public ReadOnlyObjectWrapper<ZeroTierNode> nodeProperty() {
        return node;
    }

    public ReadOnlyObjectWrapper<LocalClientForwarder> forwarderProperty() {
        return forwarder;
    }

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
        broadcaster.run();
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

    public void startNode() {
        NetworkManager.startNode(getNetworkId()).thenAcceptAsync(node -> {
            if (node != null) {
                this.node.set(node);
                long parsedId = Long.parseUnsignedLong(getNetworkId(), 16);
                address.set(node.getIPv4Address(parsedId).getHostAddress());
            }
        }, Schedulers.javafx());
    }

    public void startPrivateNetworkAcceptor() {
        ZeroTierCentral api = getCentral();
        // 刷新Token定时器
        if (tokenRefreshTimer.get() == null) {
            synchronized (tokenRefreshTimer) {
                if (tokenRefreshTimer.get() == null) {
                    TimerTask tokenRefreshTimer = new TimerTask() {
                        @Override
                        public void run() {
                            getOidc().refreshToken().thenAccept(token -> {
                                api.setToken(token.getAccessToken());
                            });
                        }
                    };
                    long millis = TimeUnit.SECONDS.toMillis(getOidc().getToken().getExpiresIn() / 2);
                    Lang.getTimer().scheduleAtFixedRate(tokenRefreshTimer, millis, millis);
                }
            }
        }

        // 私有网络默认接受所有成员
        String nid = getNetworkId();
        api.getNetworkByID(nid).thenAccept(network -> {
            if (Boolean.TRUE.equals(network.getConfig().getPrivate())) {
                synchronized (privateNetworkAcceptor) {
                    if (privateNetworkAcceptor.get() != null) {
                        privateNetworkAcceptor.get().cancel();
                    }
                    TimerTask acceptorTask = new TimerTask() {
                        @Override
                        public void run() {
                            ZeroTierCentral api = getCentral();
                            api.getNetworkMemberList(nid).thenAccept(members -> {
                                for (Member member : members) {
                                    String id = member.getId();
                                    MemberConfig config = member.getConfig();
                                    if (!Boolean.TRUE.equals(config.getAuthorized())) {
                                        config.setAuthorized(true);
                                        api.updateNetworkMember(member, nid, id);
                                    }
                                }
                            });
                        }
                    };
                    Lang.getTimer().scheduleAtFixedRate(acceptorTask, 0, 1000);
                    privateNetworkAcceptor.set(acceptorTask);
                }
            }
        });
    }

    public void forward(int port) {
        LocalClientForwarder forwarder = new LocalClientForwarder(getAddress(), port);
        onForwarderExit = forwarder.onExit().registerWeak(this::onForwarderExit);
        forwarder.run();
        this.forwarder.set(forwarder);
    }

    public void stopForward() {
        LocalClientForwarder clientForwarder = this.forwarder.get();
        if (clientForwarder != null) {
            clientForwarder.close();
            this.forwarder.set(null);
        }
    }

    private void onForwarderExit(Event event) {
        runInFX(() -> {
            if (this.forwarder.get() == event.getSource()) {
                this.forwarder.set(null);
            }
        });
    }

    public void detectLocal() {
        LocalServerDetector localServerDetector = new LocalServerDetector();
        localServerDetector.run();
        localServerDetector.getFuture().thenAcceptAsync(port -> {
            if (port != null) {
                setPort(Integer.parseInt(port));
            }
        }, Schedulers.javafx());
        this.detector.set(localServerDetector);
    }

    public void stopDetectLocal() {
        LocalServerDetector serverDetector = this.detector.get();
        if (serverDetector != null) {
            serverDetector.close();
            this.detector.set(null);
        }
    }
}
