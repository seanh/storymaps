package storymaps;

import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import edu.umd.cs.piccolo.util.PBounds;

/**
 * Base class for StoryMap and StoryCards.
 * 
 * @author seanh
 */
public class StoryBase implements DroppableOwner {

    /**
     * The big rounded rectangle that acts as the colorful background and
     * droppable area of the story base.
     */
    protected PPath background;
        
    protected Droppable droppable;

    private final Color color;

    private List<PNode> grid = new ArrayList<PNode>();

    private double margin;

    public StoryBase(double width, double height, double xoffset,
            double yoffset, Color color, double margin) {

        this.color = color;
        this.margin = margin;

        background = PPath.createRectangle(0, 0, (float)width, (float)height);
        background.setWidth(width);
        background.setHeight(height);
        background.setOffset(xoffset,yoffset);
        background.setPaint(color);
        background.setStrokePaint(color);

        try {
            droppable = new Droppable(background,this);            
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }                       
    }
    
    /**
     * A node has been dragged and dropped onto this story base. Just refuse
     * the drop. Subclasses should override this method.
     */
    public boolean dropped_onto(DropEvent de) {
        return false;
    }
    
    /**
     * Return the root node of this story base.
     */
    public PNode getNode() {
        return background;
    }

    public Color getColor() {
        return new Color(this.color.getRed(),this.color.getGreen(),
                this.color.getBlue());
    }

    /**
     * Add a node to the grid.
     */
    public void addToGrid(PNode node) {
        background.addChild(node);
        grid.add(node);
        layoutGrid();
    }
    
    /**
     * Add a node to the overlay.
     */
    public void addToOverlay(PNode node) {
        background.addChild(node);
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

    // FIXME: to make it more reusable this method (and its helper methods)
    // should be part of a layout node class that wraps a PNode and lays out its
    // child nodes within its bounds.
    private void layoutGrid() {
        // layoutGrid is called to position all the child nodes of the
        // background node within the bounds of the background node.

        // Sort all the children of this container into rows that fit within the
        // width of the container.
        ArrayList<ArrayList<PNode>> rows = new ArrayList<ArrayList<PNode>>();
        ArrayList<PNode> firstRow = new ArrayList<PNode>();
        rows.add(firstRow);
        double maxWidthOfRow = background.getWidth();
        for (PNode child : grid) {
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
            double middle = background.getX() + 0.5*background.getWidth();
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
}