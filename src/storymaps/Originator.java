
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

/**
 * An exception to be thrown by newInstanceFromMemento method implementations if
 * there is something wrong with the memento they receive (e.g. it's null, or
 * it's not an instanceof the right concrete subtype of Memento).
 * @author seanh
 */
class MementoException extends Exception {
    MementoException(String detail) {
        super(detail);
    }
}

/**
 * Memento design pattern.
 * 
 * An originator object can create a memento containing a snapshot of its
 * current internal state. (And a static factory method, not specified in this
 * interface, can create a new originator from the state recorded in a
 * memento.)
 * 
 * (See interface Memento.)
 * 
 * @author seanh
 */
public interface Originator {

    /**
     * Return a memento object recording the current internal state of this
     * originator.
     */
    Memento createMemento();
    
    // FIXME: classes that implement Originator also implement a
    // `static newInstanceFromMemento(Memento m)` method, but this method cannot
    // be specified in an interface or base class because it's static. May need
    // to use generics to fix this.
}