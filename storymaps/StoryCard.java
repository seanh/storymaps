package storymaps;
import DragAndDrop.DragDropObserver;
import DragAndDrop.Draggable;
import DragAndDrop.NodeAlreadyDraggableException;
import java.awt.Color;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PText;

public class StoryCard {
            
    private Function function;
    private PNode background;
    private VerticalLayoutNode vnode;    
    private PText title_node;
    private PImage image_node;
    private PText description_node;
    private boolean disabled = false;
    private boolean highlighted = false;
    private FunctionEditor editor;
    private Draggable draggable;

    private class Memento {
        public Object function_memento;
        public Object editor_memento;
        public Memento(Object function_memento, Object editor_memento) {
            this.function_memento = function_memento;
            this.editor_memento = editor_memento;
        }
    }
    
    /**
     * Copy constructor.
     * @return A new StoryCard that is a copy of this one.
     */
    public StoryCard copy() {
        return new StoryCard(function);
    }
    
    public StoryCard(Function function) {
        this(function,"");
    }
    
    public StoryCard(Function function, String text) {

        this.function = function;
        
        background = PPath.createRoundRectangle(0, 0, 200, 240,20,20);
        background.setPaint(Color.WHITE);        
        background.addAttribute("StoryCard",this);
                               
        vnode = new VerticalLayoutNode(10);
        vnode.setOffset(2,2);
        background.addChild(vnode);
        
        title_node = new PText(function.getFriendlyName());        
        title_node.setScale(2);
        vnode.addChild(title_node);
                
        image_node = new PImage(function.getImage());
        vnode.addChild(image_node);
        
        description_node = new PText(function.getFriendlyDescription());
        description_node.setConstrainWidthToTextWidth(false);
        description_node.setBounds(0,0,196,100);
                
        background.setChildrenPickable(false);
        
        background.addInputEventListener(new PBasicInputEventHandler() { 		        
            // Make the story card scale up when the mouse enters it, and down
            // again when the mouse leaves it.
            @Override
            public void mouseEntered(PInputEvent event) {
                if (!StoryCard.this.disabled) {
                    StoryCard.this.highlight();
                }
            }
            @Override
            public void mouseExited(PInputEvent event) {
                if (!StoryCard.this.disabled) {
                    StoryCard.this.unhighlight();                }
            }    
            
            // Make the camera zoom in on the story card when double-clicked.
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 1 && event.getClickCount() == 2) {
                    Messager m = Messager.getMessager();
                    m.send("StoryCard clicked", StoryCard.this);                
                    vnode.addChild(description_node);
                    event.setHandled(true);
                } else if (event.getButton() == 1 && event.getClickCount() == 1) {
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
                     
    public void disable() {
        if (!disabled) {
            disabled = true;
            background.setTransparency(.3f);
            image_node.setTransparency(.3f);
            background.setPickable(false);
        }
    }

    public void enable() {
        if (disabled) {
            disabled = false;
            background.setTransparency(1);
            image_node.setTransparency(1);
            background.setPickable(true);
        }
    }
        
    public PNode getNode() {
        return background;
    }
    
    public String getTitle() {
        return title_node.getText();
    }
    
    public String getDescription() {
        return description_node.getText();
    }
    
    public Function getFunction() {
            return function;
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