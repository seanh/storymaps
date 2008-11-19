package storymaps;

import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryCards class.
 * 
 * @author seanh
 */
public class StoryCardsDemo extends PFrame {

    private StoryCards cards;
    private StoryMap map;
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        cards = new StoryCards("Story Cards");
        getCanvas().getLayer().addChild(cards.getNode());
        
        map = new StoryMap("Story Map");
        map.getNode().setOffset(0,1100);
        getCanvas().getLayer().addChild(map.getNode());
        
        // Remove the default event handler that enables panning with the mouse.    
        getCanvas().removeInputEventListener(getCanvas().getPanEventHandler());        
    }
    
    public static void main(String args[]) {
        StoryCardsDemo demo = new StoryCardsDemo();
    }
}

