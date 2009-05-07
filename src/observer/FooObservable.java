package observer;

/**
 * A test class that implements observable.
 * 
 * @author seanh
 */
class FooObservable implements Observable<FooObservable> {

    private ConcreteObservable<FooObservable>
        obs = new ConcreteObservable<FooObservable>();

    private String state = "Initial state.";

    public void addObserver(Observer<FooObservable> o) {
        obs.addObserver(o);
    }
    
    public void removeObserver(Observer<FooObservable> o) {
        obs.removeObserver(o);
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
        obs.notify(this);
    }

}