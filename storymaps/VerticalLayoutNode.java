package storymaps;

import edu.umd.cs.piccolo.PNode;
import java.util.Iterator;

/**
 * A layout node that lays its children out in a vertical line.
 * 
 * @author seanh
 */
public class VerticalLayoutNode extends PNode {
    
    /**
     * The size of the gap between each node.         
     */
    private int margin;
    
    private int prev_num_children = 0;
    
    public VerticalLayoutNode(int margin) {
        this.margin = margin;        
    }
    
    
    public int getMargin() {
        return margin;
    }
    
    @Override
    /**
     * Reposition every child node of this node, arranging them into a
     * vertical line with gaps of size this.margin between the nodes.
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
        
        double xOffset = 0;
        double yOffset = 0;
                        
        Iterator i = getChildrenIterator();
        while (i.hasNext()) {
            PNode child = (PNode) i.next();
            child.setOffset(xOffset - child.getX(), yOffset);
            yOffset += child.getFullBoundsReference().getHeight() + margin;
        }                            
    }    
}