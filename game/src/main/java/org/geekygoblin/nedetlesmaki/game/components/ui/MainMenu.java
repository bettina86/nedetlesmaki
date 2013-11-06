/*
 The MIT License (MIT)

 Copyright (c) 2013 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package org.geekygoblin.nedetlesmaki.game.components.ui;

import com.artemis.Component;
import im.bci.lwjgl.nuit.NuitToolkit;
import java.util.Arrays;

import org.lwjgl.LWJGLException;

import im.bci.lwjgl.nuit.widgets.AudioConfigurator;
import im.bci.lwjgl.nuit.widgets.Button;
import im.bci.lwjgl.nuit.widgets.ControlsConfigurator;
import im.bci.lwjgl.nuit.widgets.Root;
import im.bci.lwjgl.nuit.widgets.Table;
import im.bci.lwjgl.nuit.widgets.VideoConfigurator;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.game.MainLoop;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;

@Singleton
public class MainMenu extends Component {

    private final Root root;
    private Table mainMenu;
    private VideoConfigurator videoConfigurator;
    private AudioConfigurator audioConfigurator;
    private Table optionsMenu;
    private ControlsConfigurator controls;
    private final LevelSelector levelSelector;
    private final MainLoop mainLoop;
    private final NuitToolkit toolkit;
    private final Assets assets;

    @Inject
    public MainMenu(MainLoop mainLoop, NuitToolkit toolkit, Assets assets, LevelSelector levelSelector) throws LWJGLException {
        this.mainLoop = mainLoop;
        this.toolkit = toolkit;
        this.assets = assets;
        root = new Root(toolkit);
        this.levelSelector = levelSelector;
        root.add(levelSelector);
        initVideo();
        initAudio();
        initControls();
        initOptions();
        initMain();
    }

    private void initVideo() throws LWJGLException {
        videoConfigurator = new VideoConfigurator(toolkit) {

            @Override
            protected void changeVideoSettings() {
                super.changeVideoSettings();
                assets.setIcon();
            }

            @Override
            protected void closeVideoSettings() {
                root.show(optionsMenu);
            }
        };
        root.add(videoConfigurator);
    }

    private void initAudio() {
        audioConfigurator = new AudioConfigurator(toolkit) {
            @Override
            protected void closeAudioSettings() {
                root.show(optionsMenu);
            }
        };
    }

    private void initMain() {
        mainMenu = new Table(toolkit);
        mainMenu.defaults().expand();
        mainMenu.cell(new Button(toolkit, "main.menu.button.start") {
            @Override
            public void onOK() {
                onStartGame();
            }
        });
        mainMenu.row();
        mainMenu.cell(new Button(toolkit, "main.menu.button.resume"));
        mainMenu.row();
        mainMenu.cell(new Button(toolkit, "main.menu.button.options") {
            @Override
            public void onOK() {
                root.show(optionsMenu);
            }
        });
        mainMenu.row();
        mainMenu.cell(new Button(toolkit, "main.menu.button.quit") {
            @Override
            public void onOK() {
                mainLoop.setCloseRequested(true);
            }
        });
        mainMenu.row();
        root.add(mainMenu);
    }

    private void initOptions() {
        optionsMenu = new Table(toolkit);
        optionsMenu.defaults().expand();
        optionsMenu.cell(new Button(toolkit, "options.menu.button.video") {
            @Override
            public void onOK() {
                root.show(videoConfigurator);
            }
        });
        optionsMenu.row();
        optionsMenu.cell(new Button(toolkit, "options.menu.button.audio") {
            @Override
            public void onOK() {
                root.show(audioConfigurator);
            }
        });
        optionsMenu.row();
        optionsMenu.cell(new Button(toolkit, "options.menu.button.controls") {
            @Override
            public void onOK() {
                root.show(controls);
            }
        });
        optionsMenu.row();
        optionsMenu.cell(new Button(toolkit, "options.menu.button.back") {
            @Override
            public void onOK() {
                root.show(mainMenu);
            }
        });
        optionsMenu.row();
        root.add(optionsMenu);
    }

    private void initControls() {
        controls = new ControlsConfigurator(toolkit, Arrays.asList(toolkit.getMenuUp(), toolkit.getMenuDown(), toolkit.getMenuLeft(), toolkit.getMenuRight(), toolkit.getMenuOK(), toolkit.getMenuCancel()), null) {
            @Override
            public void onBack() {
                root.show(optionsMenu);
            }
        };
        root.add(controls);
    }

    public void update() {
        root.update();
    }

    public void draw() {
        root.draw();
    }

    private void onStartGame() {
        root.show(levelSelector);
    }

    public void show() {
        root.show(mainMenu);
    }
}