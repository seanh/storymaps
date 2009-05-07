package observer;

class Main {

    public static void main(String[] args) {
        FooObservable observable = new FooObservable();
        FooObserver observer = new FooObserver(observable);
        observable.setState("New state");    
    }

}