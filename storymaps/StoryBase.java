package storymaps;

import DragAndDrop.*;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;

/**
 * Base class for StoryMap and StoryCards. Composes a number of nodes:
 * 
 * vnode   The root node of the StoryBase, a VerticalLayoutNode.
 * |
 * |-> title   A PText node, displays a text title above the StoryBase. Edit
 * |           with setTitle and getTitle.
 * |
 * |-> background   A colored, rounded rectangle (PPath) that visually
 *     |            represents the background of the StoryBase, and also acts as
 *     |            a droppable node for the StoryBase.
 *     |
 *     |-> grid   A GridLayoutNode to which nodes can be added, they will be
 *     |          arranged into a neat grid inside the rounded rectangle. Add a
 *     |          PNode to the grid node with addToGrid.
 *     |
 *     |-> overlay Any nodes added to this node appear on top of any nodes added
 *                 to the grid node. grid and overlay have the same parent node
 *                 and the same local transform, so if a child node of the
 *                 overlay node has the same local transform as a child node of
 *                 the grid node, then it will appear exactly on top of the
 *                 child of the grid node.
 * 
 *                 Add a PNode to the overlay node with addToOverlay.
 * 
 * @author seanh
 */
public class StoryBase implements DroppableOwner {

    /**
     * The root node of the story base, the other major nodes are arranged in a
     * vertical line by this node.
     */
    private VerticalLayoutNode vnode;

    /**
     * The big rounded rectangle that acts as the colorful background and
     * droppable area of the story base.
     */
    private PNode background;
    
    /**
     * The big title text above the story base
     */
    private PText title;

    /**
     * This node is used inside the story base to lay out the story cards (or
     * whatever) in a grid.
     */
    private GridLayoutNode grid;

    /**
     * Any nodes added to this node appear on top of any nodes added to the
     * grid node.
     */
    private PNode overlay;
    
    protected Droppable droppable;
    
    public StoryBase(String title_string) {

        vnode = new VerticalLayoutNode(10);                

        title = new PText(title_string);
        title.setScale(7.3);
        title.setPickable(false);
        vnode.addChild(title);
              
        background = PPath.createRoundRectangle(0, 0, 2200, 1000, 100, 100);
        background.setPaint(Color.MAGENTA);
        vnode.addChild(background);

        try {
            droppable = new Droppable(background,this);            
        } catch (NodeAlreadyDroppableException e) {
            // ...
        }        
        
        grid = new GridLayoutNode(10, 10);
        background.addChild(grid);
        int x = 50;
        int y = 50;
        grid.setOffset(x,y);
        
        overlay = new PNode();
        background.addChild(overlay);
        overlay.setOffset(x,y);
    }
    
    /**
     * A node has been dragged and dropped onto this story base. Just refuse
     * the drop. Subclasses should override this method.
     */
    public boolean dropped_onto(DropEvent de) {
        return false;
    }
    
    /**
     * Return the root node of this story base.
     */
    public PNode getNode() {
        return vnode;
    }
    
    /**
     * Return the title (String) of this story base.
     */
    public String getTitle() {
        return title.getText();
    }
    
    /**
     * Set the title of this story base.
     */
    public void setTitle(String title_text) {
        title.setText(title_text);
    }
    
    /**
     * Add a node to the grid.
     */
    public void addToGrid(PNode node) {
        grid.addChild(node);
        grid.layoutChildren();
    }
    
    /**
     * Add a node to the overlay.
     */
    public void addToOverlay(PNode node) {
        overlay.addChild(node);
    }
}