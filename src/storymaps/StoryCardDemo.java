
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
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the StoryCard class.
 * 
 * @author seanh
 */
public class StoryCardDemo extends PFrame implements Receiver {

    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        Function f = new Function(1,"Name","Description","Instructions");
        StoryCard card = new StoryCard(f, "A villainous act by a villain, committed in a villainous way and, generally, in the spirit of villainy.");
        getCanvas().getLayer().addChild(card.getNode());
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);
    }
    
    public static void main(String args[]) {
        StoryCardDemo demo = new StoryCardDemo();
    }
    
     public void receive(String name, Object receiver_arg, Object sender_arg) {
         if (name.equals("StoryCard clicked")) {
            PCamera cam = getCanvas().getCamera();
            StoryCard card = (StoryCard) sender_arg;
            PNode node = card.getNode();
            cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 500);
         }
     }
}