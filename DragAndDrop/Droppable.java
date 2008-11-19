package DragAndDrop;

import edu.umd.cs.piccolo.PNode;

class NodeAlreadyDroppableException extends Exception {}

/**
 * A wrapper class for PNode that makes a PNode 'droppable' (i.e. a draggable
 * can be dropped onto it).
 *
 * @author seanh
 */
public class Droppable extends DragDropSubject {

    /**
     * The PNode that this droppable is wrapping.
     */
    private PNode node;

    /**
     * The owner of this droppable. Whenever a node is dragged and dropped onto
     * this droppable, the decision of whether or not to accept the drop will be
     * delegated to the owner, via the DroppableOwner interface.
     */
    private DroppableOwner owner;
    
    /**
     * Construct a new Droppable instance.
     * 
     * @param node The PNode to be wrapped by this droppable.
     * @param owner The DroppableOwner of this droppable. Whenever a node is
     * dragged and dropped onto this droppable, the decision of whether or not
     * to accept the drop will be delegated to the owner, via the DroppableOwner
     * interface.
     * @throws storymaps.NodeAlreadyDroppableException If an attribute with
     * key "Droppable" already exists on the given PNode.
     */
    public Droppable(PNode node, DroppableOwner owner)
                     throws NodeAlreadyDroppableException {
        if (node.getAttribute("Droppable") != null) {
            throw new NodeAlreadyDroppableException();
        } else {
            node.addAttribute("Droppable", this);
            this.node = node;
            node.addAttribute("DroppableOwner", owner);
            this.owner = owner;
        }
    }
    
    /**
     * This method is called whenever a node is dropped onto this droppable. It
     * delegates the decision of whether or not to accept the drop to this
     * droppable's owner.
     * 
     * @param draggee The PNode that was dragged and dropped.
     * @return true if this droppable wishes to accept the dropped node, false
     * otherwise.
     */
    public boolean dropped_onto(DropEvent de) {
        return owner.dropped_onto(de);
    }

    public PNode getNode() {
        return node;
    }
}