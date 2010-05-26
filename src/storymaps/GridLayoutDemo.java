package storymaps;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Random;

/**
 * A demo class for the GridLayoutNode class.
 * 
 * @author seanh
 */
public class GridLayoutDemo extends PFrame {

    private GridLayoutNode grid;
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        grid = new GridLayoutNode(5,10);
        Random random = new Random();
        for (int i=0; i<25; i++) {
            PPath each = PPath.createRectangle(0, 0, 100, 80);
            each.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                    random.nextFloat()));
            each.setStroke(new BasicStroke(10));
            each.setStrokePaint(new Color(random.nextFloat(),random.nextFloat(),
                                          random.nextFloat()));
            grid.addChild(each);
        }
        getCanvas().getLayer().addChild(grid);
    }
    
    public static void main(String args[]) {
        GridLayoutDemo demo = new GridLayoutDemo();
    }    
}

