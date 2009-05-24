package storymaps;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Random;
import java.awt.Dimension;


/**
 * A demo class for the CentreAlignedRowsLayoutNode class.
 *
 * @author seanh
 */
public class CentreAlignedRowsLayoutNodeDemo extends PFrame {

    private CentreAlignedRowsLayoutNode layout;

    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        layout = new CentreAlignedRowsLayoutNode(5);
        layout.setBounds(20,20,600,440);
        Random random = new Random();
        for (int i=0; i<37; i++) {
            float x = (random.nextFloat() - 0.5f) * 50;
            float y = (random.nextFloat() - 0.5f) * 40;
            PPath each = PPath.createRectangle(0, 0, 100+x, 80+y);
            each.setPaint(new Color(random.nextFloat(),random.nextFloat(),
                                    random.nextFloat()));
            each.setStroke(new BasicStroke(10));
            each.setStrokePaint(new Color(random.nextFloat(),random.nextFloat(),
                                          random.nextFloat()));
            layout.addChild(each);
        }
        PCanvas canvas = getCanvas();
        canvas.setPreferredSize(new Dimension(640,480));
        canvas.getLayer().addChild(layout);
        final PCamera cam = canvas.getCamera();
        cam.setViewBounds(layout.getGlobalFullBounds());
    }

    public static void main(String args[]) {
        CentreAlignedRowsLayoutNodeDemo demo = new CentreAlignedRowsLayoutNodeDemo();
    }
}

