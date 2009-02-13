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

    private PText text;
    private static final String COLLAPSED_TEXT = "Write Story";
    private static final String UNCOLLAPSED_TEXT = "Go Back";
    
    public WriteStoryButton() {
        super("Write Story");
        text =  new PText("Write Story");
        text.setScale(25);
        this.addChild(text);
    }
    
    @Override
    protected void clicked() {
        super.clicked();
        if (text.getText().equals(COLLAPSED_TEXT)) {
            text.setText(UNCOLLAPSED_TEXT);
        } else {
            text.setText(COLLAPSED_TEXT);
        }
    }    
}