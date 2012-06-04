
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
 * Class that handles templating via FreeMarker.
 * 
 * @author seanh
 */
final class TemplateHandler {

    /**
     * The FreeMarker configuration instance.
     */
    private final Configuration cfg = new Configuration();

    /**
     * Construct a new TemplateHandler instance using the default templates
     * directory.
     *
     * @throws java.io.IOException
     */
    TemplateHandler() {
        this("/data/templates");
    }

    TemplateHandler(String templateDir) {
        cfg.setClassForTemplateLoading(Util.class, templateDir);
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    String renderMap(Map m, String template_filename) throws IOException, TemplateHandlerException {
        Template temp = null;
        try {
            temp = cfg.getTemplate(template_filename);
        } catch (IOException e) {
            throw new IOException("IOException when configuring template "+template_filename,e);
        }
        StringWriter out = new StringWriter();
        try {
            temp.process(m,out);
        } catch (TemplateException e) {
            String detail = "FreeMarker TemplateException when rendering template "+template_filename+" with contents "+m;
            throw new TemplateHandlerException(detail, e);
        } catch (IOException e) {
            throw new IOException("IOException when rendering template "+template_filename+" with contents "+m,e);
        }
        out.flush();
        return out.toString();
    }

    // FIXME: This method seems like it belong in the StoryMap class. Perhaps it
    // could be combined with the Memento pattern.
    /**
     * Render a StoryMap rendered as HTML.
     * 
     * @param m The StoryMap to be rendered
     * @param files_dir The path to the directory where the story map's images
     * are stored. Image URLs in the HTML will be prefixed with this string.
     * @return The rendered StoryMap (String)
     */
    String renderStoryMap(StoryMap m, String filesPath) throws IOException, TemplateHandlerException {
        Map root = new HashMap();
        root.put("filesPath",filesPath);
        Map storyMap = new HashMap();
        root.put("StoryMap", storyMap);
        storyMap.put("title", m.getEditor().getTitle());
        List storyCards = new ArrayList<Map>();
        for (int i=0; i<m.getStoryCards().size(); i++) {
            StoryCard c = m.getStoryCards().get(i);
            Map function = new HashMap();
            function.put("name", c.getFunction().getName());
            function.put("description", c.getFunction().getDescription());
            function.put("instructions", c.getFunction().getInstructions());
            Map storyCard = new HashMap();
            storyCard.put("number",i);
            storyCard.put("Function",function);            
            storyCard.put("text",c.getEditor().getTextAsHTML());
            storyCard.put("imageFile",Util.joinClassPaths(filesPath,c.getFunction().getImageFilename()));
            storyCards.add(storyCard);
        }
        storyMap.put("storyCards", storyCards);
        return renderMap(root, "story.ftl");
    }

    /**
     * Render the list of functions using the functions.ftl template and return
     * the result.
     *
     * @return The rendered functions
     */
    String renderFunctions() throws IOException, TemplateHandlerException {
        Template temp = null;
        Map root = new HashMap();
        List functions = new ArrayList();
        for (Function f : Function.getFunctions()) {
            Map fmap = new HashMap();
            fmap.put("number",f.getNumber());
            fmap.put("name",f.getName());
            fmap.put("description",f.getDescription());
            fmap.put("instructions",f.getInstructions());
            functions.add(fmap);
        }
        root.put("functions", functions);
        return renderMap(root,"functions.ftl");
    }
}