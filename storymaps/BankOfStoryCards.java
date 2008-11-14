/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storymaps;

import edu.umd.cs.piccolo.PNode;
import java.util.Iterator;

/**
 * This is the bank of story cards that the user selects from. It is a node
 * that arranges its children (which should be StoryCard nodes) into a grid
 * formation.
 * 
 * @author seanh
 */
public class BankOfStoryCards extends PNode {

    @Override
    public void layoutChildren() {
        double columns = 8;        
        int count = 0;
        Iterator i = getChildrenIterator();
        while (i.hasNext()) {
            PNode each = (PNode) i.next();
            int xOffset = (int)Math.floor((count % columns) * 200);
            int yOffset = (int)(Math.floor(count/columns)*200);
            each.setOffset(xOffset - each.getX(), yOffset - each.getY());
            count++;
        }

    }
}