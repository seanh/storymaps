package storymaps;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the GridLayoutNode class.
 * 
 * @author seanh
 */
public class GridLayoutDemo extends PFrame implements Receiver {

    private GridLayoutNode grid;
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        grid = new GridLayoutNode(10,10);
        for (int i=0; i<30; i++) {
            StoryCard card = new StoryCard("Argh", "Aaaargh!");
            grid.addChild(card.getNode());
        }
        getCanvas().getLayer().addChild(grid);

        final PCamera cam = getCanvas().getCamera();
        cam.addInputEventListener(new PBasicInputEventHandler() { 		                    
            // Make the camera zoom out when RMB is pressed.
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 3) {
                    cam.animateViewToCenterBounds(grid.getGlobalFullBounds(), true, 750);
                }
            }
        });        
                        
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);
    }
    
    public static void main(String args[]) {
        GridLayoutDemo demo = new GridLayoutDemo();
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

