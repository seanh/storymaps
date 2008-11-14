package storymaps;
import java.awt.Color;
import edu.umd.cs.piccolo.PNode;
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
     * The root node of this StoryCard tree of nodes.
     */
    private PNode node;
    
    public StoryCard(String title, String description) {
        this.title = title;
        this.description = description;
                
        node = PPath.createRectangle(0, 0, 200, 240);
        node.setPaint(Color.WHITE);

        PText title_node = new PText(title);
        node.addChild(title_node);
        title_node.setOffset(2,0);
        title_node.setScale(2);
                
        PNode image_node = new PImage("/home/seanh/git/phd/storymaps_java/storymaps/home.png");
        node.addChild(image_node);
        image_node.setOffset(2,30);
        
        PText description_node = new PText(description);
        node.addChild(description_node);
        description_node.setOffset(2,120);
        description_node.setConstrainWidthToTextWidth(false);
        description_node.setBounds(0,0,196,100);
                
        node.setChildrenPickable(false);                        
    }
    
    public PNode getNode() {
        return node;
    }
}