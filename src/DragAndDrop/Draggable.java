
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@lavabit.com>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
// FIXME: only one event handler, attached to the canvas, is needed to implement
// drag & drop, not one event handler per draggable instance attached to the
// draggable node. The event handler could be a static field. But how to access
//the canvas to add the event handler?
package DragAndDrop;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPickPath;
import java.awt.geom.Point2D;
import storymaps.Messager;

/**
 * A wrapper class for PNode that makes a PNode 'draggable' (i.e. it can be
 * dragged and dropped).
 * 
 * @author seanh
 */
public class Draggable extends DragDropSubject {

    /**
     * The PNode that is wrapped by this draggable.
     */
    private PNode node;
    
    /**
     * Records whether this draggable is currently being dragged or not.
     */
    private boolean isDragging = false;
    
    /**
     * Construct a new Draggable instance.
     * 
     * @param node The PNode to be wrapped by this draggable.
     * @throws NodeAlreadyDraggableException If an attribute with key
     * "Draggable" already exists on the given PNode.
     */    
    public Draggable(PNode node) throws NodeAlreadyDraggableException {
        if (node.getAttribute("Draggable") != null) {
            throw new NodeAlreadyDraggableException();
        } else {
            node.addAttribute("Draggable", this);
            this.node = node;
            node.addInputEventListener(createEventHandler());            
        }
    }
    
    /**
     * Return true if this draggable is currently being dragged, false
     * otherwise.
     */
    public boolean isDragging() { return isDragging; }
        
    /**
     * Return a new drag & drop event handler, that can then be added to a node.
     * It's in this event handler that most of the actual work of dragging and
     * dropping is handled.
     * 
     * @return A new drag & drop event handler.
     */
    private PDragEventHandler createEventHandler() {       
        PDragEventHandler dragEventHandler = new PDragEventHandler() {
            private Point2D startPos;
            private PNode previousParent;
            @Override
            protected void startDrag(PInputEvent e) {
                super.startDrag(e);
                startPos = getDraggedNode().getOffset();
                // We want the dragged node to appear on top of all other nodes
                // in the scene graph, so we use getParent() to search up the
                // scene graph starting from the dragged node until we find the
                // PLayer at the top, then we reparent the dragged node to the
                // PLayer. (If no PLayer is found, the dragged node simply won't
                // be reparented.)
                PNode node = getDraggedNode();
                // Save the node's original parent so we can return it later.
                previousParent = node.getParent();
                while (node != null) {
                    if (node instanceof PLayer) {
                        PLayer layer = (PLayer) node;                        
                        getDraggedNode().reparent(layer);
                        break;
                    }
                    node = node.getParent();
                }
                Messager.getMessager().send("drag started", getDraggedNode());
                isDragging = true;
            }
            @Override
            protected void endDrag(PInputEvent e) {
                // Return the node to its original parent.
                getDraggedNode().reparent(previousParent);
                
                // Find the draggable that was dragged.
                PNode dragNode = getDraggedNode();
                Draggable draggable = (Draggable)
                                             dragNode.getAttribute("Draggable");
                if (draggable == null) {
                    System.out.println("Dragged a node that wasn't wrapped in a draggable?");
                }
                
                // Find the droppable that was dropped onto.
                PPickPath path = e.getInputManager().getMouseOver();
                PNode dropNode = path.getPickedNode();
                // The first node under the mouse is going to be the dragged
                // node, so we move down the pick path until we find the first
                // picked node that is not the dragged node.
                while (dropNode == dragNode) {                    
                    dropNode = path.nextPickedNode();
                }
                Droppable droppable = null;
                if (dropNode != null) {
                    // Find the Droppable object attached to the dropNode                
                    droppable = (Droppable) dropNode.getAttribute("Droppable");
                    while (droppable == null) {
                        dropNode = path.nextPickedNode();
                        if (dropNode instanceof PCamera || dropNode == null) { break; }
                        droppable = (Droppable) dropNode.getAttribute("Droppable");
                    }
                }
                if (droppable == null) {
                    // No droppable attribute, just return the dragged node to
                    // where it was dragged from.
                    dragNode.setOffset(startPos);
                } else {
                    // Construct a DropEvent to represent the drop.
                    DropEvent de = new DropEvent(draggable,droppable,e.getPosition());
                    boolean accepted = droppable.dropped_onto(de);                                        
                    if (accepted) {
                        // The droppable accepted the drop, notify observers.                       
                        // FIXME: should we reparent the draggee's node to the
                        // droppee's node here? Or leave that up to the
                        // DroppableOwner to do if it pleases?
                        draggable.notify_observers(de);
                        droppable.notify_observers(de);
                    }
                    else {
                        // The Droppable refused the dragged node, just return
                        // the dragged node to where it came from.
                        dragNode.setOffset(startPos);
                    }
                }
                super.endDrag(e);
                isDragging = false;
            }
        };
        return dragEventHandler;
    }
    
    public PNode getNode() {
        return node;
    }
}