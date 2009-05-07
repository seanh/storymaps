package storymaps;

/**
 * The interface that must be implemented by any class that wants to receive
 * messages from the singleton Messager object.
 * 
 * @author seanh
 */
interface Receiver {    
    /**
     * This method is called by the messager when a message is sent with a name
     * that this receiver is subscribed to. Subclasses should override this
     * method to receive messages.
     * 
     * @param name The name of the message that was sent.
     * @param receiver_arg The argument that was passed to accept or acceptOnce
     *                     when subscribing to the message name.
     * @param sender_arg The argument that was passed to send by the object that
     *                   sent the message.
     */
    public void receive(String name, Object receiver_arg, Object sender_arg);
}
