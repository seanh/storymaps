package storymaps;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 *
 * @author seanh
 */
public class DisabledStoryCard extends StoryCardBase {
        
    private StoryCard storycard = null;
    private boolean taken = false;

    public DisabledStoryCard(Function f) {
        super(f);
        getNode().addAttribute("DisabledStoryCard",this);
        background.setTransparency(.3f);
        image_node.setTransparency(.3f);
        background.addInputEventListener(new PBasicInputEventHandler() { 		        
            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getButton() == 1 && event.getClickCount() == 2) {
                    // If the StoryCard is double-clicked with the left mouse
                    // button send the "StoryCard double-clicked" message.
                    Messager m = Messager.getMessager();
                    m.send("StoryCard double-clicked", DisabledStoryCard.this);                
                    goToHighDetail();
                    event.setHandled(true);
                }
            }
        });
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
            s.getNode().addAttribute("DisabledStoryCard",this);            
            storycard = s;
            taken = true;
        }
    }
    
    public void clearStoryCard() {
        storycard = null;
        taken = false;
    }
    
    public static class Memento {
        public Object function_memento;
        public Object storycard_memento;
        public Memento(Object function_memento, Object storycard_memento) {
            this.function_memento = function_memento;
            this.storycard_memento = storycard_memento;
        }
        @Override
        public String toString() {
            String string = "<div class='DisabledStoryCard'>\n";
            string += function_memento.toString();
            if (storycard_memento != null) {
                string += storycard_memento.toString();
            }
            string += "</div><!--DisabledStoryCard-->\n";
            return string;
        }
    }
    
    public Object saveToMemento() {
        Object function_memento = function.saveToMemento();
        Object storycard_memento = null;
        if (storycard != null) {
            storycard_memento = storycard.saveToMemento();
        }
        return new Memento(function_memento,storycard_memento);
    }
    
    public static DisabledStoryCard newFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException("Argument not instanceof Memento");
        }
        Memento m = (Memento) o;
        Function f = Function.newFromMemento(m.function_memento);
        DisabledStoryCard d = new DisabledStoryCard(f);
        if (m.storycard_memento != null) {
            StoryCard s = StoryCard.newFromMemento(m.storycard_memento);
            d.setStoryCard(s);
        }
        return d;
    }
}
