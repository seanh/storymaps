package storymaps;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryMap class.
 * 
 * @author seanh
 */
public class StoryMapDemo extends PFrame implements Receiver {

    private StoryMap map;
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        map = new StoryMap("My Story Map");
        for (int i=0; i<30; i++) {
            StoryCard card = new StoryCard("Argh", "Aaaargh!");
            map.addStoryCard(card);
        }
        getCanvas().getLayer().addChild(map.getNode());

        final PCamera cam = getCanvas().getCamera();
        cam.addInputEventListener(new PBasicInputEventHandler() { 		                    
            // Make the camera zoom out when RMB is pressed.
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 3) {
                    cam.animateViewToCenterBounds(map.getGlobalFullBounds(), true, 750);
                }
            }
        });        
                        
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);
    }
    
    public static void main(String args[]) {
        StoryMapDemo demo = new StoryMapDemo();
    }
    
     public void receive(String name, Object receiver_arg, Object sender_arg) {
         if (name.equals("StoryCard clicked")) {
            PCamera cam = getCanvas().getCamera();
            StoryCard card = (StoryCard) sender_arg;
            PNode node = card.getNode();
            cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 750);
         }
     }
}

