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
            PNode child = getChild(i);            
            if (i > 0 && i % this.columns == 0) {
                // FIXME: this uses the height of the last child in a row to
                // determine the offset of the next row. That's okay if all
                // children are the same height, but if they can vary then you
                // need to use the height of the tallest child in the previous
                // row.
                yoffset += child.getFullBoundsReference().getHeight() + margin;
                xoffset = 0;
            }
            child.setOffset(xoffset - child.getX(), yoffset);
            xoffset += child.getFullBoundsReference().getWidth() + margin;
        }                            
    }    
}