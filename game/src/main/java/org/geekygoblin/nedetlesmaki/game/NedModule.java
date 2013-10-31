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
package org.geekygoblin.nedetlesmaki.game;

import com.artemis.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import im.bci.lwjgl.nuit.NuitToolkit;
import java.io.File;
import javax.inject.Named;
import javax.inject.Singleton;
import org.geekygoblin.nedetlesmaki.game.assets.Assets;
import org.geekygoblin.nedetlesmaki.game.assets.VirtualFileSystem;
import org.geekygoblin.nedetlesmaki.game.components.IngameControls;
import org.geekygoblin.nedetlesmaki.game.components.ZOrder;
import org.geekygoblin.nedetlesmaki.game.components.ui.LevelSelector;
import org.geekygoblin.nedetlesmaki.game.components.ui.MainMenu;
import org.geekygoblin.nedetlesmaki.game.constants.ZOrders;
import org.geekygoblin.nedetlesmaki.game.systems.IngameInputSystem;

/**
 *
 * @author devnewton
 */
public class NedModule extends AbstractModule {

    @Override
    protected void configure() {
        File applicationDir = Main.getApplicationDir();
        bind(VirtualFileSystem.class).toInstance(new VirtualFileSystem(new File(applicationDir, "data"), new File(applicationDir.getParentFile(), "data")));

        bind(Preferences.class);
        bind(Main.class);
        bind(Assets.class);
        bind(NuitToolkit.class).to(NedToolkit.class);
        bind(LevelSelector.class);
        bind(IngameInputSystem.class);
        bind(MainLoop.class);
    }

    @Provides
    @NamedEntities.MainMenu
    @Singleton
    public Entity createMainMenu(Game game, MainMenu mainMenuComponent) {
        Entity mainMenu = game.createEntity();
        mainMenu.addComponent(mainMenuComponent);
        mainMenu.addComponent(new ZOrder(ZOrders.MENU));
        game.addEntity(mainMenu);
        return mainMenu;
    }

    @Provides
    @NamedEntities.IngameControls
    @Singleton
    public Entity createIngameControls(Game game, IngameControls ingameControlsComponent) {
        Entity ingameControls = game.createEntity();
        ingameControls.addComponent(ingameControlsComponent);
        ingameControls.disable();
        game.addEntity(ingameControls);
        return ingameControls;
    }

}
