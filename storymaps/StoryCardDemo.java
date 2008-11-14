package storymaps;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryCard class.
 * 
 * @author seanh
 */
public class StoryCardDemo extends PFrame implements Receiver {

    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        StoryCard card = new StoryCard("Villainy", "A villainous act by a villain, committed in a villainous way and, generally, in the spirit of villainy.");
        getCanvas().getLayer().addChild(card.getNode());
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);
    }
    
    public static void main(String args[]) {
        StoryCardDemo demo = new StoryCardDemo();
    }
    
     public void receive(String name, Object receiver_arg, Object sender_arg) {
         if (name.equals("StoryCard clicked")) {
            PCamera cam = getCanvas().getCamera();
            StoryCard card = (StoryCard) sender_arg;
            PNode node = card.getNode();
            cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 500);
         }
     }
}

