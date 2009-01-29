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
            if (i == 0) {
                // If this is the first node in the first row then set the
                // yoffset to be half the height of the node. This is a hack to
                // deal with the fact that the (0,0) in a storycard's space is
                // the center of the story card, not the top-left.
                yoffset = child.getHeight()/2.0;
            } else if (i > 0 && i % this.columns == 0) {
                // If this is the first node of a later row then use its height
                // to determine the offset of the next row.
                // (This assumes all nodes in the grid have the same height.)
                yoffset += child.getFullBoundsReference().getHeight() + margin;
                xoffset = 0;
            }
            child.setOffset(xoffset - child.getX(), yoffset);
            xoffset += child.getFullBoundsReference().getWidth() + margin;
        }                            
    }    
}