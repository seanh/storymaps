package observer;

public interface Observable<T> {
    public void addObserver(Observer<T> o);
    public void removeObserver(Observer<T> o);
}