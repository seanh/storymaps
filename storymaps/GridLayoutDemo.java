package storymaps;
import edu.umd.cs.piccolox.PFrame;

/**
 * A demo class for the GridLayoutNode class.
 * 
 * @author seanh
 */
public class GridLayoutDemo extends PFrame implements Receiver {

    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        GridLayoutNode grid = new GridLayoutNode(10,10);
        for (int i=0; i<30; i++) {
            StoryCard card = new StoryCard("Argh", "Aaaargh!");
            grid.addChild(card.getNode());
        }
        getCanvas().getLayer().addChild(grid);
    }
    
    public static void main(String args[]) {
        GridLayoutDemo demo = new GridLayoutDemo();
    }
    
     public void receive(String name, Object receiver_arg, Object sender_arg) {

     }
}

