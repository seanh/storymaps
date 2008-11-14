package storymaps;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Passive helper class for Messager. A Subscription object just wraps a
 * Receiver object and an receiver's argument Object in one class.
 * 
 * @author seanh
 */
class Subscription {
    private Receiver r;
    private Object arg;
    public Subscription(Receiver r, Object arg) {
        this.r =r;
        this.arg = arg;
    }
    public Receiver getReceiver() {
        return r;
    }
    public Object getArg() {
        return arg;
    }
    @Override
    public String toString() {
        String s = "Receiver: " + r + "\n";
        s = s + "  receiver's arg: " + arg;
        return s;
    }
}

/**
 * An incredibly useful (if not very java-like) singleton message-passing class.
 * 
 * @author seanh
 */
final class Messager { // Class is final so it can't be subclassed.
    
    /**
     * The single object-instance of messager.
     */
    private static Messager m = new Messager();

    /**
     * A HashMap that maps message names (Strings) to lists of Subscription
     * objects. Every time a Receiver subscribes to a message name using the
     * accept method a Subscription object is added to the appropriate list.
     */
    private HashMap<String,ArrayList<Subscription>> subscriptions
            = new HashMap<String,ArrayList<Subscription>>();

    /**
     * Same as the above field subscriptions, but for the one-time subscriptions
     * made using the acceptOnce method.
     */
    private HashMap<String,ArrayList<Subscription>> one_time_subscriptions
            = new HashMap<String,ArrayList<Subscription>>();
    
    /**
     * If verbose, the messager will do a System.out.println whenever it
     * receives a message or a message subscription. For debugging.
     */
    private boolean verbose = false;
        
    private Messager() { // Constructor is private, no other class can use it.
        
    }
    
    /**
     * Get the singleton Mediator instance.
     * 
     * @return the singleton Mediator instance.
     */
    public static Messager getMessager() {
        return m;
    }
    
    /**
     * Send a message with the given name and the given argument, all receivers
     * subscribed to the message name will be notified and passed the message
     * argument.
     * 
     * @param name  The name of the message to send
     * @param arg   The object to pass to the receive methods as sender_arg 
     */
    public void send(String name, Object arg) {        
        if (verbose) {
            System.out.println("Sending message: " + name);
        }
        
        if (subscriptions.containsKey(name)) {        
            for (Subscription s : subscriptions.get(name)) {
                Receiver r = s.getReceiver();
                r.receive(name,s.getArg(),arg);
                if (verbose) {
                    System.out.println("   Sent to " + r);
                }
            }
        }
        
        if (one_time_subscriptions.containsKey(name)) {        
            for (Subscription s : one_time_subscriptions.get(name)) {
                Receiver r = s.getReceiver();
                r.receive(name,s.getArg(),arg);
                if (verbose) {
                    System.out.println("   Sent to " + r + " (one time)");
                }
            }
            one_time_subscriptions.remove(name);
        }        
    }
    
    /**
     * Register with the messager to receive messages with the given name, the
     * messager will call the receiver's receive method to notify of each 
     * message sent.
     * 
     * @param name  The message name to subscribe to
     * @param r     The receiver to subscribe
     * @param arg   The object to pass to the receive method as receiver_arg
     */
    public void accept(String name, Receiver r, Object arg) {
        if (!subscriptions.containsKey(name)){
            subscriptions.put(name,new ArrayList<Subscription>());
        }
        Subscription s = new Subscription(r,arg);
        subscriptions.get(name).add(s);
        if (verbose) {
            System.out.println(r + " subscribed to event " + name + " with argument " + arg);
        }
    }

    /**
     * Register with the messager to receive the next message only with the
     * given name, the messager will call the receiver's receive method to
     * notify when the message is sent.
     * a string listing all of the subscriptions held by the messager.
     * @param name  The message name to subscribe to
     * @param r     The receiver to subscribe
     * @param arg   The object to pass to the receive method as receiver_arg
     */
    public void acceptOnce(String name, Receiver r, Object arg) {
        if (!one_time_subscriptions.containsKey(name)){
            one_time_subscriptions.put(name,new ArrayList<Subscription>());
        }
        Subscription s = new Subscription(r,arg);
        one_time_subscriptions.get(name).add(s);
        if (verbose) {
            System.out.println(r + " subscribed to event " + name + " with argument " + arg + " once only ");
        }
    }
 
    /**
     * Remove all subscriptions made by the given receiver object to the given
     * message name.
     * 
     * @param name  The message name to unsubscribe from
     * @param r     The receiver to unsubscribe
     */
    public void ignore(String name, Receiver r) {        
        if (subscriptions.containsKey(name)) {
            ArrayList<Subscription> new_subscriptions = 
                                                  new ArrayList<Subscription>();
            for (Subscription s : subscriptions.get(name)) {
                if (!(s.getReceiver() == r)) {
                    new_subscriptions.add(s);
                }
            }
            // Replace the old list of subscriptions for name with the new one.
            subscriptions.put(name,new_subscriptions);
        }
    }

    /**
     * Remove all subscriptions made by the given receiver object.
     * 
     * @param r     The receiver to unsubscribe
     */
    public void ignoreAll(Receiver r) {
        // TODO.
    }
    
    /**
     * Clear all subscriptions with the messager.
     */
    public void clear() {
        subscriptions = new HashMap<String,ArrayList<Subscription>>();
        one_time_subscriptions = new HashMap<String,ArrayList<Subscription>>();
    }
    
    /**
     * Return a string listing all of the subscriptions held by the messager.
     * 
     * @return a string listing all of the subscriptions held by the messager.
     */
    @Override
    public String toString() {
        String s = "Subscriptions:\n";
        s = s + subscriptions + "\n";
        s = s + "One-time subscriptions:\n";
        s = s + one_time_subscriptions + "\n";
        return s;
    }
}