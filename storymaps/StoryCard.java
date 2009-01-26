package storymaps;
import DragAndDrop.DragDropObserver;
import DragAndDrop.Draggable;
import DragAndDrop.NodeAlreadyDraggableException;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import java.io.Serializable;

public class StoryCard extends StoryCardBase {
            
    private boolean highlighted = false;
    private FunctionEditor editor;
    private Draggable draggable;
        
    public StoryCard(Function function) {
        this(function,"");
    }
    
    public StoryCard(Function function, String text) {

        super(function);
        
        background.addAttribute("StoryCard",this);
                                       
        background.addInputEventListener(new PBasicInputEventHandler() { 		        
            // Make the story card scale up when the mouse enters it, and down
            // again when the mouse leaves it.
            @Override
            public void mouseEntered(PInputEvent event) {
                StoryCard.this.highlight();
            }
            @Override
            public void mouseExited(PInputEvent event) {
                StoryCard.this.unhighlight();
            }    
            
            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getButton() == 1 && event.getClickCount() == 2) {
                    // If the StoryCard is double-clicked with the left mouse
                    // button send the "StoryCard double-clicked" message.
                    Messager m = Messager.getMessager();
                    m.send("StoryCard double-clicked", StoryCard.this);
                    goToHighDetail();
                    event.setHandled(true);
                } else if (event.getButton() == 1 && event.getClickCount() == 1) {
                    // If the StoryCard is single-clicked with the left mouse
                    // button send the "StoryCard single-clicked" message.
                    Messager.getMessager().send("StoryCard single-clicked", StoryCard.this);
                    event.setHandled(true);
                }
            }
        });
        
        editor = new FunctionEditor(function,text);
        
        try {
            draggable = new Draggable(background);            
        } catch (NodeAlreadyDraggableException e) {
            // ...
        }              
        
    }
        
    public void attach(DragDropObserver o) {
        draggable.attach(o);
    }
    
    public Draggable getDraggable() {
        return draggable;
    }
    
    public void highlight() {
        if (!highlighted) {
            double centerx = background.getX() + (background.getWidth()/2.0);
            double centery = background.getY() + (background.getHeight()/2.0);
            background.scaleAboutPoint(1.2, centerx, centery);
            highlighted = true;
        }
    }
    
    public void unhighlight() {
        if (highlighted) {
            double centerx = background.getX() + (background.getWidth()/2.0);
            double centery = background.getY() + (background.getHeight()/2.0);
            background.scaleAboutPoint(1.0/1.2, centerx, centery);
            highlighted = false;
        }
    }
                                 
    public FunctionEditor getEditor() {
        return editor;
    }
    
    @Override
    public String toString() {
        return function.toString() + "\n" + editor.getText();
    }
    
    public boolean compare(Object o) {
        if (!(o instanceof StoryCard)) {
            return false;            
        } else {
            StoryCard s = (StoryCard) o;
            if (!s.getEditor().getText().equals(editor.getText())) {
                return false;
            }
            if (!s.getFunction().compare(getFunction())) {
                return false;
            }
            return true;
        }
    }
    
    // Implement the Originator interface.

    private static class Memento implements Serializable {
        public Object function_memento;
        public Object editor_memento;
        public Memento(Object function_memento, Object editor_memento) {
            this.function_memento = function_memento;
            this.editor_memento = editor_memento;
        }
    }    
    
    /** Return a memento object for the current state of this StoryCard. */
    public Object saveToMemento() {
        Object function_memento = function.saveToMemento();
        Object editor_memento = editor.saveToMemento();
        return new Memento(function_memento,editor_memento);
    }

    /** 
     * Return a new StoryCard constructed from a memento object.
     */
    public static StoryCard newFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException("Argument not instanceof Memento.");
        }
        else {
            Memento m = (Memento) o;
            Function f = Function.newFromMemento(m.function_memento);
            FunctionEditor e = FunctionEditor.newFromMemento(m.editor_memento);
            return new StoryCard(f,e.getText());
        }
    }   
}