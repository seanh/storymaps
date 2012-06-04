
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
package DragAndDrop;

/**
 * An interface to be implemented by classes that want to own droppables. Each
 * droppable must have exactly one owner. When a node is dragged and dropped
 * onto a droppable, the droppable delegates the decision of whether or not to
 * accept the drop to its owner, via this interface.
 * 
 * @author seanh
 */
public interface DroppableOwner {
   
    /**
     * This method is called whenever a node is dropped onto a droppable owned
     * by this DroppableOwner. Any action to be taken when a drop occurs should
     * be implemented here.
     * 
     * @param draggee The PNode that was dropped.
     * @return true to accept the drop (all observers of the draggable and the
     * droppable will be notified), false to refuse the drop.
     * 
     */    
    public boolean dropped_onto(DropEvent de);

}
