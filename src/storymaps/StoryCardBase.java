package storymaps;
import storymaps.ui.Fonts;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.Color;

/**
 * Absract base class for StoryCard and DisabledStoryCard.
 * @author seanh
 */
abstract class StoryCardBase {
    
    // This story card's Propp function.
    private Function function;
    
    // Root node of the story card, to which the card and shadow nodes are
    // attached.
    private PNode root = new PNode();

    // The card-shaped background geometry of the story card.
    private PPath card = newRectangle();

    // The story card's drop-shadow.
    protected PPath shadow = newRectangle();
    
    // A story card has two levels of detail, high and low. When in high detail
    // highDetailNode is attached to the card node, when in low detail
    // lowDetailNode is attached.    
    private VerticalLayoutNode highDetailNode;
    private VerticalLayoutNode lowDetailNode;

    /**
     * StoryCardBase exports the method it uses to make the story card shapes
     * so that other classes can make story card-shaped objects.
     *
     * @return A rounded rectangle story card shape.
     */
    public static PPath newRectangle() {
        PPath rectangle = PPath.createRoundRectangle(-24.5f,-33.5f,49,67,6,6);
        Color color = new Color(1.0f,0.9f,0.6f);
        rectangle.setPaint(color);
        rectangle.setStrokePaint(color);
        return rectangle;
    }

    StoryCardBase(Function function) {

        this.function = function;

        root.addAttribute("StoryCardBase",this);
        root.addChild(shadow);
        shadow.setPaint(Color.BLACK);
        shadow.setStrokePaint(Color.BLACK);
        shadow.setOffset(1,1);
        shadow.setTransparency(0.5f);
        shadow.setPickable(false); // The shadow node is what the user actually picks with the mouse.

        card.setPickable(false);
        root.addChild(card);

        // The root node (and not the card node or shadow node or any of their
        // children) needs to be the pickable node so that the card and shadow
        // move together when clicked and dragged. But the root node has no
        // geometry of its own and therefore no bounds. So we give it the card
        // node's bounds.
        // Note: we're ignoring coordinate space translations here. It works,
        // but only because the card node has no transform of its own.
        root.setPickable(true);
        root.setBounds(card.getBounds());

        highDetailNode = new VerticalLayoutNode(5);
        lowDetailNode = new VerticalLayoutNode(5);
        lowDetailNode.setScale(.5);
        lowDetailNode.setOffset(
                -0.5*card.getWidth()+0.05*card.getWidth(),
                -0.5*card.getHeight()+0.05*card.getHeight());
        highDetailNode.setScale(.5);
        highDetailNode.setOffset(
                -0.5*card.getWidth()+0.05*card.getWidth(),
                -0.5*card.getHeight()+0.05*card.getHeight());

        PImage lowDetailImage = new PImage(function.getImage());
        lowDetailImage.setScale(.92);
        lowDetailNode.addChild(lowDetailImage);

        PImage highDetailImage = new PImage(function.getImage());        
        highDetailImage.setScale(.6);
        highDetailNode.addChild(highDetailImage);

        HTMLNode descriptionNode = new HTMLNode(function.getDescription());
        descriptionNode.setBounds(0,0,card.getWidth()*4,card.getHeight());
        descriptionNode.setFont(Fonts.NORMAL);
        descriptionNode.scale(.5);
        highDetailNode.addChild(descriptionNode);

        PText lowDetailTitle = makeTitleNode();
        lowDetailTitle.setOffset(0.5*(card.getWidth()-lowDetailTitle.getWidth()),0);
        lowDetailNode.addChild(lowDetailTitle);

        //PText highDetailTitle = makeTitleNode();
        //highDetailNode.addChild(highDetailTitle);

        lowDetailNode.setPickable(false);
        lowDetailNode.setChildrenPickable(false);
        highDetailNode.setPickable(false);
        highDetailNode.setChildrenPickable(false);
        
        goToLowDetail();
    }

    private PText makeTitleNode() {
        PText titleNode = new PText(function.getName());        
        // The font size is really set by the scale of the node, not by font.        
        titleNode.setFont(Fonts.SMALL);
        titleNode.setConstrainWidthToTextWidth(false);
        // Uncomment this line to clip the text if its height goes beyond the
        // bounds.
        //titleNode.setConstrainHeightToTextHeight(false);
        titleNode.setBounds(0, 0, 70, 10);
        return titleNode;
    }
               
    // FIXME: should use a state-machine for these state changes.
    protected void goToHighDetail() {
        lowDetailNode.removeFromParent();
        card.addChild(highDetailNode);
    }
    
    void goToLowDetail() {
        highDetailNode.removeFromParent();
        card.addChild(lowDetailNode);
    }    

    protected PNode getNode() {return root;}

    //PNode getCard() { return card; }
    
    String getTitle() { return function.getName(); }
    
    String getDescription() { return function.getDescription(); }
    
    Function getFunction() { return function; }
}