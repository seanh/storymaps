package storymaps;

import DragAndDrop.*;
import java.util.ArrayList;

/**
 * The collection of story cards that the user can drag from.
 * 
 * A StoryCards initially fills its grid node with 31 DisabledStoryCard
 * objects, one for each Propp function.
 * 
 * It then puts 31 more StoryCard objects on its overlay node, again one for
 * each function, placing each one over the corresponding DisabledStoryCard on
 * the grid node.
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
class StoryCards extends StoryBase implements DragDropObserver {

    private ArrayList<DisabledStoryCard> disabled_storycards =
            new ArrayList<DisabledStoryCard>();    
                        
    /**
     * Construct a new StoryCards instance and add to it a DisabledStoryCard and
     * a StoryCard for each Propp function in Function.functions.
     */
    public StoryCards(String title_text) {
        super(title_text);
        
        // For each Propp function, add a DisabledStoryCard to the grid node.
        // Keep references to all these DisabledStoryCards in
        // disabled_storycards.
        for (Function f : Function.functions) {
            DisabledStoryCard d = new DisabledStoryCard(f);
            addToGrid(d.getNode());
            disabled_storycards.add(d);
        }
        
        // Add a duplicate StoryCard on top of each DisabledStoryCard.
        for (DisabledStoryCard d : disabled_storycards) {            
            StoryCard s = new StoryCard(d.getFunction());
            addStoryCard(s);
        }
    }
    
    /**
     * Construct a new StoryCards instance using a given list of
     * DisabledStoryCard objects.
     */
    public StoryCards(String title_text, ArrayList<DisabledStoryCard> disabled_storycards) {
        super(title_text);
        for (DisabledStoryCard dsc : disabled_storycards) {
            addToGrid(dsc.getNode());
            this.disabled_storycards.add(dsc);
            StoryCard sc = dsc.getStoryCard();
            if (sc != null) {
                addStoryCard(sc);
            }
        }        
    }

    /**
     * Return a DisabledStoryCard from disabled_storycards that has the same
     * function as s, or null if no such story card exists in
     * disabled_storycards.
     */
    private DisabledStoryCard findDisabledStoryCard(StoryCard s) {
        for (DisabledStoryCard d : disabled_storycards) {
            if (d.getFunction().compare(s.getFunction())) {
                return d;
            }
        }
        return null;
    }

    /**
     * Return a StoryCard from this story map that has the same function as s,
     * or null if no such story card exists in this story map.
     */
    private StoryCard findStoryCard(StoryCard s) {
        for (StoryCard c : getStoryCards()) {
            if (c.getFunction().compare(s.getFunction())) {
                return c;
            }
        }
        return null;
    }    
    
    /**
     * Return a list of all the StoryCards in this story map.
     */
    public ArrayList<StoryCard> getStoryCards() {
        ArrayList<StoryCard> list = new ArrayList<StoryCard>();
        for (DisabledStoryCard d: disabled_storycards) {
            if (d.taken()) {
                list.add(d.getStoryCard());
            }
        }
        return list;
    }

    public ArrayList<DisabledStoryCard> getDisabledStoryCards() {
        return disabled_storycards;
    }
    
    private void addStoryCard(StoryCard s) {
        // Subscribe to the Draggable of this story card.
        s.attach(this);
        // Position the story card over its disabled counterpart.        
        s.unhighlight();
        addToOverlay(s.getNode());
        DisabledStoryCard d = findDisabledStoryCard(s);        
        s.getNode().setOffset(d.getNode().getOffset());
        d.setStoryCard(s);        
    }
    
    /**
     * Called when a node is dropped onto this story map. Accept the node only
     * if:
     * 
     * +   It has a StoryCard attribute attached to it.
     * +   This story map does not already contain a story card with the same
     *     function.
     * +   And disabled_storycards _does_ already contain a story card with the
     *     same function.
     * 
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
        
        if (findDisabledStoryCard(s) == null) {
            // We don't want a story card like this one, reject it.
            return false;
        }
        
        // Accept the new story card...
        addStoryCard(s);
        return true;
    }
    
    /**
     * Called when a draggable that this story map is subscribed to is dropped
     * onto something. Get the StoryCard that the Draggable instance belongs to
     * and remove it from storycards, then return false to unsubscribe from the
     * Draggable instance.
     */
    public boolean notify(DropEvent de) {
        Draggable draggee = de.getDraggee();
        Droppable droppee = de.getDroppee();        
            
        if (droppee == this.background.getAttribute("Droppable")) {            
            return true;
        }        
        
        StoryCard s = (StoryCard) draggee.getNode().getAttribute("StoryCard");
        DisabledStoryCard d = (DisabledStoryCard) s.getNode().getAttribute("DisabledStoryCard");
        d.clearStoryCard();
        return false;
    }    
}