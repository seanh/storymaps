
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

import java.util.HashSet;

/**
 * Superclass for Draggable and Droppable. Implements the Observer interface
 * that is common to both.
 * 
 * @author seanh
 */
public class DragDropSubject {

    /**
     * The observers currently subscribed to this subject.
     */
    private HashSet<DragDropObserver> observers =
            new HashSet<DragDropObserver>();

    /**
     * Subscribe a new observer.
     */
    public void attach(DragDropObserver o) {
        observers.add(o);
    }

    /**
     * Unsubscribe an observer.
     */
    public void detach(DragDropObserver o) {
        observers.remove(o);
    }

    /**
     * Called by Draggable and Droppable when they want to notify all of their
     * observer of a drop event. If an observer's notify method returns false
     * it will be unsubscribed from the subject.
     */
    public void notify_observers(DropEvent de) {
        HashSet<DragDropObserver> to_detach = new HashSet<DragDropObserver>();
        for (DragDropObserver observer : observers) {
            if (!observer.notify(de)) {
                to_detach.add(observer);
            }
        }
        for (DragDropObserver observer : to_detach) {
            detach(observer);
        }
    }
}