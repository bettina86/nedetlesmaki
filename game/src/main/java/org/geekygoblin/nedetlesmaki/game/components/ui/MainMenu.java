/*
 * Copyright (c) 2013 devnewton <devnewton@bci.im>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'devnewton <devnewton@bci.im>' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import org.geekygoblin.nedetlesmaki.game.Game;
import org.geekygoblin.nedetlesmaki.game.components.Triggerable;
import org.geekygoblin.nedetlesmaki.game.events.StartGameTrigger;

public class MainMenu extends Component {

    private final Root root;
    private Table mainMenu;
    private VideoConfigurator videoConfigurator;
    private AudioConfigurator audioConfigurator;
    private Table optionsMenu;
    private ControlsConfigurator controls;
    private final Game game;
    private final NuitToolkit toolkit;

    public MainMenu(Game game, NuitToolkit toolkit) throws LWJGLException {
        this.game = game;
        this.toolkit = toolkit;
        root = new Root(toolkit);
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
                game.getAssets().setIcon();
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
                game.setCloseRequested(true);
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
        game.addEntity(game.createEntity().addComponent(new Triggerable(new StartGameTrigger())));
    }
}
