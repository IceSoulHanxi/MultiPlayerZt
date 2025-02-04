package com.ixnah.mpzt;

import com.ixnah.hmcl.api.LoaderApi;
import com.ixnah.mpzt.asm.MpztTransformer;
import com.ixnah.mpzt.network.NetworkManager;
import com.ixnah.mpzt.setting.MpztConfig;
import com.ixnah.mpzt.ui.MultiplayerPage;
import org.jackhuang.hmcl.setting.Config;
import org.jackhuang.hmcl.ui.Controllers;
import org.jackhuang.hmcl.ui.SVG;
import org.jackhuang.hmcl.ui.construct.AdvancedListBox;
import org.jackhuang.hmcl.ui.construct.AdvancedListItem;
import org.jackhuang.hmcl.util.Lazy;
import org.pf4j.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.jackhuang.hmcl.ui.versions.VersionPage.wrap;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public class MpztPlugin extends Plugin {

    private static Lazy<MultiplayerPage> multiplayerPage = new Lazy<>(MultiplayerPage::new);
    private static MpztPlugin instance;
    private Path pluginDir;
    private MpztConfig config;

    public MpztPlugin() {
        LoaderApi.registerTransformers(MpztTransformer::new);
        instance = this;
    }

    @Override
    public void start() {
        try {
            this.pluginDir = LoaderApi.initPluginDir("MultiPlayerZt");
            Path configFile = this.pluginDir.resolve("config.json");
            if (Files.exists(configFile) && Files.isRegularFile(configFile)) {
                this.config = Config.CONFIG_GSON.fromJson(Files.newBufferedReader(configFile), MpztConfig.class);
            } else {
                this.config = new MpztConfig();
            }
            NetworkManager.initialize(this.pluginDir);
        } catch (IOException e) {
            log.error("Failed to initialize MultiPlayerZt", e);
        }
    }

    @Override
    public void stop() {
        multiplayerPage = null;
    }

    public static MultiplayerPage getMultiplayerPage() {
        return multiplayerPage.get();
    }

    public static MpztPlugin getInstance() {
        return instance;
    }

    public static MpztConfig pluginConfig() {
        return getInstance().getConfig();
    }

    public Path getPluginDir() {
        return pluginDir;
    }

    public MpztConfig getConfig() {
        return config;
    }

    public static void onSideBarInit(AdvancedListBox sideBar) {
        AdvancedListItem multiplayerItem = new AdvancedListItem();
        multiplayerItem.setLeftGraphic(wrap(SVG.LAN));
        multiplayerItem.setActionButtonVisible(false);
        multiplayerItem.setTitle(i18n("multiplayer"));
        multiplayerItem.setOnAction(e -> Controllers.navigate(getMultiplayerPage()));

        sideBar.add(multiplayerItem);
    }
}
