/*
 * Copyright © 2013, Pierre Marijon <pierre@marijon.fr>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * The Software is provided "as is", without warranty of any kind, express or 
 * implied, including but not limited to the warranties of merchantability, 
 * fitness for a particular purpose and noninfringement. In no event shall the 
 * authors or copyright holders X be liable for any claim, damages or other 
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in the
 * Software.
 */
package org.geekygoblin.nedetlesmaki.core.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.artemis.Entity;

import org.geekygoblin.nedetlesmaki.core.utils.Square;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;

/**
 *
 * @author natir
 */
@Singleton
public class LevelIndex {

    private Square[][] index;
    private int height, width;

    public LevelIndex() {
        this.height = -1;
        this.width = -1;
    }

    public LevelIndex(int height, int width) {
        this.initialize(height, width);
    }

    public void initialize(int height, int width) {
        this.height = height;
        this.width = width;
        this.index = new Square[this.height][this.width];
    }

    public void added(Entity e, Position p) {
        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setEntity(e);
        }
    }

    public void deleted(Entity e, Position p) {
        if (p != null) {
            Square s = this.index[p.getX()][p.getY()];
            if (s != null) {
                s.setEntity(null);
            }
        }
    }

    public void disabled(Entity e, Position p) {
        if (p != null) {
            Square s = this.index[p.getX()][p.getY()];
            if (s != null) {
                s.setEntity(null);
            }
        }
    }

    public void enabled(Entity e, Position p) {
        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            e.enable();
            this.index[p.getX()][p.getY()].setEntity(e);
        }
    }

    public void addPlate(Entity e, Position p) {
        if (p != null) {
            if (this.index[p.getX()][p.getY()] == null) {
                this.index[p.getX()][p.getY()] = new Square();
            }

            this.index[p.getX()][p.getY()].setPlate(e);
        }
    }

    public Entity getEntity(int x, int y) {

        Square test = this.getSquare(x, y);

        if (test != null) {
            return test.getEntity();
        } else {
            return null;
        }
    }

    public Square getSquare(int x, int y) {

        if (x > this.height - 1 || x < 0 || y > this.width - 1 || y < 0) {
            return null;
        }

        Square test = index[x][y];

        if (test != null) {
            return test;
        } else {
            return null;
        }
    }

    public void setSquare(int x, int y, Square s) {
        index[x][y] = s;
    }

    public boolean positionIsVoid(Position p) {
        Square s = this.getSquare(p.getX(), p.getY());

        if (s != null) {
            return s.getEntity() == null;
        }

        return true;
    }
}