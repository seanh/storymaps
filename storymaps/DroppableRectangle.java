package storymaps;

import edu.umd.cs.piccolo.nodes.PPath;
import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;

/**
 * A simple class that implements DroppableOwner, used for testing stuff.
 * 
 * @author seanh
 */
class DroppableRectangle implements DroppableOwner {

    // Lazy, public fields.
    public PPath rect;
    public Droppable d;
    
    public DroppableRectangle(int height, int width) {
        rect = PPath.createRoundRectangle(0, 0, height, width, 50, 50);
        try {
            // Make the ellipse droppable.
            d = new Droppable(rect,this);
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }               
    }
        
    /**
     * A node has been dropped onto the rectangle, accept it.
     * 
     * @param de The DropEvent object.
     * @return true, always accept drops.
     */
    public boolean dropped_onto(DropEvent de) {                        
        
        Droppable d = de.getDroppee();        
        // Accept the drop, observers will be notified.
        return true;        
    }
    
    public PNode getNode() {
        return rect;
    }
}