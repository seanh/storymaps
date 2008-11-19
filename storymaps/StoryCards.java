package storymaps;

import DragAndDrop.*;
import java.util.HashSet;

/**
 * The collection of story cards that the user can drag from.
 * 
 * @author seanh
 */
public class StoryCards extends StoryBase
                        implements DroppableOwner, DragDropObserver {

    private HashSet<StoryCard> storycards = new HashSet<StoryCard>();
    
    public StoryCards(String title_text) {
        super(title_text);

        for (int i=0; i<31; i++) {
            StoryCard s = new StoryCard(""+i,""+i);
            try {
                Draggable d = new Draggable(s.getNode());
                d.attach(this);
            } catch (NodeAlreadyDraggableException e) {
                // ...
            }            
            add(s.getNode());
            storycards.add(s);
        }
        
        try {
            Droppable d = new Droppable(background,this);            
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }
        
    }
    
    public boolean dropped_onto(DropEvent de) {
        StoryCard s = (StoryCard) de.getDraggee().getNode().getAttribute("StoryCard");
        if (s == null) { return false; }
        if (storycards.contains(s)) { return false; }
        // Add a new story card and attach to its observer...
        return true;
    }
    
    public boolean notify(DropEvent de) {
        return false;
    }   
}