
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

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
/*
 * A visible rectangle that can contain a number of story cards. Story cards
 * can be dragged from and dropped onto the container. The container controls
 * the layout of its story cards.
 */

package scratch;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.PNode;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


/**
 * A visible, resizable rectangle that contains a fixed number of
 * placeholders that are layed out within the rectangle. The layout is
 * re-computed each time the rectangle is resized. The rectangle acts as a
 * droppable object, story cards may be dropped onto the rectangle and snap to
 * the placeholders. They may be dragged off the rectangle also.
 *
 * @author seanh
 */
public class StoryCardsContainer extends PNodeWrapper {

    private List<Placeholder> placeholders = new ArrayList<Placeholder>();
    double margin;

    /**
     * Initialise the StoryCardsContainer, setting an initial height, position,
     * colour, and number of placeholders it should contain.
     *
     * @param width
     * @param height
     * @param xoffset
     * @param yoffset
     * @param color
     * @param num_placeholders
     * @param margin
     */
    public StoryCardsContainer(float width, float height, float xoffset,
            float yoffset, Color color, int num_placeholders, double margin) {

        super(makeRectangle(width,height,xoffset,yoffset,color));
        this.margin = margin;

        for (int i=0; i<num_placeholders; i++) {
            Placeholder p = new Placeholder();
            placeholders.add(p);
            addChild(p.getNode());
        }

        layoutPlaceholders();
    }

    /**
     * Make the rectangular object used as the container's background.
     *
     */
    private static PPath makeRectangle(float width, float height, float xoffset,
            float yoffset, Color color) {
        PPath rectangle = PPath.createRectangle(0, 0, width, height);
        rectangle.setOffset(xoffset,yoffset);
        rectangle.setPaint(color);
        rectangle.setStrokePaint(color);
        return rectangle;
    }
    
    /**
     * Return the length of a given row (list of PNodes).
     * 
     * The length is computed by summing the widths of all the PNodes and adding
     * this.margin in-between each pair of PNodes.
     */
    private double widthOfRow(List<PNode> row) {
        double width = 0;
        for (PNode p : row) {
            width += p.getWidth() + margin;
        }
        if (row.size() >= 1) {
            // Margin will have been added one time too many.
            width = width - margin;
        }
        return width;
    }

    private void layoutPlaceholders() {
        // FIXME: this uses the widths of child nodes in their own coordinate
        // spaces, when it should be translating them into this node's
        // coordinate space. It only works if the child nodes have scale 1.

        // Sort all the children of this container into rows that fit within the
        // width of the container.
        ArrayList<ArrayList<PNode>> rows = new ArrayList<ArrayList<PNode>>();
        ArrayList<PNode> firstRow = new ArrayList<PNode>();
        rows.add(firstRow);
        double maxWidthOfRow = getWidth() - margin - margin;
        for (PNode child : getChildren()) {
            ArrayList<PNode> currentRow = rows.get(rows.size()-1);
            if (widthOfRow(currentRow) + margin + child.getWidth() >= maxWidthOfRow) {
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
            double middle = getX() + 0.5*getWidth();
            double xoffset = middle - 0.5 * width;
            double max_height = 0;
            System.out.println("middle: "+middle);
            System.out.println("widthOfRow: "+width);
            System.out.println("xoffset: "+xoffset);
            for (PNode child : row) {
                offsetNode(child, xoffset, yoffset);
                xoffset += child.getWidth() + margin;
                if (child.getHeight() > max_height) {
                    max_height = child.getHeight();
                }
            }
            yoffset += max_height + margin;
        }
        // At this point yoffset is equal to the total height of the container's
        // contents, the minimum height of the container.
        setHeight(yoffset);
    }

    /**
     * Position `node` so that its top-left corner is at (xoffset,yoffset).
     */
    private void offsetNode(PNode node, double xoffset, double yoffset) {
        double top = node.getY();
        double left = node.getX();
        node.setOffset(xoffset-left,yoffset-top);
    }

    // Make sure we call layoutPlaceholders whenever width is changed.
    @Override
    public boolean setWidth(double width) {
        boolean result = super.setWidth(width);
        computeFullBounds();
        layoutPlaceholders();
        return result;
    }
}