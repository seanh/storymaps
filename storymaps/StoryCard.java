package storymaps;
import java.awt.Color;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Graphics2D;

/* TODO
 * 
 * Need a global singleton Mediator object:
 * 
 * - When a story card is clicked on it informs the mediator, which causes the
 *   camera to zoom in on that node.
 * - When the camera is zoomed out again the mediator is also informed, and can
 *   inform the node that it is no longer zoomed in on so that it can change its
 *   level of detail back down again.
 */

public class StoryCard {
    
    /**
     * The title that appears on this story card,
     */
    private String title;
    
    /**
     * The description that appears on this story card.
     */
    private String description;
        
    /**
     * The root node of this StoryCard tree of nodes.
     */
    private PNode node;    
    
    private PText title_node;
    private PImage image_node;
    private PText description_node;
    
    public StoryCard(String title, String description) {
        this.title = title;
        this.description = description;
                
        node = PPath.createRectangle(-100, -120, 200, 240); // x,y,width,height
        node.setPaint(Color.WHITE);

        title_node = new PText(title);
        node.addChild(title_node);
        title_node.setOffset(-98,-120);
        title_node.setScale(2);
                
        image_node = new PImage("/home/seanh/git/phd/storymaps_java/storymaps/home.png");
        node.addChild(image_node);
        image_node.setOffset(-98,-90);
        
        description_node = new PText(description);
        //node.addChild(description_node);
        description_node.setOffset(-98,0);
        description_node.setConstrainWidthToTextWidth(false);
        description_node.setBounds(0,0,196,100);
                
        node.setChildrenPickable(false);            
        
        node.addInputEventListener(new PBasicInputEventHandler() { 		        
            // Make the story card scale up when the mouse enters it, and down
            // again when the mouse leaves it.
            @Override
            public void mouseEntered(PInputEvent event) {
                node.setScale(1.2);
            }
            @Override
            public void mouseExited(PInputEvent event) {
                node.setScale(1);
            }    
            
            // Make the camera zoom in on the story card when it's clicked.
            @Override
            public void mousePressed(PInputEvent event) {
                Messager m = Messager.getMessager();
                m.send("StoryCard clicked", StoryCard.this);                
                node.addChild(description_node);
                event.setHandled(true);
            }
        });        
    }
    
    public PNode getNode() {
        return node;
    }    
}