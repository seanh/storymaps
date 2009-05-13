package scratch;

import edu.umd.cs.piccolo.nodes.PPath;
import java.awt.Color;

/**
 * Base class for different types of story card.
 *
 * @author seanh
 */
public class StoryCard extends PNodeWrapper {

    private String title;

    public StoryCard(String title) {
        super(newRectangle());
        this.title = title;
    }

    /**
     * StoryCard exports the method it uses to make the rectangle-shaped
     * background of a story card so that other classes can make story card-
     * shaped objects.
     *
     * @return A rectangular story card shape.
     */
    public static PPath newRectangle() {
        PPath rectangle = PPath.createRoundRectangle(0, 0, 70, 82,8,8);
        Color color = new Color(1.0f,0.9f,0.6f);
        rectangle.setPaint(color);
        rectangle.setStrokePaint(color);
        return rectangle;
    }
}