
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
package DragAndDrop;

import java.awt.geom.Point2D;

/**
 * A passive class to record details about a drop event (when a draggable is
 * dropped onto a droppable).
 * 
 * @author seanh
 */
public class DropEvent {
    private Draggable draggee;
    private Droppable droppee;
    private Point2D position;
    public DropEvent(Draggable draggee, Droppable droppee, Point2D position) {
        this.draggee = draggee;
        this.droppee = droppee;
        this.position = position;
    }
    public Draggable getDraggee() {
        return draggee;
    }
    public Droppable getDroppee() {
        return droppee;
    }
    public Point2D getPosition() {
        return position;
    }
}