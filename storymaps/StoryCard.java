package storymaps;
import java.awt.Color;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PText;

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
     * Callback object for zooming to the card.
     */
    private final StoryCardDemo demo;
    
    /**
     * The root node of this StoryCard tree of nodes.
     */
    private PNode node;    
    
    public StoryCard(String title, String description, final StoryCardDemo demo) {
        this.title = title;
        this.description = description;
        this.demo = demo;
                
        node = PPath.createRectangle(-100, -120, 200, 240); // x,y,width,height
        node.setPaint(Color.WHITE);

        PText title_node = new PText(title);
        node.addChild(title_node);
        title_node.setOffset(-98,-120);
        title_node.setScale(2);
                
        PNode image_node = new PImage("/home/seanh/git/phd/storymaps_java/storymaps/home.png");
        node.addChild(image_node);
        image_node.setOffset(-98,-90);
        
        PText description_node = new PText(description);
        node.addChild(description_node);
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
                // Here need to send a message to the controlling application
                // somehow so it can decide to zoom the camera to this node with
                // a method call like:
                demo.getCanvas().getCamera().animateViewToCenterBounds(node.getGlobalBounds(), true, 500);
                event.setHandled(true);
            }
        });        
    }
    
    public PNode getNode() {
        return node;
    }
}