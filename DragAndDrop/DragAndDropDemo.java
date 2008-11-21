/**
 * A demonstration of the drag & drop protocol implemented by this package.
 * This is a general and flexible drag & drop protocol for Piccolo, that you
 * might be able to integrate into your own Piccolo application.
 * 
 * The demo has a number of rectangles that can be dragged, and a number of
 * ellipses that can be dropped onto. When a rectangle is dropped onto an
 * ellipse the ellipse changes the color of the rectangle to match its own color
 * (the rectangle is still visible by its outline). Each ellipse maintains a set
 * of the rectangles that have been dropped onto it, and displays the number of
 * rectangles in this set. You can drag a rectangle from one ellipse to another.
 * Just to show that multiple objects can observe the same draggables and
 * droppables, two text nodes print out information about drag and drop events
 * as they occur. One does this by observing all the draggables (the
 * rectangles), the other prints out the same information by observing the
 * droppables (ellipses).
 * 
 * How the Drag & Drop Protocol Works
 * ----------------------------------
 * 
 * Any PNode can be dragged and/or dropped onto. To make a PNode draggable or
 * droppable, wrap it in one of the decorator classes Draggable or Droppable. To
 * make sure that the same PNode cannot be wrapped by two droppables or two
 * draggables at once, the consructors of the decorator classes throw
 * exceptions if the node is already draggable or droppable:
 * 
 *    try {
 *        Draggable draggable = new Draggable(pnode);
 *    } catch (PNodeAlreadyDraggableException e) {
 *        // This PNode is already wrapped in a draggable!
 *    }     
 * 
 *    try {
 *        Droppable droppable = new Droppable(another_pnode, owner);
 *    } catch (PNodeAlreadyDroppableException e) {
 *        // This PNode is already wrapped in a droppable!
 *    }     
 * 
 * (A draggable is a node that can be dragged and dropped, a droppable is
 * a node that a draggable can be dropped onto, the same node can be both
 * draggable and droppable at the same time.)
 * 
 * If a draggable node is dragged and dropped but not onto a droppable node,
 * then the draggable node simple snaps back to the position it was dragged
 * from, and nothing happens. If a draggable node is dropped onto a droppable
 * node then the owner of the the droppable node is notified and may take
 * action, and must either accept or refuse the drop. If the drop is accepted,
 * then the draggable will _not_ snap back to its original position, and all the
 * objects observing the draggable and the droppable node will be notified. If
 * the drop is refused then the draggable snaps back to its original position
 * and nothing happens.
 * 
 * A droppable has to have an owner, an object that implements the
 * DroppableOwner interface, which contains the single method dropped_onto. When
 * a draggable is dropped onto a droppable, Droppable calls the dropped_onto
 * method of the owner. Any action to be taken by the owner when a drop occurs
 * should be implemented here. In particular, the method must return true or
 * false to indicate whether the drop should be accepted or refused.
 *
 * The parameter to dropped_onto is a DropEvent, a passive object that holds
 * information about a drop event, including the Draggable object and the
 * Droppable object, so you can see what object has been dropped onto you.
 * 
 * If a DroppableOwner accepts a drop, then all observers of the draggee and the
 * droppee will be notified and passed a reference to the DropEvent. Both
 * Draggable and Droppable implement an observer pattern, any object that
 * implements the DragDropObserver interface can attach itself to any number of
 * draggables or droppables by calling their attach methods and passing itself.
 * When a drop is accepted, all observers attached to the draggee and the
 * droppee are notified via the notify method of the DragDropObserver interface.
 * 
 * @author seanh
 */
package DragAndDrop;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.PFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.HashSet;
import java.util.Random;

/**
 * A PText node that implements DragDropServer and prints out (to the Piccolo
 * canvas) the details of every drop event it is notified of.
 * @author seanh
 */
class TextObserver extends PText implements DragDropObserver {
    public TextObserver(String text) {
        super(text);
    }
    public boolean notify(DropEvent de) {
        Draggable draggee = de.getDraggee();
        Droppable droppee = de.getDroppee();
        setText(getText()+"\n"+draggee+" was dropped onto\n   "+droppee);
        return true;
    }            
}

/**
 * A class that composes a PText node and an elliptical PPath node.
 * 
 * The ellipse is wrapped in a droppable. This class implements DroppableOwner,
 * and passes itself as the owner of the droppable ellipse node. When a
 * draggable node is dropped onto the ellipse, it changes the color of the node
 * to match that of the ellipse, and it adds the node to a set of nodes.
 * 
 * The text node is updated so that it always displays the number of nodes in
 * the set of nodes that have been dragged onto the ellipse. This class
 * implements DragDropObserver and attaches itself as an observer to each node
 * that is dragged onto the ellipse, in order to be notified of when a node is
 * dragged out of the ellipse and dropped onto another droppable, so that the
 * node can be removed from the set of nodes.
 *
 * @author seanh
 */
