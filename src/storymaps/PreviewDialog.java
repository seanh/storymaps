package storymaps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author seanh
 */
public class PreviewDialog {

    private final JFrame frame;
    private final JDialog dialog;
    private final JEditorPane editor;
    private final JToolBar toolBar;
    private final Logger logger;
    private final JFileChooser fileChooser = new JFileChooser();
    private StoryMap map;

    /**
     * Construct (but do not yet show) a new PreviewDialog.
     *
     * @param frame The JFrame to which this dialog should belong.
     */
    PreviewDialog(JFrame frame) {
        this.frame = frame;
        logger = Logger.getLogger(getClass().getName());
        dialog = new JDialog(frame);
        dialog.setSize(new Dimension(600,600));
        dialog.getContentPane().setLayout(new BorderLayout());
        editor = new JEditorPane();

        // Construct a custom HTMLEditorKit to replace the default one. This one
        // loads images synchronously rather than asynchronously, which means
        // that images should print successfully instead of appearing as broken
        // links.
        HTMLEditorKit kit = new HTMLEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return new HTMLFactory() {
                    @Override
                    public View create(Element elem) {
                        View view = super.create(elem);
                        if (view instanceof ImageView) {
                            ((ImageView) view).setLoadsSynchronously(true);
                        }
                        return view;
                    }
                };
            }
        };
        editor.setEditorKit(kit);
        editor.setEditable(false);
        editor.setContentType("text/html");

        // Java's default style doesn't seem to leave any gaps between
        // paragraphs.
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("p {margin-bottom:5px;}");
        Document doc = kit.createDefaultDocument();
        editor.setDocument(doc);

        JScrollPane editorScrollPane = new JScrollPane(editor);
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        JButton printButton = new JButton("Print story");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        configureButton(printButton,"/data/icons/print.png");

        JButton htmlButton = new JButton("Save story as HTML");
        htmlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAsHTML();
            }
        });
        configureButton(htmlButton,"/data/icons/save_as_html.png");

        toolBar.add(new JSeparator(SwingConstants.VERTICAL));

        JButton closeButton = new JButton("Close preview");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        configureButton(closeButton,"/data/icons/close.png");

        dialog.getContentPane().add(editorScrollPane,BorderLayout.CENTER);
        dialog.getContentPane().add(toolBar,BorderLayout.SOUTH);
    }

    // Configures a JButton and adds it to the toolbar.
    private void configureButton(JButton button, String iconPath) {
        try {
            ImageIcon icon = Util.readImageIconFromClassPath(iconPath);
            button.setIcon(icon);
        } catch (IOException e) {
            // Exception deliberately silenced. Just continue with no icon on
            // the button. The exception will already have been logged by Util.
        }
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(button);
    }

    // Update the storymap field and the editor.
    private void update(StoryMap map) {
        this.map = map;
        // Convert the story map to HTML and load it into the editor pane.
        try {
            String html = new TemplateHandler().renderStoryMap(map,getClass().getResource("/data/functions/").toString());
            editor.setText(html);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException when converting StoryMap to HTML", e);
        } catch (TemplateHandlerException e) {
            logger.log(Level.SEVERE, "TemplateHandlerException when converting StoryMap to HTML", e);
        }
    }

    /**
     * Show the preview dialog.
     */
    void show(StoryMap map) {
        update(map);
        // Show the dialog.        
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    void print(StoryMap m) {
        update(map);
        print();
    }

    // FIXME: if this method is called when show() hasn't been called it'll
    // just print nothing.
    private void print() {
        try {
            boolean printed = editor.print();
        } catch (PrinterException ex) {
            logger.log(Level.SEVERE, "PrinterException when trying to print StoryMap.", ex);
        }
    }

    void saveAsHTML(StoryMap map) {
        update(map);
        saveAsHTML();
    }

    // FIXME: if this method is called when show() hasn't been called you'll
    // get a NullPointerException. Really a PreviewDialog should belong to a
    // StoryMap, making such a situation impossible.
    private void saveAsHTML() {
        int returnVal = fileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".html")) {
                    path = path + ".html";
                }
                File parentDir = fileChooser.getSelectedFile().getParentFile();
                String fileName = new File(path).getName();
                String filesDir = fileName+"_files";
                File filesPath = new File(parentDir,filesDir);
                filesPath.mkdirs();
                try {
                    String html = new TemplateHandler().renderStoryMap(map, filesPath.getPath());
                    Util.writeTextToFile(html,path);
                } catch (TemplateHandlerException ex) {
                   logger.log(Level.SEVERE, "TemplateHandlerException when converting StoryMap to HTML.", ex);
                }
                for (int i=0; i < map.getStoryCards().size(); i++) {
                    StoryCard s = map.getStoryCards().get(i);
                    try {
                        BufferedImage bi = Util.toBufferedImage(s.getNode().toImage());
                        File outputfile = new File(filesPath,s.getFunction().getImageFilename());
                        ImageIO.write(bi, "png", outputfile);
                    } catch (IOException e) {
                        logger.warning("IOException when exporting story card image to file." + e.toString());
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException when writing HTML representation of StoryMap to file.", e);
            }
        } else {
            // The command was cancelled by the user.
        }
    }
}