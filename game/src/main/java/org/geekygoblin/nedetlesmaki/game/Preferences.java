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

import im.bci.lwjgl.nuit.controls.Control;
import im.bci.lwjgl.nuit.controls.ControlsUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devnewton
 */
public class Preferences {

    private final Properties store = new Properties();
    private static final String appName = "nedetlesmaki";

    public Preferences() {
        load();
    }

    private void load() {
        File userConfigFile = getUserConfigFilePath();
        if (userConfigFile.exists() && userConfigFile.canRead()) {
            try (FileInputStream is = new FileInputStream(userConfigFile)) {
                store.load(is);
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.WARNING, "Cannot load config from file " + userConfigFile, ex);
            }
        }
    }

    public void saveConfig() {
        File userConfigFile = getUserConfigFilePath();

        if (!userConfigFile.exists()) {
            getUserConfigDirPath().mkdirs();
        }
        try (FileOutputStream os = new FileOutputStream(userConfigFile)) {
            store.store(os, "");

        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot save config to file " + userConfigFile, e);
        }
    }

    public File getUserConfigDirPath() {
        String configDirPath = System.getenv("XDG_CONFIG_HOME");
        if (null == configDirPath) {
            configDirPath = System.getenv("APPDATA");
        }
        if (null == configDirPath) {
            configDirPath = System.getProperty("user.home") + File.separator
                    + ".config";
        }
        return new File(configDirPath, appName);
    }

    private File getUserConfigFilePath() {
        return new File(getUserConfigDirPath(), "config.properties");
    }

    public void putBoolean(String name, boolean value) {
        store.setProperty(name, String.valueOf(value));
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return Boolean.valueOf(store.getProperty(name, String.valueOf(defaultValue)));
    }

    public void putInt(String name, int value) {
        store.setProperty(name, String.valueOf(value));
    }

    public int getInt(String name, int defaultValue) {
        return Integer.valueOf(store.getProperty(name, String.valueOf(defaultValue)));
    }

    public void putControl(String name, Control value) {
        if (null != value) {
            store.setProperty(name + ".controller", value.getControllerName());
            store.setProperty(name + ".control", value.getName());
        } else {
            store.setProperty(name + ".controller", null);
            store.setProperty(name + ".control", null);
        }
    }

    Control getControl(String name, Control defaultValue) {
        String controllerName = store.getProperty(name + ".controller");
        String controlName = store.getProperty(name + ".control");
        for (Control control : ControlsUtils.getPossibleControls()) {
            if (control.getControllerName().equals(controllerName) && control.getName().equals(controlName)) {
                return control;
            }
        }
        return defaultValue;
    }

}