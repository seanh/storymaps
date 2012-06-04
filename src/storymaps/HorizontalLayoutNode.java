
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

import edu.umd.cs.piccolo.PNode;
import java.util.Iterator;

/**
 * A layout node that lays its children out in a horizontal line.
 * 
 * @author seanh
 */
public class HorizontalLayoutNode extends PNode {
    
    /**
     * The size of the gap between each node.         
     */
    private int margin;
    
    private int prev_num_children = 0;
    
    public HorizontalLayoutNode(int margin) {
        this.margin = margin;        
    }
    
    
    public int getMargin() {
        return margin;
    }
    
    @Override
    /**
     * Reposition every child node of this node, arranging them into a
     * horizontal line with gaps of size this.margin between the nodes.
     */
    public void layoutChildren() {
        // We only recompute the layout if a child node has been added or
        // removed (and not if one has just moved or changed size).
        if (getChildrenCount() == prev_num_children) {
            return;
        }
        else {
            prev_num_children = getChildrenCount();
        }
        
        double xOffset = 0;
        double yOffset = 0;
                        
        Iterator i = getChildrenIterator();
        while (i.hasNext()) {
            PNode child = (PNode) i.next();
            child.setOffset(xOffset - child.getX(), yOffset);
            xOffset += child.getFullBoundsReference().getWidth() + margin;
        }                            
    }    
}