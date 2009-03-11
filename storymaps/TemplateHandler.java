package storymaps;

import freemarker.template.*;
import java.io.*;
import java.util.*;


/**
 * If FreeMarker throws its TemplateException then we wrap it in one of these
 * and throw it on.
 * 
 * @author seanh
 */
class TemplateHandlerException extends Exception {
    TemplateHandlerException(String detail, TemplateException e) {
        super(detail,e);
    }
}
/**
 * Singleton that handles templating via FreeMarker.
 * 
 * @author seanh
 */
final class TemplateHandler {

    /**
     * The FreeMarker configuration instance.
     */
    private static final Configuration cfg = new Configuration();    
    
    /**
     * The Singleton instance of this class.
     */
    private static TemplateHandler INSTANCE = null;
    
    /**
     * Return the singleton instance of this class.
     */
    static TemplateHandler getInstance() throws IOException {
        if (INSTANCE == null) {
             INSTANCE = new TemplateHandler();
        }
        return INSTANCE;
    }

    /**
     * Private constructor prevents instantiation from outside this class.
     */
    private TemplateHandler() throws IOException {
        try {
            String templates_path = getClass().getResource("/storymaps/templates").getPath();
            cfg.setDirectoryForTemplateLoading(new File(templates_path));
            cfg.setObjectWrapper(new DefaultObjectWrapper());            
        } catch (IOException e) {
            String detail = "IOException when configuring template engine.";
            Util.reportException(detail, e);
            throw new IOException(detail,e);
        }               
    }
    
    /**
     * Render a StoryMap instance using the story.ftl template and return the
     * result.
     * 
     * @param m The StoryMap to be rendered
     * @return The rendered StoryMap (String)
     */
    String renderStoryMap(StoryMap m) throws IOException, TemplateHandlerException {
        Map root = new HashMap();
        Map storyMap = new HashMap();
        root.put("StoryMap", storyMap);
        storyMap.put("title", m.getEditor().getTitle());
        List storyCards = new ArrayList<Map>();
        for (StoryCard c : m.getStoryCards()) {
            Map function = new HashMap();
            function.put("name", c.getFunction().getName());
            Map storyCard = new HashMap();
            storyCard.put("Function",function);            
            storyCard.put("text",c.getEditor().getTextAsHTML());
            storyCards.add(storyCard);
        }
        storyMap.put("storyCards", storyCards);

        Template temp = null;
        String template_filename = "story.ftl";
        try {
            temp = cfg.getTemplate(template_filename);
        } catch (IOException e) {
            throw new IOException("IOException when configuring template "+template_filename,e);
        }
        StringWriter out = new StringWriter();
        try {
            temp.process(root,out);
        } catch (TemplateException e) {
            String detail = "FreeMarker TemplateException when rendering template "+template_filename+" with contents "+root;
            throw new TemplateHandlerException(detail, e);
        } catch (IOException e) {
            throw new IOException("IOException when rendering template "+template_filename+" with contents "+root,e);
        }
        out.flush();
        return out.toString();
    }        
    
    /**
     * Runs a simple test of FreeMarker templater rendering, renders
     * templates/test.ftl and prints out the result.
     */
    public static void main(String[] args) {
        Map root = new HashMap();
        root.put("user","Big Joe");
        Template temp = null;
        try {
            temp = cfg.getTemplate("test.ftl");
        } catch (IOException e) {
            System.out.println("IOException when loading template test.ftl "+e);
            return;
        }
        StringWriter out = new StringWriter();
        try {
            temp.process(root,out);
            out.flush();
            System.out.println(out.toString());
        } catch (TemplateException e) {
            System.out.println("TemplateException when rendering template test.ftl" +e);
            return;
        } catch (IOException e) {
            System.out.println("IOException when rendering template test.ftl" +e);            
            return;
        }
    }
}