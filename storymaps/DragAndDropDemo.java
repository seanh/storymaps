import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPickPath;
import edu.umd.cs.piccolox.PFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Random;

public class DragAndDropDemo extends PFrame {

    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();
        
        // Create some small rectangles.
        Random random = new Random();
        for (int i=0; i<5; i++) {
            PPath rect = PPath.createRectangle(0, 0, 100, 80);
            rect.setOffset(random.nextFloat()*1000, random.nextFloat()*1000);
            rect.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                    random.nextFloat()));
            rect.setStroke(new BasicStroke(1 + (10 * random.nextFloat())));
            rect.setStrokePaint(new Color(random.nextFloat(),random.nextFloat(),
                                          random.nextFloat()));
            canvas.getLayer().addChild(rect);
        }

        // Remove the default event handler that enables panning with the mouse.    
        canvas.removeInputEventListener(canvas.getPanEventHandler());

        // Create a new drag-and-drop event handler...
        PDragEventHandler dragEventHandler =
              new PDragEventHandler() {

            @Override
            protected void startDrag(PInputEvent e) {
                super.startDrag(e);
                System.out.println("Dragge: " + getDraggedNode());
            }

            @Override
            protected void endDrag(PInputEvent e) {
                PPickPath path = e.getInputManager().getMouseOver();
                PNode dropTarget = path.getPickedNode();
                while (dropTarget == getDraggedNode()) {
                    dropTarget = path.nextPickedNode();
                }
                System.out.println("Drop Target: " + dropTarget);
                super.endDrag(e);
            }
        };
        dragEventHandler.setMoveToFrontOnPress(true);        
        // ...and add it to the canvas.
        canvas.addInputEventListener(dragEventHandler);
    }

    public static void main(String[] args) {
        new DragAndDropDemo();
    }
}