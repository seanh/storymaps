package storymaps;
import DragAndDrop.DragDropObserver;
import DragAndDrop.Draggable;
import DragAndDrop.DropEvent;
import DragAndDrop.NodeAlreadyDraggableException;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PInterpolatingActivity;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PUtil;

class StoryCard extends StoryCardBase implements Receiver,
        DragDropObserver, Comparable, Originator {
            
    private boolean highlighted = false;
    private FunctionEditor editor;
    private Draggable draggable;
    private boolean dragging = false;
    
    /**
     * The activity used for both scaling up and scaling down the story card
     * to highlight and unhighlight it.
     */
    private PInterpolatingActivity activity;
    
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
                /*
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
                }*/
            }
        });
        
        editor = new FunctionEditor(function,text);
        
        try {
            draggable = new Draggable(background);
            draggable.attach(this);
        } catch (NodeAlreadyDraggableException e) {
            // ...
        }       
        
        Messager.getMessager().accept("drag started", this, null);
    }
        
    public void attach(DragDropObserver o) {
        draggable.attach(o);
    }
    
    public Draggable getDraggable() {
        return draggable;
    }
                
    /**
     * Start an activity that smoothly scales the story card over time.
     * 
     * Starts a PInterpolatingActivity and stores it in this.activity. Scales
     * the story card by scaling its background node.
     * 
     * @param dest The value to scale up or down to.
     */
    private void smoothlyScale(final float dest) {
        // First make sure no other scale up or scale down activity is running.
        if (activity != null) {
            activity.terminate();
        }
        
        int duration = 150;
        int delay = 50;
	activity = new PInterpolatingActivity(
            duration,
            PUtil.DEFAULT_ACTIVITY_STEP_RATE,
            delay + System.currentTimeMillis(),
            1, // Number of times the activity should loop before ending            
            PInterpolatingActivity.SOURCE_TO_DESTINATION
        ){
            // Override some of PInterpolatingActivity's methods to make
            // something actually happen as the activity runs.
            private float source;

            /**
             * Called before the activity is scheduled to start running.
             */ 
            @Override
            protected void activityStarted() {
                source = (float)background.getScale();
                super.activityStarted();
            }
            /**
             * Called to set the target value at each step of the activity.
             */
            @Override
            public void setRelativeTargetValue(float scale) {
                float scaleTo = source + (scale * (dest - source));
                background.setScale(scaleTo);
            }
        };
        background.addActivity(activity);
    }
        
    public void highlight() {
        if (!highlighted && !dragging) {
            getNode().reparent(getNode().getParent());
            highlighted = true;
            smoothlyScale(1.4f);
        }
    }
    
    public void unhighlight() {
        if (highlighted && !dragging) {
            highlighted = false;
            smoothlyScale(1.0f);
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
    
    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("drag started")) {
            if (sender_arg instanceof PNode) {
                PNode node = (PNode) sender_arg;
                if (node.equals(background)) {
                    if (activity != null) {
                        activity.terminate();
                    }
                    background.setScale(1.0);
                    dragging = true;
                }
            }
        }
    }
    
    public boolean notify(DropEvent de) {
        dragging = false;
        return true;
    }

    public int compareTo(Object arg) {
        StoryCard s = (StoryCard) arg;
        return s.getFunction().compareTo(this.getFunction());
    }

    // Implement Originator
    // --------------------
    
    private static final class StoryCardMemento implements Memento {
        // No need to defensively copy anything as strings are immutable and
        // FunctionMemento should be immutable.
        private final Memento functionMemento;
        private final String userText;
        StoryCardMemento (StoryCard sc) {
            this.functionMemento = sc.getFunction().createMemento();
            this.userText = sc.getEditor().getText();
        }
        Memento getFunctionMemento() { return functionMemento; }
        String getUserText() { return userText; }
    }
    
    public Memento createMemento() {
        return new StoryCardMemento(this);
    }
    
    public static StoryCard newInstanceFromMemento(Memento m) throws MementoException {
        if (m == null) {
            String detail = "Null memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        if (!(m instanceof StoryCardMemento)) {
            String detail = "Wrong type of memento object.";
            MementoException e = new MementoException(detail);
            Util.reportException(detail, e);
            throw e;
        }
        StoryCardMemento scm = (StoryCardMemento) m;
        Function f = Function.newInstanceFromMemento(scm.getFunctionMemento());
        return new StoryCard(f,scm.getUserText());
    }
}