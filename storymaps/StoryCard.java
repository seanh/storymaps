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
        
    private PNode background;
    private VerticalLayoutNode vnode;    
    private PText title_node;
    private PImage image_node;
    private PText description_node;
    
    public StoryCard(String title, String description) {
        this.title = title;
        this.description = description;
                
        background = PPath.createRectangle(0, 0, 200, 240); // x,y,width,height
        background.setPaint(Color.WHITE);

        vnode = new VerticalLayoutNode(10);
        vnode.setOffset(2,2);
        background.addChild(vnode);
        
        title_node = new PText(title);        
        title_node.setScale(2);
        vnode.addChild(title_node);
                
        image_node = new PImage("/home/seanh/git/phd/storymaps_java/storymaps/home.png");
        vnode.addChild(image_node);
        
        description_node = new PText(description);
        description_node.setConstrainWidthToTextWidth(false);
        description_node.setBounds(0,0,196,100);
                
        background.setChildrenPickable(false);  
        
        background.addInputEventListener(new PBasicInputEventHandler() { 		        
            // Make the story card scale up when the mouse enters it, and down
            // again when the mouse leaves it.
            @Override
            public void mouseEntered(PInputEvent event) {
                double centerx = background.getX() + (background.getWidth()/2.0);
                double centery = background.getY() + (background.getHeight()/2.0);
                background.scaleAboutPoint(1.2, centerx, centery);
            }
            @Override
            public void mouseExited(PInputEvent event) {
                double centerx = background.getX() + (background.getWidth()/2.0);
                double centery = background.getY() + (background.getHeight()/2.0);
                background.scaleAboutPoint(1.0/1.2, centerx, centery);
            }    
            
            // Make the camera zoom in on the story card when it's clicked.
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 1) {
                    Messager m = Messager.getMessager();
                    m.send("StoryCard clicked", StoryCard.this);                
                    vnode.addChild(description_node);
                    event.setHandled(true);
                }
            }
        });        
    }
    
    public PNode getNode() {
        return background;
    }    
}