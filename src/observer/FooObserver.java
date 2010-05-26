package observer;

/**
 * A test class that subscribes to a FooObservable. By using an anonymous inner
 * class as the observer a different method of FooObserver can be called for
 * each observable that it observes.
 * 
 * @author seanh
 */
class FooObserver {

    private FooObservable observable;
    
    FooObserver(FooObservable observable) {
        this.observable = observable;
        observable.addObserver(new Observer<FooObservable>() {
            public void update(FooObservable observable) {
                fooUpdate(observable);
            }
            
        });
    }
    
    public void fooUpdate(FooObservable observable) {
        System.out.println("This is the update method of FooObserver "+this);
        System.out.println("The state of my FooObservable "+observable+" is "+observable.getState());
    }
}