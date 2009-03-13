package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * A DisabledStoryCard is like a story card but it is faded out and not
 * interactive.
 * 
 * FIXME: this class needs to be refactored, there should be a Box class for a
 * node that can "hold" another node and this class should be a subclass of Box
 * that adds some visible geometry to make the box look like a faded out
 * StoryCard for a particular function. There also needs to be some code sharing
 * between this class and StoryCard, both have a function and create some
 * storycard-shaped visual geometry for it.
 * 
 * 
 * @author seanh
 */
class DisabledStoryCard extends StoryCardBase implements Originator {
        
    private StoryCard storycard = null;
    private boolean taken = false;

    public DisabledStoryCard(Function f) {
        super(f);
        getNode().addAttribute("DisabledStoryCard",this);
        getBackground().setTransparency(.3f);
        for (int i = 0; i < getNode().getChildrenCount(); i++) {
            PNode child = getNode().getChild(i);
            child.setTransparency(.3f);
        }
        getBackground().addInputEventListener(new PBasicInputEventHandler() { 		        
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
    
    // Implement Originator
    // --------------------
    
    private static final class DisabledStoryCardMemento implements Memento {
        // No need to defensively copy anything as FunctionMemento and
        // StoryCardMemento should be immutable.
        private final Memento functionMemento;
        private final Memento storyCardMemento;
        DisabledStoryCardMemento (DisabledStoryCard dsc) {                        
            this.functionMemento = dsc.getFunction().createMemento();
            StoryCard sc = dsc.getStoryCard();
            if (sc == null) {
                this.storyCardMemento = null;
            } else {
                this.storyCardMemento = sc.createMemento();
            }
        }
        Memento getFunctionMemento() { return functionMemento; }
        Memento getStoryCardMemento() { return storyCardMemento; }
    }
    
    public Memento createMemento() {
        return new DisabledStoryCardMemento(this);
    }
    
    public static DisabledStoryCard newInstanceFromMemento(Memento m)
            throws MementoException {
        if (m == null) {
            String detail = "Null memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        if (!(m instanceof DisabledStoryCardMemento)) {
            String detail = "Wrong type of memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        DisabledStoryCardMemento dscm = (DisabledStoryCardMemento) m;        
        Function f = Function.newInstanceFromMemento(dscm.getFunctionMemento());
        DisabledStoryCard dsc = new DisabledStoryCard(f);
        Memento scm = dscm.getStoryCardMemento();
        if (scm != null) {
            dsc.setStoryCard(StoryCard.newInstanceFromMemento(scm));
        }
        return dsc;
    }
}