package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;

public class Placeholder {
            
    private PNode background;
    private boolean taken = false;
    private StoryCard storycard = null;
    
    public Placeholder() {                
        background = PPath.createRoundRectangle(0, 0, 200, 240,20,20);
        background.setPaint(Color.CYAN);
        background.setTransparency(0);              
        background.addAttribute("Placeholder",this);                
        background.setPickable(false);        
    }
                                 
    public PNode getNode() {
        return background;
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
            storycard = s;
            taken = true;
        }
    }
    
    public void clearStoryCard() {
        storycard = null;
        taken = false;
    }
    
    public Object saveToMemento() {
        // The only state a Placeholder needs to save is the StoryCard (or null)
        // that it's holding, so instead of a Placeholder.Memento class we just
        // use a StoryCard.Memento, or null.
        if (storycard == null) {
            return null;
        } else {
            return storycard.saveToMemento();
        }
    }
    
    public static Placeholder newFromMemento(Object o) {
        Placeholder p = new Placeholder();
        if (o == null) {
            return p;
        } else {
            StoryCard s = StoryCard.newFromMemento(o);            
            p.setStoryCard(s);
            return p;
        }
    }
}