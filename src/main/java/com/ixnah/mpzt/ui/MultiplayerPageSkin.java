package com.ixnah.mpzt.ui;

import com.ixnah.zerotier.central.model.Network;
import com.jfoenix.controls.*;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.jackhuang.hmcl.task.Schedulers;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.ui.SVG;
import org.jackhuang.hmcl.ui.construct.*;
import org.jackhuang.hmcl.ui.construct.MessageDialogPane.MessageType;
import org.jackhuang.hmcl.ui.decorator.DecoratorAnimatedPage;
import org.jackhuang.hmcl.util.Lang;
import org.jackhuang.hmcl.util.StringUtils;

import static com.ixnah.mpzt.ui.MultiplayerPage.MASTER_MODE;
import static com.ixnah.mpzt.ui.MultiplayerPage.SLAVE_MODE;
import static org.jackhuang.hmcl.ui.versions.VersionPage.wrap;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public class MultiplayerPageSkin extends DecoratorAnimatedPage.DecoratorAnimatedPageSkin<MultiplayerPage> {

    private ObservableList<Node> clients;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected MultiplayerPageSkin(MultiplayerPage control) {
        super(control);

        {
            AdvancedListBox sideBar = new AdvancedListBox()
                    .addNavigationDrawerItem(item -> {
                        item.setTitle(i18n("version.launch"));
                        item.setLeftGraphic(wrap(SVG.ROCKET_LAUNCH_OUTLINE));
                        item.setOnAction(e -> control.launchGame());
                    })
                    .startCategory(i18n("help"))
                    .addNavigationDrawerItem(item -> {
                        item.setTitle(i18n("help"));
                        item.setLeftGraphic(wrap(SVG.HELP_CIRCLE_OUTLINE));
                        item.setOnAction(e -> FXUtils.openLink("https://docs.hmcl.net/multiplayer"));
                    })
//                    .addNavigationDrawerItem(item -> {
//                        item.setTitle(i18n("multiplayer.help.1"));
//                        item.setLeftGraphic(wrap(SVG.helpCircleOutline));
//                        item.setOnAction(e -> FXUtils.openLink("https://docs.hmcl.net/multiplayer/admin.html"));
//                    })
                    .addNavigationDrawerItem(item -> {
                        item.setTitle(i18n("multiplayer.help.2"));
                        item.setLeftGraphic(wrap(SVG.HELP_CIRCLE_OUTLINE));
                        item.setOnAction(e -> FXUtils.openLink("https://docs.hmcl.net/multiplayer/help.html"));
                    })
                    .addNavigationDrawerItem(item -> {
                        item.setTitle(i18n("multiplayer.help.3"));
                        item.setLeftGraphic(wrap(SVG.HELP_CIRCLE_OUTLINE));
                        item.setOnAction(e -> FXUtils.openLink("https://docs.hmcl.net/multiplayer/help.html#%E5%88%9B%E5%BB%BA%E6%96%B9"));
                    })
                    .addNavigationDrawerItem(item -> {
                        item.setTitle(i18n("multiplayer.help.4"));
                        item.setLeftGraphic(wrap(SVG.HELP_CIRCLE_OUTLINE));
                        item.setOnAction(e -> FXUtils.openLink("https://docs.hmcl.net/multiplayer/help.html#%E5%8F%82%E4%B8%8E%E8%80%85"));
                    })
                    .addNavigationDrawerItem(item -> {
                        item.setTitle(i18n("multiplayer.help.text"));
                        item.setLeftGraphic(wrap(SVG.ROCKET_LAUNCH_OUTLINE));
                        item.setOnAction(e -> FXUtils.openLink("https://docs.hmcl.net/multiplayer/text.html"));
                    })
                    .addNavigationDrawerItem(report -> {
                        report.setTitle(i18n("feedback"));
                        report.setLeftGraphic(wrap(SVG.MESSAGE_ALERT_OUTLINE));
//                        report.setOnAction(e -> HMCLService.openRedirectLink("multiplayer-feedback"));
                    });
            FXUtils.setLimitWidth(sideBar, 200);
            setLeft(sideBar);
        }

        {
            VBox content = new VBox(16);
            content.setPadding(new Insets(10));
            content.setFillWidth(true);
            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.setFitToWidth(true);
            setCenter(scrollPane);

            VBox mainPane = new VBox(16);
            {
                ComponentList offPane = new ComponentList();
                {
                    BorderPane modeSelectPane = new BorderPane();
                    {
                        Label modeTitle = new Label("请选择模式");
                        BorderPane.setAlignment(modeTitle, Pos.CENTER_LEFT);
                        modeSelectPane.setLeft(modeTitle);

                        ToggleGroup modeGroup = new ToggleGroup();
                        JFXRadioButton slaveRadioButton = new JFXRadioButton("我是参与方");
                        slaveRadioButton.setUserData(SLAVE_MODE);
                        slaveRadioButton.setToggleGroup(modeGroup);
                        JFXRadioButton masterRadioButton = new JFXRadioButton("我是创建方");
                        masterRadioButton.setUserData(MASTER_MODE);
                        masterRadioButton.setToggleGroup(modeGroup);
                        modeGroup.selectToggle(slaveRadioButton);

                        HBox modeGroupPane = new HBox();
                        modeGroupPane.getChildren().addAll(slaveRadioButton, masterRadioButton);

                        modeGroup.selectedToggleProperty()
                                .addListener((ov, old, newv) -> control.setMode(newv.getUserData().toString()));

                        BorderPane.setAlignment(modeGroupPane, Pos.CENTER_LEFT);
                        BorderPane.setMargin(modeGroupPane, new Insets(0, 8, 0, 8));
                        modeSelectPane.setCenter(modeGroupPane);
                    }

                    ColumnConstraints masterTitleColumn = new ColumnConstraints();
                    ColumnConstraints masterValueColumn = new ColumnConstraints();
                    ColumnConstraints masterRightColumn = new ColumnConstraints();
                    masterValueColumn.setFillWidth(true);
                    masterValueColumn.setHgrow(Priority.ALWAYS);
                    GridPane masterAccountPane = new GridPane();
                    masterAccountPane.setVgap(8);
                    masterAccountPane.setHgap(16);
                    masterAccountPane.getColumnConstraints().setAll(masterTitleColumn, masterValueColumn, masterRightColumn);
                    {
                        BorderPane titlePane = new BorderPane();
                        GridPane.setColumnSpan(titlePane, 3);
                        Label title = new Label(i18n("multiplayer.master"));
                        titlePane.setLeft(title);

                        JFXHyperlink tutorial = new JFXHyperlink(i18n("multiplayer.master.video_tutorial"));
                        titlePane.setRight(tutorial);
//                        tutorial.setOnAction(e -> HMCLService.openRedirectLink("multiplayer-tutorial-master"));
                        masterAccountPane.addRow(0, titlePane);

                        HintPane hintPane = new HintPane(MessageType.INFO);
                        GridPane.setColumnSpan(hintPane, 3);
                        hintPane.setText(i18n("account.hmcl.hint"));
                        masterAccountPane.addRow(1, hintPane);

                        Label accountTitle = new Label(i18n("account"));

                        JFXTextField emailField = new JFXTextField();
                        emailField.setPromptText("暂未登录");
                        emailField.setEditable(false);
                        FXUtils.bindString(emailField, control.emailProperty());

                        JFXButton btnLogin = new JFXButton(i18n("account.login"));
                        btnLogin.getStyleClass().add("dialog-accept");
                        btnLogin.setOnAction(e -> control.openOidcLink());

                        masterAccountPane.addRow(2, accountTitle, emailField, btnLogin);

                        Label networkTitle = new Label(i18n("multiplayer.token"));
                        JFXComboBox<Network> networkSelector = new JFXComboBox<>();
                        networkSelector.setMaxWidth(Double.MAX_VALUE);
                        networkSelector.setConverter(FXUtils.stringConverter(n -> n.getConfig().getName() + " (" + n.getId() + ")"));
                        networkSelector.valueProperty()
                                .addListener((ov, old, newv) -> control.setNetworkId(newv.getId()));
                        JFXButton btnCreateNetwork = new JFXButton("新建");
                        btnCreateNetwork.getStyleClass().add("dialog-accept");
                        btnCreateNetwork.setOnAction(e -> {
                            // TODO: 对话框新建网络
                        });

                        HBox networkPane = new HBox(8);
                        networkPane.setAlignment(Pos.CENTER_LEFT);
                        networkPane.getChildren().addAll(networkTitle, networkSelector, btnCreateNetwork);
                        GridPane.setColumnSpan(networkPane, 3);
                        masterAccountPane.addRow(3, networkPane);
                        FXUtils.onChangeAndOperate(control.emailProperty(), email -> {
                            if (StringUtils.isNotBlank(email)) {
                                control.getCentral().getNetworkList().thenAcceptAsync(list -> {
                                    networkSelector.getItems().setAll(list);
                                    networkSelector.getSelectionModel().select(0);
                                    networkPane.getChildren().addAll(networkTitle, networkSelector, btnCreateNetwork);
                                }, Schedulers.javafx());
                            } else {
                                networkPane.getChildren().clear();
                                networkSelector.getItems().clear();
                                networkSelector.getSelectionModel().clearSelection();
                            }
                        });
                    }

                    ColumnConstraints slaveTitleColumn = new ColumnConstraints();
                    ColumnConstraints slaveValueColumn = new ColumnConstraints();
                    ColumnConstraints slaveRightColumn = new ColumnConstraints();
                    slaveValueColumn.setFillWidth(true);
                    slaveValueColumn.setHgrow(Priority.ALWAYS);

                    GridPane slaveTokenPane = new GridPane();
                    slaveTokenPane.setVgap(8);
                    slaveTokenPane.setHgap(16);
                    slaveTokenPane.getColumnConstraints().setAll(slaveTitleColumn, slaveValueColumn, slaveRightColumn);
                    {
                        Label tokenTitle = new Label(i18n("multiplayer.token"));
                        BorderPane.setAlignment(tokenTitle, Pos.CENTER_LEFT);
                        // Token acts like password, we hide it here preventing users from accidentally leaking their token when taking screenshots.
                        JFXPasswordField tokenField = new JFXPasswordField();
                        BorderPane.setAlignment(tokenField, Pos.CENTER_LEFT);
                        BorderPane.setMargin(tokenField, new Insets(0, 8, 0, 8));
                        tokenField.textProperty().bindBidirectional(control.networkIdProperty());
                        tokenField.setPromptText(i18n("multiplayer.token.prompt"));

                        Validator validator = new Validator("multiplayer.token.format_invalid", StringUtils::isAlphabeticOrNumber);
                        InvalidationListener listener = any -> tokenField.validate();
                        validator.getProperties().put(validator, listener);
                        tokenField.textProperty().addListener(new WeakInvalidationListener(listener));
                        tokenField.getValidators().add(validator);

                        JFXHyperlink applyLink = new JFXHyperlink(i18n("multiplayer.token.apply"));
                        BorderPane.setAlignment(applyLink, Pos.CENTER_RIGHT);
                        slaveTokenPane.addRow(1, tokenTitle, tokenField, applyLink);
                    }

                    HBox startPane = new HBox();
                    {
                        JFXButton startButton = new JFXButton("启动 Zerotier"/*i18n("multiplayer.off.start")*/);
                        startButton.getStyleClass().add("jfx-button-raised");
                        startButton.setButtonType(JFXButton.ButtonType.RAISED);
                        startButton.setOnMouseClicked(e -> {
                            control.startNode();
                            if (MASTER_MODE.equals(control.getMode())) {
                                control.startPrivateNetworkAcceptor();
                            }
                        });
//                        startButton.disableProperty().bind(MultiplayerManager.tokenInvalid);

                        startPane.getChildren().setAll(startButton);
                        startPane.setAlignment(Pos.CENTER_RIGHT);
                    }

                    FXUtils.onChangeAndOperate(control.modeProperty(), mode -> {
                        ObservableList<Node> nodes = offPane.getContent();
                        nodes.clear();
                        if (SLAVE_MODE.equals(mode)) {
                            nodes.addAll(modeSelectPane, slaveTokenPane, startPane);
                        } else {
                            nodes.addAll(modeSelectPane, masterAccountPane, startPane);
                        }
                    });
                }

                ComponentList onPane = new ComponentList();
                {

                    GridPane masterPane = new GridPane();
                    masterPane.setVgap(8);
                    masterPane.setHgap(16);
                    ColumnConstraints titleColumn = new ColumnConstraints();
                    ColumnConstraints valueColumn = new ColumnConstraints();
                    ColumnConstraints rightColumn = new ColumnConstraints();
                    masterPane.getColumnConstraints().setAll(titleColumn, valueColumn, rightColumn);
                    valueColumn.setFillWidth(true);
                    valueColumn.setHgrow(Priority.ALWAYS);
                    {
                        BorderPane titlePane = new BorderPane();
                        GridPane.setColumnSpan(titlePane, 3);
                        Label title = new Label(i18n("multiplayer.master"));
                        titlePane.setLeft(title);

                        JFXHyperlink tutorial = new JFXHyperlink(i18n("multiplayer.master.video_tutorial"));
                        titlePane.setRight(tutorial);
//                        tutorial.setOnAction(e -> HMCLService.openRedirectLink("multiplayer-tutorial-master"));
                        masterPane.addRow(0, titlePane);

                        HintPane hintPane = new HintPane(MessageType.INFO);
                        GridPane.setColumnSpan(hintPane, 3);
                        hintPane.setText(i18n("multiplayer.master.hint"));
                        masterPane.addRow(1, hintPane);

                        Label portTitle = new Label(i18n("multiplayer.master.port"));
                        BorderPane.setAlignment(portTitle, Pos.CENTER_LEFT);

                        JFXTextField portTextField = new JFXTextField();
                        portTextField.getValidators().add(new Validator(i18n("multiplayer.master.port.validate"), (text) -> {
                            Integer value = Lang.toIntOrNull(text);
                            return value != null && 0 < value && value <= 65535;
                        }));
                        portTextField.textProperty().bindBidirectional(control.portProperty(), new StringConverter<Number>() {
                            @Override
                            public String toString(Number object) {
                                return Integer.toString(object.intValue());
                            }

                            @Override
                            public Number fromString(String string) {
                                return Lang.parseInt(string, 0);
                            }
                        });
                        FXUtils.setValidateWhileTextChanged(portTextField, true);

                        HBox actionPane = new HBox();
                        {
                            JFXButton startButton = new JFXButton("开启");
                            startButton.setOnAction(e -> control.forward(control.getPort()));
                            FXUtils.onChangeAndOperate(portTextField.textProperty(), text -> startButton.setDisable(!portTextField.validate()));

                            JFXButton stopButton = new JFXButton("停止");
                            stopButton.setOnAction(e -> control.stopForward());

                            FXUtils.onChangeAndOperate(control.forwarderProperty(), forwarder -> {
                                if (forwarder == null) {
                                    portTextField.setDisable(false);
                                    actionPane.getChildren().setAll(startButton);
                                    control.detectLocal();
                                } else {
                                    portTextField.setDisable(true);
                                    actionPane.getChildren().setAll(stopButton);
                                    control.stopDetectLocal();
                                }
                            });
                        }
                        masterPane.addRow(2, portTitle, portTextField, actionPane);

                        Label serverAddressTitle = new Label(i18n("multiplayer.master.server_address"));
                        BorderPane.setAlignment(serverAddressTitle, Pos.CENTER_LEFT);
                        Label serverAddressLabel = new Label();
                        BorderPane.setAlignment(serverAddressLabel, Pos.CENTER_LEFT);
                        serverAddressLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                            return (control.getAddress() == null ? "" : control.getAddress()) + ":" + control.getPort();
                        }, control.addressProperty(), control.portProperty()));
                        JFXButton copyButton = new JFXButton(i18n("multiplayer.master.server_address.copy"));
                        copyButton.setOnAction(e -> FXUtils.copyText(serverAddressLabel.getText()));
                        masterPane.addRow(3, serverAddressTitle, serverAddressLabel, copyButton);
                    }

                    VBox slavePane = new VBox(8);
                    {
                        BorderPane titlePane = new BorderPane();
                        Label title = new Label(i18n("multiplayer.slave"));
                        titlePane.setLeft(title);

                        JFXHyperlink tutorial = new JFXHyperlink(i18n("multiplayer.slave.video_tutorial"));
//                        tutorial.setOnAction(e -> HMCLService.openRedirectLink("multiplayer-tutorial-slave"));
                        titlePane.setRight(tutorial);

                        HintPane hintPane = new HintPane(MessageType.INFO);
                        GridPane.setColumnSpan(hintPane, 3);
                        hintPane.setText(i18n("multiplayer.slave.hint"));
                        slavePane.getChildren().add(hintPane);

                        HintPane hintPane2 = new HintPane(MessageType.WARNING);
                        GridPane.setColumnSpan(hintPane2, 3);
                        hintPane2.setText(i18n("multiplayer.slave.hint2"));
                        slavePane.getChildren().add(hintPane2);

                        GridPane notBroadcastingPane = new GridPane();
                        {
                            notBroadcastingPane.setVgap(8);
                            notBroadcastingPane.setHgap(16);
                            notBroadcastingPane.getColumnConstraints().setAll(titleColumn, valueColumn, rightColumn);

                            Label addressTitle = new Label(i18n("multiplayer.slave.server_address"));

                            JFXTextField addressField = new JFXTextField();
                            FXUtils.setValidateWhileTextChanged(addressField, true);
                            addressField.getValidators().add(new ServerAddressValidator());

                            JFXButton startButton = new JFXButton(i18n("multiplayer.slave.server_address.start"));
                            startButton.setOnAction(e -> control.broadcast(addressField.getText()));
                            notBroadcastingPane.addRow(0, addressTitle, addressField, startButton);
                        }

                        GridPane broadcastingPane = new GridPane();
                        {
                            broadcastingPane.setVgap(8);
                            broadcastingPane.setHgap(16);
                            broadcastingPane.getColumnConstraints().setAll(titleColumn, valueColumn, rightColumn);

                            Label addressTitle = new Label(i18n("multiplayer.slave.server_address"));
                            Label addressLabel = new Label();
                            addressLabel.textProperty().bind(Bindings.createStringBinding(() ->
                                            control.getBroadcaster() != null ? control.getBroadcaster().getRemoteAddresses().get(0) : "",
                                    control.broadcasterProperty()));

                            JFXButton stopButton = new JFXButton(i18n("multiplayer.slave.server_address.stop"));
                            stopButton.setOnAction(e -> control.stopBroadcasting());
                            broadcastingPane.addRow(0, addressTitle, addressLabel, stopButton);
                        }

                        FXUtils.onChangeAndOperate(control.broadcasterProperty(), broadcaster -> {
                            if (broadcaster == null) {
                                slavePane.getChildren().setAll(titlePane, hintPane, hintPane2, notBroadcastingPane);
                            } else {
                                slavePane.getChildren().setAll(titlePane, hintPane, hintPane2, broadcastingPane);
                            }
                        });
                    }

                    FXUtils.onChangeAndOperate(control.modeProperty(), mode -> {
                        if (SLAVE_MODE.equals(mode)) {
                            onPane.getContent().setAll(slavePane);
                        } else {
                            onPane.getContent().setAll(masterPane);
                        }
                    });
                }

                FXUtils.onChangeAndOperate(control.nodeProperty(), node -> {
                    if (node == null) {
                        mainPane.getChildren().setAll(offPane);
                    } else {
                        mainPane.getChildren().setAll(onPane);
                    }
                });
            }

            ComponentList thanksPane = new ComponentList();
            {
                HBox pane = new HBox();
                pane.setAlignment(Pos.CENTER_LEFT);

                HBox placeholder = new HBox();
                HBox.setHgrow(placeholder, Priority.ALWAYS);

                pane.getChildren().setAll(new Label("Based on ZeroTier"));

                thanksPane.getContent().addAll(pane);
            }

            content.getChildren().setAll(
                    mainPane,
                    ComponentList.createComponentListTitle(i18n("about")),
                    thanksPane
            );
        }
    }

}
