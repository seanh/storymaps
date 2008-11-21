package storymaps;

import DragAndDrop.*;
import java.util.HashSet;

/**
 * The collection of story cards that the user can drag from.
 * 
 * A StoryCards initially fills its grid node with 31 disabled StoryCard
 * objects, one for each Propp function.
 * 
 * It then puts 31 more StoryCard objects on its overlay node, again one for
 * each function, placing each one over the corresponding card on the grid node.
 * These overlay cards are not disabled.
 * 
 * The story cards on the overlay can be dragged out of the StoryCards, leaving
 * their disabled counterparts showing behind. The disabled story cards cannot
 * be dragged away.
 * 
 * If a story card has been dragged away from the StoryCards then it (or another
 * card with the same function) can be dropped back on the StoryCards, but no
 * new functions can be added by dropping, and only one card for each function
 * can be added.
 * 
 * If a StoryCard is dropped onto a StoryCards the drop will be accepted only if
 * a story card with the same function does _not_ already exist on the overlay
 * node, and a story card with the same function _does_ already exist on the
 * grid node. If accepted, the new story card will be positioned on the overlay
 * node over its corresponding disabled story card on the grid node.
 * 
 * @author seanh
 */

//FIXME: Need a different name for this class.
public class StoryCards extends StoryBase implements DragDropObserver {

    private HashSet<StoryCard> disabled_storycards = new HashSet<StoryCard>();    
    private HashSet<StoryCard> storycards = new HashSet<StoryCard>();
                        
    public StoryCards(String title_text) {
        super(title_text);
        
        // For each Propp function, add a StoryCard to the grid node, and
        // disable the StoryCard. Keep references to all these story cards in
        // disabled_storycards.        
        for (Function f : Functions.getFunctions()) {
            StoryCard disabled = new StoryCard(f.friendly_name,f.description);
            disabled.disable();
            addToGrid(disabled.getNode());
            disabled_storycards.add(disabled);
        }
        
        // For each StoryCard in disabled_storycard, add a duplicate StoryCard
        // over the original StoryCard, on the overlay node. Keep references to
        // all these story cards in storycards.
        for (StoryCard disabled : disabled_storycards) {            
            StoryCard s = disabled.copy();
            // FIXME: StoryCard should take care of wrapping and unwrapping itself.
            try {
                Draggable d = new Draggable(s.getNode());
                d.attach(this);
            } catch (NodeAlreadyDraggableException e) {
                // ...
            }              
            addToOverlay(s.getNode());
            s.getNode().setOffset(disabled.getNode().getOffset());            
            storycards.add(s);
        }                        
    }

    /**
     * Return a StoryCard from disabled_storycards that has the same function as
     * s, or null if no such story card exists.
     */
    private StoryCard findDisabled(StoryCard s) {
        for (StoryCard c : disabled_storycards) {
            if (s.getTitle().equals(c.getTitle())) {
                return c;
            }
        }
        return null;
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
     * +   And disabled_storycards _does_ already contain a story card with the
     *     same function.
     * 
     * If the story card is accepted then add it to storycards, subscribe to its
     * draggable instance, position the story card on top of its disabled
     * counterpart, and return true to indicate the drop was accepted.
     */
    @Override
    public boolean dropped_onto(DropEvent de) {
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
        
        if (findDisabled(s) == null) {
            // We don't want a story card like this one, reject it.
            return false;
        }
        
        // Accept the new story card...
        storycards.add(s);
        // Subscribe to the Draggable of this story card.
        Draggable d = (Draggable) s.getNode().getAttribute("Draggable");            
        d.attach(this);
        // Position the story card over its disabled counterpart.
        StoryCard disabled = findDisabled(s);        
        s.unhighlight();
        addToOverlay(s.getNode());
        s.getNode().setOffset(disabled.getNode().getOffset());
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