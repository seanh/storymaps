package storymaps;

import edu.umd.cs.piccolo.PNode;

/**
 * A layout node that lays its children out in a grid.
 * 
 * @author seanh
 */
public class GridLayoutNode extends PNode {

    /**
     * The number of columns in the grid layout
     */
    private int columns;
    
    /**
     * The size of the gap between each node.         
     */
    private int margin;
    
    private int prev_num_children = 0;
    
    public GridLayoutNode(int columns, int margin) {
        this.columns = columns;
        this.margin = margin;        
    }
    
    public int getColumns() {
        return columns;        
    }
    
    public int getMargin() {
        return margin;
    }
        
    @Override
    /**
     * Reposition every child node of this node, arranging them into a grid
     * formation of width this.columns, with gaps of size this.margin between
     * the nodes.
     */
    public void layoutChildren() {
        // We only recompute the layout if a child node has been added or
        // removed (and not if one has just moved or changed size).
        if (getChildrenCount() == prev_num_children) {
            return;
        }
        else {
            prev_num_children = getChildrenCount();
        }
        
        double xoffset = 0;
        double yoffset = 0;
        for (int i = 0; i < getChildrenCount(); i++) {
            if (i > 0) {
                // If this is the first node in a row other than the first row.
                if (i % this.columns == 0) {
                    // Increment yoffset by the height of the tallest node in the
                    // previous row plus the margin.
                    double maxheight = 0;
                    for (int j = i-this.columns; j<i; j++) {
                        double height = getChild(j).getHeight();
                        if (height > maxheight) {
                            maxheight = height;
                        }
                    }
                    yoffset += maxheight + margin;
                    // Reset xoffset to 0.
                    xoffset = 0;                
                } else {
                    // Increment xoffset by the width of the previous node plus the
                    // margin
                    double width = getChild(i-1).getWidth();
                    xoffset += width + margin;
                }
            }
            // Position the node so that its top-left corner is at
            // (xoffset,yoffset)
            PNode child = getChild(i);
            double top = child.getY();
            double left = child.getX();
            child.setOffset(xoffset-left,yoffset-top);
        }                            
    }    
}