class DroppableEllipse implements DroppableOwner, DragDropObserver {

    // Lazy, public fields.
    public PPath ellipse = PPath.createEllipse(0, 0, 400, 400);
    private HashSet<Draggable> draggees = new HashSet<Draggable>();
    private PText text = new PText("0");
    public Droppable d;
    
    public DroppableEllipse() {
        ellipse.addChild(text);
        try {
            // Make the ellipse droppable.
            d = new Droppable(ellipse,this);
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }               
    }
    
    /**
     * Add more text to the PText node.
     */
    private void setText() {        
        text.setText(""+draggees.size());
    }
    
    /**
     * A node has been dropped onto the sphere.
     * 
     * @param de The DropEvent object.
     * @return true, always accept drops.
     */
    public boolean dropped_onto(DropEvent de) {                        

        // Attach self as observer of draggable that was dropped.
        Draggable draggee = de.getDraggee();
        draggee.attach(this);
        
        // Change color of draggable node.
        draggee.getNode().setPaint(ellipse.getPaint());
        
        // Add draggable node to set of nodes dragged onto this ellipse, and
        // update text node.
        draggees.add(draggee);
        setText();
        
        // Accept the drop, observers will be notified.
        return true;        
    }
    
    /**
     * A node that was dragged onto the ellipse has now been dragged onto
     * another droppable.
     * @param de The DropEvent object.
     * @return true to stay subscribed as an observer of this node, false to
     * unsubscribe.
     */
    public boolean notify(DropEvent de) {
        Droppable droppee = de.getDroppee();
        Draggable draggee = de.getDraggee();
        if (droppee == d) {
            // The user dragged the node from the ellipse and dropped it onto
            // the same ellipse. Do nothing, and stay subscribed to the node.
            return true;
        }
        else {
            // Node was dropped onto some other droppable. Remove it from the
            // set, update the text node, and unsubscribe.
            draggees.remove(draggee);
            setText();
            return false;
        }
    }    
}

public class DragAndDropDemo extends PFrame {
    
    @Override
    public void initialize() {
        PCanvas canvas = getCanvas();

        Random random = new Random();
        
        // Create some droppable ellipses.
        TextObserver observer = new TextObserver("Droppable Observer\n------------------");
        canvas.getLayer().addChild(observer);
        observer.setOffset(900,500);
        DroppableEllipse droppable_ellipse = new DroppableEllipse();
        droppable_ellipse.d.attach(observer);
        PPath ellipse = droppable_ellipse.ellipse;
        ellipse.setOffset(0,0);
        ellipse.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                   random.nextFloat()));
        canvas.getLayer().addChild(ellipse);        
        droppable_ellipse = new DroppableEllipse();
        droppable_ellipse.d.attach(observer);
        ellipse = droppable_ellipse.ellipse;
        ellipse.setOffset(500,0);
        ellipse.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                   random.nextFloat()));
        canvas.getLayer().addChild(ellipse);        
        droppable_ellipse = new DroppableEllipse();
        droppable_ellipse.d.attach(observer);
        ellipse = droppable_ellipse.ellipse;
        ellipse.setOffset(500,500);
        ellipse.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                   random.nextFloat()));
        canvas.getLayer().addChild(ellipse);
                
        // Create some rectangles and make them draggable.
        observer = new TextObserver("Draggable Observer\n------------------");
        canvas.getLayer().addChild(observer);
        observer.setOffset(900,0);
        for (int i=0; i<5; i++) {
            PPath rect = PPath.createRectangle(0, 0, 100, 80);
            rect.setOffset(random.nextFloat()*1000, random.nextFloat()*1000);
            rect.setPaint(Color.WHITE);
            rect.setStroke(new BasicStroke(10));
            rect.setStrokePaint(Color.BLACK);
            canvas.getLayer().addChild(rect);
            try {
                Draggable d = new Draggable(rect);
                d.attach(observer);
            } catch (NodeAlreadyDraggableException e) {
                // ...
            }                                    
        }
                
        // Remove the default event handler that enables panning with the mouse.    
        canvas.removeInputEventListener(canvas.getPanEventHandler());
    }
    
    public static void main(String[] args) {
        new DragAndDropDemo();
    }
}