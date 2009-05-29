package storymaps;

import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Graphics2D;
import java.awt.Image;
import edu.umd.cs.piccolo.util.PBounds;

/**
 *
 * @author seanh
 */
public class SemanticImageNode extends PImage {

    private Image farImage;
    private Image nearImage;
    private double scale;

    SemanticImageNode(Image farImage, Image nearImage, double scale) {
        super(farImage);
        this.farImage = farImage;
        this.nearImage = nearImage;
        this.scale = scale;
    }

    Image getFarImage() {
        return farImage;
    }

    Image getNearImage() {
        return nearImage;
    }

    @Override
    public void paint(PPaintContext paintContext) {
        double s = paintContext.getScale();
        if (s < this.scale) {
            paintNode(paintContext, farImage);
        } else {
            paintNode(paintContext, nearImage);
        }
    }

    private void paintNode(PPaintContext paintContext, Image image) {
        double iw = image.getWidth(null);
        double ih = image.getHeight(null);
        PBounds b = getBoundsReference();
        Graphics2D g2 = paintContext.getGraphics();
        if (b.x != 0 || b.y != 0 || b.width != iw || b.height != ih) {
            g2.translate(b.x, b.y);
            g2.scale(b.width / iw, b.height / ih);
            g2.drawImage(image, 0, 0, null);
            g2.scale(iw / b.width, ih / b.height);
            g2.translate(-b.x, -b.y);
        } else {
            g2.drawImage(image, 0, 0, null);
        }
    }
}