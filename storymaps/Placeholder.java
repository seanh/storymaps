package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;

public class Placeholder {
            
    private PNode background;
    private boolean taken = false;
    private StoryCard storycard = null;
    
    public Placeholder() {                
        background = PPath.createRoundRectangle(-100, -120, 200, 240,20,20);
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

    public static class Memento {
        public Object storycard_memento;
        public Memento(Object storycard_memento) {
            this.storycard_memento = storycard_memento;
        }
        @Override
        public String toString() {
            String string = "<div class='placeholder'>\n";
            if (storycard_memento != null) {
                string += storycard_memento.toString();
            }
            string += "</div><!--placeholder-->\n";
            return string;
        }        
    }      
    
    public Object saveToMemento() {
        if (storycard == null) {
            return new Memento(null);
        } else {
            return new Memento(storycard.saveToMemento());
        }
    }
    
    public static Placeholder newFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException("Argument not instanceof Memento.");
        }
        else {
            Memento m = (Memento) o;
            Placeholder p = new Placeholder();
            if (m.storycard_memento != null) {
                StoryCard s = StoryCard.newFromMemento(m.storycard_memento);
                p.setStoryCard(s);
            }                        
            return p;
        }
    }
}