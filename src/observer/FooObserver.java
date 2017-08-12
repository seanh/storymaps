
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

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