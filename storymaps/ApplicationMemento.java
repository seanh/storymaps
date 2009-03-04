package storymaps;

/**
 * An ApplicationMemento object represents a saved state to which the
 * application can be restored by calling Application.restoreFromMemento.
 * 
 * FIXME: This class needs to be immutable! Currently it only stores references
 * to the in-use object instances, and those object instances will change as the
 * user continues to use the application.
 * 
 * Right now it doesn't matter because all we do with ApplicationMemento objects
 * is create them and write them to file immediately then discard them, or read
 * them from file and use them to restore the application state immediately and
 * then discard them. But if they were kept in a list to implement undo, say,
 * then this mutability would be a problem.
 * 
 * Should make defensive copies of all the objects passed to the constructor,
 * and before returning objects in get methods. Ensure that no other class can
 * have a reference to any of the object instances used by a memento object so
 * that those object instances cannot change.
 * 
 * @author seanh
 */
final class ApplicationMemento {
    private final StoryMap story_map;
    private final StoryCards story_cards;
    
    // FIXME: to enforce immutability should make defensive copies of all
    // arguments.
    ApplicationMemento(StoryMap story_map, StoryCards story_cards) {
        this.story_map = story_map;
        this.story_cards = story_cards;
    }
    
    // FIXME: to enforce immutability should make a defensice copy of story_map.
    public StoryMap getStoryMap() {
        return story_map;
    }
    
    // FIXME: to enforce immutability should make a defensice copy of
    // story_cards.
    public StoryCards getStoryCards() {
        return story_cards;
    }
}