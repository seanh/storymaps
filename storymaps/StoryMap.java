package storymaps;

import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class StoryMap  extends StoryBase implements DragDropObserver {

    private HashSet<StoryCard> placeholders = new HashSet<StoryCard>();    
    private HashSet<StoryCard> storycards = new HashSet<StoryCard>();
                        
    public StoryMap(String title_text) {
        super(title_text);
        
        // For each Propp function add a PlaceHolder to the grid node. Keep
        // references to all these placeholders in placeholders.
        // TODO: implement PlaceHolder class and use it here instead of disabled
        // story cards.
        for (int i=0; i<31; i++) {
            StoryCard disabled = new StoryCard(""+i,""+i);
            disabled.disable();
            addToGrid(disabled.getNode());
            placeholders.add(disabled);
        }        
    }

    /**
     * Return the distance between two points.
     */
    private double findDistance(double x1, double y1, double x2, double y2) {
        double a = (x2-x1)*(x2-x1);
        double b = (y2-y1)*(y2-y1);
        return Math.sqrt(a+b);
    }
    
    /**
     * Return the offset of any pnode in global coords.
     */
    private Point2D globalPos(PNode n) {
        return n.localToGlobal(n.parentToLocal(n.getOffset()));
    }
    
    /**
     * Return the nearest PlaceHolder in placeholders to the given StoryCard.
     */
    private StoryCard findNearest(StoryCard s) {
        // First translate the storycard's x and y offsets to global coords.        
        Point2D p2d = globalPos(s.getNode());
        double x1 = p2d.getX();
        double y1 = p2d.getY();
        double nearest_distance = -1;
        StoryCard nearest_placeholder = null;        
        for (StoryCard p : placeholders) {
            boolean taken = false;
            for (StoryCard c : storycards) {
                if (c.getNode().getOffset().equals(p.getNode().getOffset())) {
                    // This placeholder already has a node.
                    taken = true;
                    break;
                }
            }
            if (taken) { continue; }
            p2d = globalPos(p.getNode());
            double x2 = p2d.getX();
            double y2 = p2d.getY();            
            double distance = findDistance(x1,y1,x2,y2);
            if (nearest_distance == -1 || distance < nearest_distance) {
                nearest_distance = distance;
                nearest_placeholder = p;
            }
        }
        return nearest_placeholder;
    }
    
    /**
     * Return a StoryCard from storycards that has the same function as s, or
     * null if no such story card exists.
     */
    private StoryCard findStoryCard(StoryCard s) {
        for (StoryCard c : storycards) {
            if (s.getTitle().equals(c.getTitle())) {
                return c;
            }
        }
        return null;
    }    
    
    /**
     * Called when a node is dropped onto this story map. Accept the node only
     * if:
     * 
     * +   It has a StoryCard attribute attached to it.
     * +   Field storycards does not already contain a story card with the same
     *     function.
     * +   There's at least one empty space: we have more placeholders than
     *     story cards.
     * 
     * If the story card is accepted then add it to storycards, subscribe to its
     * draggable instance, position the story card on top of the nearest free
     * placeholder, and return true to indicate the drop was accepted.
     */
    @Override
    public boolean dropped_onto(DropEvent de) {

        if (placeholders.size() <= storycards.size()) {
            // We have no space, reject the drop.
            return false;
        }
        
        StoryCard s = (StoryCard)
                      de.getDraggee().getNode().getAttribute("StoryCard");
                        
        if (s == null) { 
            // This object is not a story card, reject it.
            return false;
        }
        
        if (findStoryCard(s) != null) { 
            // We already have a story card like this one, reject it.
            return false;
        }
                
        // Accept the new story card...
        storycards.add(s);
        // Subscribe to the Draggable of this story card.
        Draggable d = (Draggable) s.getNode().getAttribute("Draggable");            
        d.attach(this);
        // Position the story card over the nearest free placeholder.
        StoryCard nearest = findNearest(s);        
        s.unhighlight();
        addToOverlay(s.getNode());
        s.getNode().setOffset(nearest.getNode().getOffset());
        return true;
    }

    /**
     * Called when a draggable that this story map is subscribed to is dropped
     * onto something. Get the StoryCard that the Draggable instance belongs to
     * and remove it from storycards, then return false to unsubscribe from the
     * Draggable instance.
     */
    public boolean notify(DropEvent de) {
        /* Note: this method is tightly coupled to method dropped_onto above!
         * 
         * Here we don't check whether the droppee belonged to this StoryCards
         * object (i.e. a StoryCard was dragged from this StoryCards then
         * dropped back onto this StoryCards) because if that was the case then
         * dropped_onto above would not have accepted the drop and this method
         * would not be getting called.
         */
        Draggable draggee = de.getDraggee();
        Droppable droppee = de.getDroppee();
        StoryCard s = (StoryCard) draggee.getNode().getAttribute("StoryCard");
        storycards.remove(s);
        return false;
    }   
}