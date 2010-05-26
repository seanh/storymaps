package scratch;

/**
 * An invisible story card-shaped object that holds a position onto which a
 * story card can be placed.
 *
 * @author seanh
 */
public class Placeholder extends PNodeWrapper {

    public Placeholder() {
        super(StoryCard.newRectangle());
    }
}
