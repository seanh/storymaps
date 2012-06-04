
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@lavabit.com>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
package scratch;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * A wrapper class for Piccolo's PNode class.
 *
 * FIXME: unfortunately PNode is designed for inheritance, it contains protected
 * methods intended to be overridden, e.g. layoutChildren, so it should be
 * extended not composed.
 *
 * @author seanh
 */
public class PNodeWrapper {

    private final PNode p;

    public PNodeWrapper() {
        this.p = new PNode();
    }

    public PNodeWrapper(PNode p) {
        this.p = p;
    }

    /**
     * Return the PNode that this PNodeWrapper is wrapping. Sometimes the PNode
     * type is needed.
     * 
     */
    public PNode getNode() {
        return p;
    }

    public void addChild(PNode child) {
        p.addChild(child);
    }

    public void setPaint(Color color) {
        p.setPaint(color);
    }

    public boolean setWidth(double width) {
        return p.setWidth(width);
    }

    public boolean setHeight(double height) {
        return p.setHeight(height);
    }

    public void setOffset(double x, double y) {
        p.setOffset(x,y);
    }

    public double getX() {
        return p.getX();
    }

    public double getY() {
        return p.getY();
    }

    public double getXOffset() {
        return p.getXOffset();
    }

    public double getYOffset() {
        return p.getYOffset();
    }

    public PBounds getBounds() {
        return p.getBounds();
    }

    public PBounds getGlobalBounds() {
        return p.getGlobalBounds();
    }

    public PBounds getFullBounds() {
        return p.getFullBounds();
    }

    public double getWidth() {
        return p.getWidth();
    }

    public double getHeight() {
        return p.getHeight();
    }

    /**
     * Return a list of all the child nodes of the wrapped node.
     */
    public List<PNode> getChildren() {
        List<PNode> children = new ArrayList<PNode>();
        for (int i=0; i<p.getChildrenCount(); i++) {
            children.add(p.getChild(i));
        }
        return children;
    }

    public void computeFullBounds() {
        p.computeFullBounds(new PBounds());
    }
}