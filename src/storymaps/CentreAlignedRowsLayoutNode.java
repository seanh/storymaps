
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
package storymaps;

import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.PNode;

import java.util.List;
import java.util.ArrayList;

/**
 * A layout node that lays its children out in multiple centre-aligned rows. The
 * maximum width of a row is defined by the width of the layout node's own
 * bounds. Since these bounds are initially 0 you will get only one child in
 * every row, so you probably want to call setBounds() after constructing the
 * layout node.
 *
 * @author seanh
 */
public class CentreAlignedRowsLayoutNode extends PNode {

    /**
     * The size of the gap between each node.
     */
    private double margin;

    private int prev_num_children = 0;

    public CentreAlignedRowsLayoutNode(double margin) {
        this.margin = margin;
    }

    public double getMargin() {
        return margin;
    }

    /**
     * Return the length of a given row (list of PNodes).
     *
     * The length is computed by summing the widths of all the PNodes and adding
     * this.margin in-between each pair of PNodes.
     */
    private double widthOfRow(List<PNode> row) {
        double width = 2 * margin;
        for (PNode p : row) {
            width += p.getFullBounds().getWidth() + margin;
        }
        if (row.size() >= 1) {
            // Margin will have been added one time too many.
            width = width - margin;
        }
        return width;
    }

    /**
     * Position `node` so that the top-left corner of its full bounds is at
     * (xoffset,yoffset).
     *
     */
    private void offsetNode(PNode node, double xoffset, double yoffset) {
        // Find the (left,top) position of the node's full bounds.
        PBounds b = node.getFullBounds();
        double left = b.getCenterX() - (b.getWidth() * 0.5);
        double top = b.getCenterY() - (b.getHeight() * 0.5);
        // How far from left to the desired xoffset?
        double x = xoffset - left;
        // How far from top to the desired yoffset?
        double y = yoffset - top;
        // Move the node by the necessary amount.
        node.translate(x,y);
    }

     /**
     * Return a list of all the child nodes of this node.
     */
    public List<PNode> getChildren() {
        List<PNode> children = new ArrayList<PNode>();
        for (int i=0; i<getChildrenCount(); i++) {
            children.add(getChild(i));
        }
        return children;
    }

    @Override
    protected void layoutChildren() {
        // We only recompute the layout if a child node has been added or
        // removed (and not if one has just moved or changed size).
        if (getChildrenCount() == prev_num_children) {
            return;
        }
        else {
            prev_num_children = getChildrenCount();
        }
        // layoutGrid is called to position all the child nodes of the
        // background node within the bounds of the background node.

        // Sort all the children of this container into rows that fit within the
        // width of the container.
        ArrayList<ArrayList<PNode>> rows = new ArrayList<ArrayList<PNode>>();
        ArrayList<PNode> firstRow = new ArrayList<PNode>();
        rows.add(firstRow);
        double maxWidthOfRow = getBounds().getWidth();
        for (PNode child : getChildren()) {
            ArrayList<PNode> currentRow = rows.get(rows.size()-1);
            if (widthOfRow(currentRow) + margin + child.getFullBounds().getWidth() >= maxWidthOfRow) {
                currentRow = new ArrayList<PNode>();
                rows.add(currentRow);
            }
            currentRow.add(child);
        }

        // Set the offsets of the children of this node, row by row, child by
        // child.
        double yoffset = margin;
        for (ArrayList<PNode> row : rows) {
            double width = widthOfRow(row);

            // Set xoffset of children in this row. While we do it figure out
            // the height of the tallest child so we can increment yoffset for
            // next time.
            double middle = getBounds().getX() + 0.5*getBounds().getWidth();
            double xoffset = middle - 0.5 * width;
            double max_height = 0;
            for (PNode child : row) {
                offsetNode(child, xoffset, yoffset);
                xoffset += child.getFullBounds().getWidth() + margin;
                if (child.getFullBounds().getHeight() > max_height) {
                    max_height = child.getFullBounds().getHeight();
                }
            }
            yoffset += max_height + margin;
        }
        // At this point yoffset is equal to the total height of the container's
        // contents.
    }
}