package storymaps;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryCards class.
 * 
 * @author seanh
 */
public class StoryCardsDemo extends PFrame implements Receiver {

    private VerticalLayoutNode home = new VerticalLayoutNode(50);
    private StoryCards cards;    
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        setSize(1024, 768);
        
        PCanvas canvas = getCanvas();
        canvas.getLayer().addChild(home);

        DroppableRectangle rect = new DroppableRectangle(2000,400);
        home.addChild(rect.getNode());
                
        cards = new StoryCards("Story Cards");
        final PNode node = cards.getNode();        
        home.addChild(cards.getNode());
                
        // Remove the default event handler that enables panning with the mouse.    
        canvas.removeInputEventListener(canvas.getPanEventHandler());
        
        final PCamera cam = canvas.getCamera();
        cam.animateViewToCenterBounds(home.getGlobalFullBounds(), true, 750);
        
        // Make middle mouse button return camera to home position.
        cam.addInputEventListener(new PBasicInputEventHandler() { 		                    
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 2) {
                    cam.animateViewToCenterBounds(home.getGlobalFullBounds(), true, 750);
                }
            }
        });
        
        // Listen for 'clicked' messages from story cards (the receive method
        // will be called), this is how we make RMB zoom in on cards.
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);        
    }

     public void receive(String name, Object receiver_arg, Object sender_arg) {
         if (name.equals("StoryCard clicked")) {
            PCamera cam = getCanvas().getCamera();
            StoryCard card = (StoryCard) sender_arg;
            PNode node = card.getNode();
            cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 750);
         }
     }    
    
    public static void main(String args[]) {
        StoryCardsDemo demo = new StoryCardsDemo();
    }
}