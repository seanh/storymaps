
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

import java.io.Serializable;


/**
 * Memento design pattern.
 * 
 * A memento object stores the internal state of a originator object (see
 * Originator interface).
 * 
 * There is always a one-one relationship between a class that implements
 * Originator and a corresponding class that implements Memento.
 * 
 * Mementos are passive, only the class that created a memento can access the
 * internal state of the memento.
 *  
 * Notes on implementing Memento
 * -----------------------------
 * 
 * * Memento classes should be private static final inner classes of their
 *   Originator classes so that encapsulation of the originator's internal state
 *   that the memento records is opaque.
 * 
 * * Implementations of Memento should be immutable:
 * 
 *   * The class should not have any mutator methods.
 *   * The class should be final so that it cannot be subclassed.
 *   * All fields of the class should be final.
 *   * All fields of the class should be private.
 *   * Make defensive copies of mutable objects before saving them as fields
 *     (e.g. in constructor methods) and before returning fields in accessor
 *     methods.
 * 
 * * Minimise the time and space needed to copy information into a memento by
 *   minimising the amount of information contained in a memento.
 * 
 * @author seanh
 */
public interface Memento extends Serializable {}