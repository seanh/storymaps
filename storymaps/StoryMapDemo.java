package storymaps;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolox.PFrame;
import java.awt.geom.Point2D;

/**
 * A demo class for the StoryMap class.
 * 
 * @author seanh
 */
public class StoryMapDemo extends PFrame implements Receiver {

    private VerticalLayoutNode home;
    private StoryMap bank;
    private StoryMap map;
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        home = new VerticalLayoutNode(50);
        getCanvas().getLayer().addChild(home);
        
        bank = new StoryMap("Story Cards");
        for (int i=0; i<30; i++) {
            StoryCard card = new StoryCard("Argh", "Aaaargh!");
            bank.addStoryCard(card);
        }
        home.addChild(bank.getNode());
        
        map = new StoryMap("My Story Map");
        home.addChild(map.getNode());

        final PCamera cam = getCanvas().getCamera();
        cam.addInputEventListener(new PBasicInputEventHandler() { 		                    
            // Make the camera zoom out when RMB is pressed.
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 2) {
                    cam.animateViewToCenterBounds(home.getGlobalFullBounds(), true, 750);
                }
            }
        });        
                        
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);
        
        // Remove the default event handler that enables panning with the mouse.    
        PCanvas canvas = getCanvas();
        canvas.removeInputEventListener(canvas.getPanEventHandler());

        // Create a new drag-and-drop event handler...
        PDragEventHandler dragEventHandler =
              new PDragEventHandler() {

            private Point2D startPos;
            
            @Override
            protected void startDrag(PInputEvent e) {
                super.startDrag(e);
                startPos = getDraggedNode().getOffset();
            }

            @Override
            protected void endDrag(PInputEvent e) {
                PPickPath path = e.getInputManager().getMouseOver();
                PNode dropTarget = path.getPickedNode();
                while (dropTarget == getDraggedNode()) {
                    dropTarget = path.nextPickedNode();
                }
                StoryMap storymap = (StoryMap) dropTarget.getAttribute(StoryMap.attribute);
                if (storymap == null) {
                    getDraggedNode().setOffset(startPos);
                }
                else {
                    StoryCard s = (StoryCard) getDraggedNode().getAttribute(StoryCard.attribute);
                    if (s == null) {
                        System.out.println("Dragged something that wasn't a StoryCard?");               
                    }
                    else {
                        storymap.addStoryCard(s);
                    }
                }                
                super.endDrag(e);
            }
        };
        dragEventHandler.setMoveToFrontOnPress(true);        
        // ...and add it to the canvas.
        canvas.addInputEventListener(dragEventHandler);        
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

