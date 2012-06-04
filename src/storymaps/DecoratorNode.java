
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
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * A node that bases its bounds and rendering on its children, and paints a
 * background rectangle that encompasses all of its children.
 * 
 * @author seanh (adapted from example by Lance Good)
 */
public class DecoratorNode extends PNode {

    int INDENT = 10;
    PBounds cachedChildBounds = new PBounds();
    PBounds comparisonBounds = new PBounds();

    /**
     * Change the default paint to fill an expanded bounding box based on its
     * children's bounds.
     */
    @Override
    public void paint(PPaintContext ppc) {
        Paint paint = getPaint();
        if (paint != null) {
            Graphics2D g2 = ppc.getGraphics();
            g2.setPaint(paint);

            PBounds bounds = getUnionOfChildrenBounds(null);
            bounds.setRect(bounds.getX() - INDENT, bounds.getY() - INDENT,
               bounds.getWidth() + 2 * INDENT, bounds.getHeight() + 2 * INDENT);
            g2.fill(bounds);
        }
    }

    /**
     * Change the full bounds computation to take into account that we are
     * expanding the children's bounds. Do this instead of overriding
     * getBoundsReference() since the node is not volatile
     */
    @Override
    public PBounds computeFullBounds(PBounds dstBounds) {
        PBounds result = getUnionOfChildrenBounds(dstBounds);

        cachedChildBounds.setRect(result);
        result.setRect(result.getX() - INDENT, result.getY() - INDENT, result.getWidth() + 2 * INDENT, result.getHeight() + 2 * INDENT);
        localToParent(result);
        return result;
    }

    /**
     * This is a crucial step.  We have to override this method to invalidate
     * the paint each time the bounds are changed so we repaint the correct
     * region.
     */
    @Override
    public boolean validateFullBounds() {
        comparisonBounds = getUnionOfChildrenBounds(comparisonBounds);

        if (!cachedChildBounds.equals(comparisonBounds)) {
            setPaintInvalid(true);
        }
        return super.validateFullBounds();
    }
}
