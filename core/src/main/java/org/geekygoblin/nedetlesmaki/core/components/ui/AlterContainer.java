/*
 The MIT License (MIT)

 Copyright (c) 2013 Pierre Marijon <pierre@marijon.fr>

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
package org.geekygoblin.nedetlesmaki.core.components.ui;

import im.bci.jnuit.widgets.Widget;

import im.bci.jnuit.widgets.Container;

/**
 *
 * @author natir
 */
public class AlterContainer extends Container {

    @Override
    public Widget findClosestLeftFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {
            
            float closestLeftChildLengthSquared = Float.MAX_VALUE;
	    float closestOmega = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterX() < widget.getCenterX()) {
                    float lenghtSquared = distanceSquared(w, widget);
		    float omega = omegaHorizontal(w, widget);
                    if (null == closestLeftChild || (omega <= closestOmega && lenghtSquared < closestLeftChildLengthSquared)) {
                        closestLeftChildLengthSquared = lenghtSquared;
			closestOmega = omega;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }
    
    @Override
    public Widget findClosestRightFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {
            
            float closestLeftChildLengthSquared = Float.MAX_VALUE;
	    float closestOmega = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterX() > widget.getCenterX()) {
                    float lenghtSquared = distanceSquared(w, widget);
	            float omega = omegaHorizontal(w, widget);
                    if (null == closestLeftChild || (omega <= closestOmega && lenghtSquared < closestLeftChildLengthSquared)) {
                        closestLeftChildLengthSquared = lenghtSquared;
			closestOmega = omega;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }
    
    @Override
    public Widget findClosestUpFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {
            
            float closestLeftChildLengthSquared = Float.MAX_VALUE;
	    float closestOmega = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterY() < widget.getCenterY()) {
                    float lenghtSquared = distanceSquared(w, widget);
		    float omega = omegaVertical(w, widget);
                    if (null == closestLeftChild || (omega <= closestOmega && lenghtSquared < closestLeftChildLengthSquared)) {
                        closestLeftChildLengthSquared = lenghtSquared;
			closestOmega = omega;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }

    @Override    
    public Widget findClosestDownFocusableWidget(Widget widget) {
        Widget closestLeftChild = null;
        if (null != widget) {
            
            float closestLeftChildLengthSquared = Float.MAX_VALUE;
	    float closestOmega = Float.MAX_VALUE;
            for (Widget w : getChildren()) {
                if (w.isFocusable() && w.getCenterY() > widget.getCenterY()) {
                    float lenghtSquared = distanceSquared(w, widget);
		    float omega = omegaVertical(w, widget);
                    if (null == closestLeftChild || (omega <= closestOmega && lenghtSquared < closestLeftChildLengthSquared)) {
                        closestLeftChildLengthSquared = lenghtSquared;
			closestOmega = omega;
                        closestLeftChild = w;
                    }
                }
            }
        }
        return closestLeftChild;
    }
    
    private static float distanceSquared(Widget w1, Widget w2) {
        float dx = w1.getCenterX() - w2.getCenterX();
        float dy = w1.getCenterY() - w2.getCenterY();
        return dx * dx + dy * dy;
    }

    private static float omegaHorizontal(Widget w1, Widget w2) {
        float dx = (float)Math.abs(w1.getCenterX() - w2.getCenterX());
        float dy = (float)Math.abs(w1.getCenterY() - w2.getCenterY());
        return (float) Math.atan(dy/dx);
    }

    private static float omegaVertical(Widget w1, Widget w2) {
        float dx = (float)Math.abs(w1.getCenterX() - w2.getCenterX());
        float dy = (float)Math.abs(w1.getCenterY() - w2.getCenterY());
        return (float) Math.atan(dx/dy);
    }
}
