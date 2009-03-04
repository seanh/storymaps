package storymaps;

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
class DisabledStoryCard extends StoryCardBase {
        
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
}