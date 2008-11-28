package storymaps;

import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

public class StoryMap extends StoryBase implements DragDropObserver, Receiver,
        Originator {
        
    private ArrayList<Placeholder> placeholders = 
            new ArrayList<Placeholder>();    
    
    private StoryEditor editor;
    
    public StoryMap(String title_text, StoryEditor editor) {
        super(title_text);
        this.editor = editor;
        
        // For each Propp function add a PlaceHolder to the grid node. Keep
        // references to all these placeholders in placeholders.
        for (int i=0; i<33; i++) {
            Placeholder p = new Placeholder();
            addToGrid(p.getNode());
            placeholders.add(p);
        }
        
        Messager.getMessager().accept("StoryCard single-clicked", this, null);
    }

    /**
     * Return the distance between two points.
     */
    private double findDistance(double x1, double y1, double x2, double y2) {
        // Go Pythagoras!
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
     * Return the nearest free PlaceHolder in placeholders to the given
     * StoryCard, or null if there are no free placeholders.
     */
    private Placeholder findNearest(StoryCard s) {
        ArrayList<Placeholder> free_placeholders = new ArrayList<Placeholder>();
        for (Placeholder p: placeholders) {
            if (!p.taken()) {
                free_placeholders.add(p);
            }
        }
        if (free_placeholders.size() == 0) {
            // There's no free placeholders.
            return null;
        }
        
        // Translate the storycard's x and y offsets to global coords.        
        Point2D p2d = globalPos(s.getNode());
        double x1 = p2d.getX();
        double y1 = p2d.getY();
        
        double nearest_distance = -1;
        Placeholder nearest_placeholder = null;        
        for (Placeholder p : placeholders) {
            p2d = globalPos(p.getNode());
            double x2 = p2d.getX();
            double y2 = p2d.getY();            
            double distance = findDistance(x1,y1,x2,y2);
            if (nearest_distance == -1 || distance < nearest_distance) {
                nearest_distance = distance;
                nearest_placeholder = p;
            }
        }
        
        assert nearest_placeholder != null : "findNearest did not find a placeholder, even though there are free placeholders? This shouldn't happen.";
        
        return nearest_placeholder;
    }
    
    /**
     * Return a StoryCard from storycards that has the same function as s, or
     * null if no such story card exists in storycards.
     */
    private StoryCard findStoryCard(StoryCard s) {
        for (StoryCard c : getStoryCards()) {
            if (s.getFunction().compare(c.getFunction())) {
                return c;
            }
        }
        return null;
    }

    /** 
     * Return a StoryCard from storycard that is the same object instance as
     * StoryCard s, or null if no such StoryCard exists in storycards.
     */
    private StoryCard findStoryCardInstance(StoryCard s) {
        for (StoryCard c : getStoryCards()) {
            if (s == c) { return c; }
        }
        return null;
    }
    
    /**
     * Reposition StoryCard s in the scene graph, placing it over the nearest
     * Placeholder in placeholders.
     */
    private void positionStoryCard(StoryCard s) {
        // Detach s from any existing placeholder, if it's attached to one.
        Placeholder previous = (Placeholder)
                s.getNode().getAttribute("Placeholder");
        if (previous != null) {
            previous.clearStoryCard();
        }
        Placeholder nearest = findNearest(s);        
        s.unhighlight(); // Scale the SotyrCard down, to position it properly.
        addToOverlay(s.getNode());
        s.getNode().setOffset(nearest.getNode().getOffset());
        nearest.setStoryCard(s);                
    }
    
    /**
     * Add a new StoryCard to this StoryMap, positioning it over the nearest
     * free placeholder.
     */
    private void addStoryCard(StoryCard s) {
        s.attach(this);
        positionStoryCard(s);
    }
    
    /**
     * Add a new StoryCard to this StoryMap, positioning it over placeholder p.
     */
    private void addStoryCard(StoryCard s, Placeholder p) {
        s.attach(this);
        Placeholder previous = (Placeholder)
                s.getNode().getAttribute("Placeholder");
        if (previous != null) {
            previous.clearStoryCard();
        }
        s.unhighlight(); // Scale the SotyrCard down, to position it properly.
        addToOverlay(s.getNode());
        s.getNode().setOffset(p.getNode().getOffset());
        p.setStoryCard(s);                    
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

        if (placeholders.size() <= getStoryCards().size()) {
            // We have no space, reject the drop.
            return false;
        }
        
        StoryCard s = (StoryCard)
                      de.getDraggee().getNode().getAttribute("StoryCard");
                        
        if (s == null) { 
            // This object is not a story card, reject it.
            return false;
        }
        
        if (findStoryCardInstance(s) != null) {
            // This is one of our own story cards, just reposition it.
            positionStoryCard(s);
            editor.update(getStoryCards());
            return true;
        }
                                    
        if (findStoryCard(s) != null) { 
            // We already have a story card like this one, reject it.
            return false;
        }
                
        // Accept the new story card...
        // Subscribe to the Draggable of this story card.
        // FIXME: this method gets called when a storycard is dropped onto this
        // story map, then by the time this method has finished executing this
        // story map has subscribed to the card's draggable also, and the notify
        // method below gets called for the same drop event!
        addStoryCard(s);
        editor.update(getStoryCards());
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
        
        // If the droppee is that of this StoryMap's background node, then a
        // story card was dragged from this story map and dropped onto this
        // story map again, so ignore the event.
        if (droppee == this.background.getAttribute("Droppable")) {            
            return true;
        }
        // Otherwise, a story card was dragged from this story map and dropped
        // onto something else, so remove the story card from this story map. 
        StoryCard s = (StoryCard) draggee.getNode().getAttribute("StoryCard");
        Placeholder p = (Placeholder) s.getNode().getAttribute("Placeholder");        
        p.clearStoryCard();
        editor.update(getStoryCards());
        return false;
    }
        
    /**
     * Return a list of all the StoryCards in this StoryMap.
     */
    public ArrayList<StoryCard> getStoryCards() {
        ArrayList<StoryCard> storycards = new ArrayList<StoryCard>();
        for (Placeholder p: placeholders) {
            if (p.taken()) {
                storycards.add(p.getStoryCard());
            }
        }
        return storycards;
    }
    
    /**
     * Called when a StoryCard in this StoryMap is single-clicked, focus the
     * StoryCard in the StoryMap and the StoryEditor.
     */
    private void focus(StoryCard s) {
        editor.focus(s);
    }
    
    /**
     * Receive messages (from Messager) that we have subscribed to using
     * Messager.accept or Messager.acceptOnce.
     */
    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("StoryCard single-clicked")) {            
            StoryCard s = (StoryCard) sender_arg;
            if (getStoryCards().contains(s)) {
                focus(s);
            }
        }
    }    

    private static class Memento implements Serializable {
        public ArrayList<Object> placeholder_mementos;
        public Memento(ArrayList<Object> placeholder_mementos) {
            this.placeholder_mementos = placeholder_mementos;
        }
    }    
    
    /** Return a memento object for the current state of this StoryMap. */
    public Object saveToMemento() {
        // We just save a list of placeholder mementos for each placeholder in
        // the story map.
        ArrayList<Object> placeholder_mementos = new ArrayList<Object>();
        for (Placeholder p : placeholders) {
            placeholder_mementos.add(p.saveToMemento());
        }
        return new Memento(placeholder_mementos);
    }                          

    /** 
     * Restore the state of this StoryMap from a memento object. 
     * 
     * @throws IllegalArgumentException if the argument cannot be cast to the
     * private StoryMap.Memento type (i.e. the argument is not an object
     * returned by the saveToMemento method of this class).
     */
    public void restoreFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException();
        } else {            
            Memento m = (Memento) o;
            // First remove all existing placeholders from the scene graph.
            for (Placeholder p : placeholders) {
                StoryCard s = p.getStoryCard();
                if (s != null) {
                    Draggable d = s.getDraggable();
                    d.detach(this);
                    s.getNode().removeFromParent();
                    s.getNode().addAttribute("Placeholder",null);
                }
                p.getNode().removeFromParent();
            }
            // Now replace the list of placeholders.
            placeholders = new ArrayList<Placeholder>();            
            for (Object pm : m.placeholder_mementos) {
                placeholders.add(Placeholder.newFromMemento(pm));
            }
            // Add each new placeholder to the grid, in order.
            for (Placeholder p: placeholders) {
                addToGrid(p.getNode());
            }
            // For each placeholder, if it has a StoryCard, add the story card
            // to the overlay.
            for (Placeholder p: placeholders) {
                StoryCard s = p.getStoryCard();
                if (s != null) {
                    addStoryCard(s,p);
                }
            }
            // Update the StoryEditor.
            editor.update(getStoryCards());
        }
    }
}