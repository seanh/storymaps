package storymaps;

import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import storymaps.ui.Button;

class StoryMap extends StoryBase implements DragDropObserver, Receiver,
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
        init();
    }
    
    public StoryMap(String title_text, StoryEditor editor, List<Placeholder> placeholders) {
        super(title_text);
        this.editor = editor;
        for (Placeholder p : placeholders) {
            addToGrid(p.getNode());
            this.placeholders.add(p);
            StoryCard sc = p.getStoryCard();
            if (sc != null) {
                addStoryCard(sc,p);
            }
        }
        init();
    }
    
    /**
     * An init method that is called by all constructor methods and does some
     * common initialisation stuff.
     */
    private void init() {
        Messager.getMessager().accept("StoryCard single-clicked", this, null);
        PImage image;
        try {
            image = new PImage(Util.readImageFromFile("/storymaps/icons/sort.png"));
        } catch (IOException e) {
            // FIXME: shouldn't need to crash here, create a text-only button
            // instead.
            throw new RuntimeException("Could not load sort.png icon.",e);
        }
        Button sort = new Button("Sort","Sort");
        sort.setIcon(image);
        sort.setScale(5);
        this.background.addChild(sort);
        sort.setOffset(background.getWidth(),background.getHeight());
        Messager.getMessager().accept("button clicked", this, null);                
    }
    
    public StoryEditor getEditor() {
        return editor;
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
        for (Placeholder p : free_placeholders) {
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
        s.unhighlight(); // Scale the StoryCard down, to position it properly.
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
        removeStoryCard(s);
        return false;
    }
    
    private void removeStoryCard(StoryCard s) {
        Placeholder p = (Placeholder) s.getNode().getAttribute("Placeholder");        
        p.clearStoryCard();
        editor.update(getStoryCards());        
    }
        
    /**
     * Return a list of all the StoryCards in this StoryMap.
     */
    public ArrayList<StoryCard> getStoryCards() {
        ArrayList<StoryCard> storycards = new ArrayList<StoryCard>();
        for (Placeholder p : placeholders) {
            if (p.taken()) {
                storycards.add(p.getStoryCard());
            }
        }
        return storycards;
    }
    
    public ArrayList<Placeholder> getPlaceholders() {
        return placeholders;
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
        } else if (name.equals("button clicked")) {
            if (((String)sender_arg).equals("Sort")) {
                sort();
            }
        }
    }

    /**
     * Sort the story cards in this story map.
     */
    private void sort() {
        // Get a list of all story cards currently in this story map.
        ArrayList<StoryCard> storycards = getStoryCards();
        
        // Remove all story cards from the story map.
        for (StoryCard c : storycards) {
            removeStoryCard(c);            
        }
        
        // Sort the story cards.
        Collections.sort(storycards);
                        
        // Add all story cards back to the story map in sorted order.
        for (int i = 0; i < storycards.size(); i++) {
            addStoryCard(storycards.get(i),placeholders.get(i));     
        }
                
        editor.update(getStoryCards());
    }
    
    // Implement Originator
    // --------------------
    
    private static final class StoryMapMemento implements Memento {
        // Don't need to defensively copy title as strings are immutable.
        private final String title;
        // Placeholder mementos are immutable but lists aren't, so this field
        // needs to be defensively copied to keep this class immutable.
        private final List<Memento> placeholderMementos = new ArrayList<Memento>();
        StoryMapMemento (StoryMap m) {
            this.title = m.getEditor().getTitle();
            for (Placeholder p : m.getPlaceholders()) {
                Memento pm = p.createMemento();
                placeholderMementos.add(pm);
            }
        }
        String getTitle() { return title; }
        List<Memento> getPlaceholderMementos() {
            // Make a defensive copy and return it.
            List<Memento> copy = new ArrayList<Memento>();
            for (Memento p : placeholderMementos) {
                // We just add the same memento instances to the copy list,
                // there would be no point in copying the mementos as they are
                // immutable, it's the list object itself we're defensively
                // copying.
                copy.add(p);
            }
            return copy;
        }
    }
    
    public Memento createMemento() {
        return new StoryMapMemento(this);
    }
    
    public static StoryMap newInstanceFromMemento(Memento m)
            throws MementoException {
        if (m == null) {
            String detail = "Null memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        if (!(m instanceof StoryMapMemento)) {
            String detail = "Wrong type of memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        StoryMapMemento smm = (StoryMapMemento) m;
        StoryEditor editor = Application.getInstance().getStoryEditor();
        String title = smm.getTitle();
        List<Memento> placeholderMementos = smm.getPlaceholderMementos();
        List<Placeholder> placeholders = new ArrayList<Placeholder>();
        for (Memento pm : placeholderMementos) {
            placeholders.add(Placeholder.newInstanceFromMemento(pm));
        }
        StoryMap storyMap = new StoryMap(title,editor,placeholders);
        editor.update(storyMap.getStoryCards());
        editor.setTitle(storyMap.getTitle());
        return storyMap;
    }
}