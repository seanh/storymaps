
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
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Random;

/**
 * A demo class for the VerticalLayoutNode class.
 * 
 * @author seanh
 */
public class VerticalLayoutDemo extends PFrame {

    private VerticalLayoutNode node;
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        node = new VerticalLayoutNode(10);
        Random random = new Random();
        for (int i=0; i<5; i++) {
            PPath each = PPath.createRectangle(0, 0, 100, 80);
            each.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                    random.nextFloat()));
            each.setStroke(new BasicStroke(1 + (10 * random.nextFloat())));
            each.setStrokePaint(new Color(random.nextFloat(),random.nextFloat(),
                                          random.nextFloat()));
            node.addChild(each);
        }
        getCanvas().getLayer().addChild(node);
    }
    
    public static void main(String args[]) {
        VerticalLayoutDemo demo = new VerticalLayoutDemo();
    }
}

