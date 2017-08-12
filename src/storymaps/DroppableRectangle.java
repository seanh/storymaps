
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

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
package storymaps;

import edu.umd.cs.piccolo.nodes.PPath;
import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;

/**
 * A simple class that implements DroppableOwner, used for testing stuff.
 * 
 * @author seanh
 */
class DroppableRectangle implements DroppableOwner {

    // Lazy, public fields.
    public PPath rect;
    public Droppable d;
    
    public DroppableRectangle(int height, int width) {
        rect = PPath.createRoundRectangle(0, 0, height, width, 50, 50);
        try {
            // Make the ellipse droppable.
            d = new Droppable(rect,this);
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }               
    }
        
    /**
     * A node has been dropped onto the rectangle, accept it.
     * 
     * @param de The DropEvent object.
     * @return true, always accept drops.
     */
    public boolean dropped_onto(DropEvent de) {                        
        
        Droppable droppee = de.getDroppee();
        Draggable draggee = de.getDraggee();
        rect.addChild(draggee.getNode());
        draggee.getNode().setOffset(0,0);
        draggee.getNode().moveToFront();        
        // Accept the drop, observers will be notified.
        return true;        
    }
    
    public PNode getNode() {
        return rect;
    }
}