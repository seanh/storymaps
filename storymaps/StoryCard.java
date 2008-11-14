package storymaps;

import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import java.util.Iterator;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import java.awt.geom.Dimension2D;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.PCamera;

/**
 * A class for story cards.
 * 
 * StoryCard has to extend PNode then add another PNode (the layout node) to
 * itself then add further PNodes to the layout node, otherwise the drag event
 * listener doesn't work. No idea why it has to be this way (one unnecessary
 * level of node).
 * 
 * @author seanh
 */
public class StoryCard extends PNode {
        
    public StoryCard(final PCamera cam) {
        super();
        final PNode layout_node = new PNode() {
            @Override
            public void layoutChildren() {
                double xOffset = 0;
                double yOffset = 0;
                Iterator i = getChildrenIterator();
                while (i.hasNext()) {
                    PNode each = (PNode) i.next();
                    each.setOffset(xOffset, yOffset - each.getY());
                    yOffset += each.getHeight();
                }
            }
        };
        addChild(layout_node);
        PNode title = new PText("Villainy!");
        layout_node.addChild(title);
        PNode image = new PImage("/home/seanh/NetBeansProjects/PiccoloExample/build/classes/piccoloexample/villainy.png");
        layout_node.addChild(image);
        PNode desc = new PText("The villain commits some\nheinous act of villainy.");
        layout_node.addChild(desc);
        layout_node.addInputEventListener(new PBasicInputEventHandler() {

            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getButton() == 1){
                    cam.animateViewToCenterBounds(layout_node.getGlobalFullBounds(), true, 1000);
                    event.setHandled(true);
                }
                else {
                    event.setHandled(false);
                }
            }

            @Override
            public void mouseDragged(PInputEvent event) {
                Dimension2D delta = event.getDeltaRelativeTo(layout_node);
                layout_node.translate(delta.getWidth(), delta.getHeight());
                event.setHandled(true);
            }
        });
    }
}