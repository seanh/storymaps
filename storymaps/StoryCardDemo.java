package storymaps;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryCard class.
 * 
 * @author seanh
 */
public class StoryCardDemo extends PFrame {

    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        StoryCard card = new StoryCard("Villainy", "A villainous act by a villain, committed in a villainous way and, generally, in the spirit of villainy.",this);
        getCanvas().getLayer().addChild(card.getNode());
    }    

    public static void main(String args[]) {
        StoryCardDemo demo = new StoryCardDemo();
    }    
}
