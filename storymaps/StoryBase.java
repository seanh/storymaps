package storymaps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;

/**
 * Base class for StoryMap and StoryCards.
 * 
 * @author seanh
 */
public class StoryBase {

    /**
     * The root node of the story base, the other major nodes are arranged in a
     * vertical line by this node.
     */
    private VerticalLayoutNode vnode;

    /**
     * The big rounded rectangle that acts as the colorful background and
     * droppable area of the story base.
     */
    protected PNode background;
    
    /**
     * The big title text above the story base
     */
    private PText title;

    /**
     * This node is used inside the story base to lay out the story cards (or
     * whatever) in a grid.
     */
    private GridLayoutNode grid;

    public StoryBase(String title_string) {

        vnode = new VerticalLayoutNode(10);                

        title = new PText(title_string);
        title.setScale(10);
        title.setPickable(false);
        vnode.addChild(title);
              
        background = PPath.createRoundRectangle(0, 0, 2200, 1000, 100, 100);
        background.setPaint(Color.MAGENTA);
        vnode.addChild(background);

        grid = new GridLayoutNode(10, 10);
        background.addChild(grid);
        grid.setOffset(50,50);
    }
    
    /**
     * Return the root node of this story base.
     */
    public PNode getNode() {
        return vnode;
    }
    
    /**
     * Add a pnode to this story base's grid.
     */
    public void add(PNode node) {
        grid.addChild(node);
    }        
}
