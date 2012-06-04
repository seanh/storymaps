
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@lavabit.com>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
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