package storymaps.ui;

import edu.umd.cs.piccolo.nodes.PText;

/**
 * A button named "Write Story" that displays some text and changes between two
 * states as it's clicked.
 * 
 * FIXME: implement a generic two-state button superclass.
 * 
 * @author seanh
 */
public class WriteStoryButton extends Button {

    private static final String COLLAPSED_TEXT = "Write Story";
    private static final String UNCOLLAPSED_TEXT = "Go Back";
    
    public WriteStoryButton() {
        super("Write Story");
        setScale(10);
    }
    
    @Override
    protected void clicked() {
        super.clicked();
        if (getText().equals(COLLAPSED_TEXT)) {
            setText(UNCOLLAPSED_TEXT);
        } else {
            setText(COLLAPSED_TEXT);
        }
    }    
}