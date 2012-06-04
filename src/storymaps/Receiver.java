
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
package storymaps;

/**
 * The interface that must be implemented by any class that wants to receive
 * messages from the singleton Messager object.
 * 
 * @author seanh
 */
interface Receiver {    
    /**
     * This method is called by the messager when a message is sent with a name
     * that this receiver is subscribed to. Subclasses should override this
     * method to receive messages.
     * 
     * @param name The name of the message that was sent.
     * @param receiver_arg The argument that was passed to accept or acceptOnce
     *                     when subscribing to the message name.
     * @param sender_arg The argument that was passed to send by the object that
     *                   sent the message.
     */
    public void receive(String name, Object receiver_arg, Object sender_arg);
}
