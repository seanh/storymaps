package observer;

import java.util.Set;
import java.util.HashSet;

public class ConcreteObservable<T> implements Observable<T> {

    private Set<Observer<T>> observers = new HashSet<Observer<T>>();

    public void addObserver(Observer o) {
        observers.add(o);
    }
    
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    
    public void notify(T arg) {
        for (Observer<T> o : observers) {
            o.update(arg);
        }
    }
}