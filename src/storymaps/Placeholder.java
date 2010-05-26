package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
import java.util.logging.Logger;

class Placeholder extends StoryCardBase implements Originator {
            
    private PImage background;
    private boolean taken = false;
    private StoryCard storycard = null;
    
    public Placeholder() {
        super(Function.getFunctions().get(0));
        getNode().addAttribute("Placeholder",this);
        getNode().setVisible(false);
    }
                                     
    public boolean taken() {
        return taken;
    }    
        
    public StoryCard getStoryCard() {
        return storycard;
    }
    
    public void setStoryCard(StoryCard s) {
        if (s == null) {
            clearStoryCard();
        } else {
            s.getNode().addAttribute("Placeholder",this);            
            storycard = s;
            taken = true;
        }
    }
    
    public void clearStoryCard() {
        storycard.getNode().addAttribute("Placeholder",null);
        storycard = null;
        taken = false;
    }

    // Implement Originator
    // --------------------
    
    private static final class PlaceholderMemento implements Memento {
        // No need to defensively copy anything as StoryCardMemento should be
        // immutable.
        private final Memento storyCardMemento;
        PlaceholderMemento (Placeholder p) {
            StoryCard sc = p.getStoryCard();
            if (sc == null) {
                this.storyCardMemento = null;
            } else {
                this.storyCardMemento = sc.createMemento();
            }
        }
        Memento getStoryCardMemento() { return storyCardMemento; }
    }
    
    public Memento createMemento() {
        return new PlaceholderMemento(this);
    }
    
    public static Placeholder newInstanceFromMemento(Memento m) throws MementoException {
        if (m == null) {
            String detail = "Null memento object.";
            MementoException e = new MementoException(detail);
            Logger.getLogger(Placeholder.class.getName()).throwing("Placeholder", "newInstanceFromMemento", e);
            throw e;
        }
        if (!(m instanceof PlaceholderMemento)) {
            String detail = "Wrong type of memento object.";
            MementoException e = new MementoException(detail);
            Logger.getLogger(Placeholder.class.getName()).throwing("Placeholder", "newInstanceFromMemento", e);
            throw e;
        }
        PlaceholderMemento pm = (PlaceholderMemento) m;
        Placeholder p = new Placeholder();                
        Memento scm = pm.getStoryCardMemento();
        if (scm != null) {
            p.setStoryCard(StoryCard.newInstanceFromMemento(scm));
        }        
        return p;
    }
}    