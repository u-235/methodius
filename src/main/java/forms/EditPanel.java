
package forms;

import static logic.Application.application;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import microfont.Document;
import microfont.MSymbol;
import microfont.gui.MSymbolEditor;
import microfont.render.ColorIndex;
import microfont.render.PixselMapRender;
import utils.config.ConfigChangeEvent;
import utils.config.ConfigChangeListener;
import utils.config.ConfigNode;

public class EditPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private MSymbolEditor     edit;

    public EditPanel() {
        JToolBar tools;
        setLayout(new BorderLayout());

        edit = new MSymbolEditor();

        ConfigNode cn = application().config().node("editor");
        cn.addConfigChangeListener(new ConfigChangeListener() {
            @Override
            public void configChange(ConfigChangeEvent ev) {
                updateStyle(false, ev.getNode(), ev.getKey());
            }
        });
        updateStyle(true, cn, null);

        tools = new JToolBar(JToolBar.VERTICAL);
        tools.setFloatable(false);
        // TODO place buttons for tools

        tools.add(new JToolBar.Separator());

        add(tools, BorderLayout.WEST);
        add(new JScrollPane(edit), BorderLayout.CENTER);
    }

    void updateStyle(boolean all, ConfigNode node, String key) {
        PixselMapRender render = edit.getSymbolRender();

        if (all || key.equals("paper")) {
            render.setColor(ColorIndex.COLOR_PAPER,
                            node.getColor("paper", null));
        }
        if (all || key.equals("ink")) {
            render.setColor(ColorIndex.COLOR_INK,
                            node.getColor("ink", Color.BLACK));
        }
        if (all || key.equals("inkAscent")) {
            render.setColor(ColorIndex.COLOR_INK_ASCENT,
                            node.getColor("inkAscent", null));
        }
        if (all || key.equals("inkDescent")) {
            render.setColor(ColorIndex.COLOR_INK_DESCENT,
                            node.getColor("inkDescent", null));
        }
        if (all || key.equals("InkMargins")) {
            render.setColor(ColorIndex.COLOR_INK_MARGINS,
                            node.getColor("InkMargins", new Color(60, 0, 0)));
        }
        if (all || key.equals("paper")) {
            render.setColor(ColorIndex.COLOR_PAPER,
                            node.getColor("paper", Color.WHITE));
        }
        if (all || key.equals("paperAscent")) {
            render.setColor(ColorIndex.COLOR_PAPER_ASCENT, node.getColor(
                            "paperAscent", new Color(224, 224, 224)));
        }
        if (all || key.equals("paperDescent")) {
            render.setColor(ColorIndex.COLOR_PAPER_DESCENT,
                            node.getColor("paperDescent", null));
        }
        if (all || key.equals("paperMargins")) {
            render.setColor(ColorIndex.COLOR_PAPER_MARGINS,
                            node.getColor("paperMargins", Color.LIGHT_GRAY));
        }

        if (all || key.equals("grid")) {
            render.setColor(ColorIndex.COLOR_GRID,
                            node.getColor("grid", Color.LIGHT_GRAY));
        }
        if (all || key.equals("gridSize")) {
            render.setGridSize(node.getInt("gridSize", 3));
        }
        if (all || key.equals("gridThickness")) {
            render.setGridThickness(node.getInt("gridThickness", 1));
        }

        if (all || key.equals("spacing")) {
            render.setSpacing(node.getInt("spacing", 1));
        }
        if (all || key.equals("space")) {
            render.setColor(ColorIndex.COLOR_SPACE,
                            node.getColor("space", null));
        }

        if (all || key.equals("size")) {
            render.setPixselSize(node.getInt("size", 6));
        }
        if (all || key.equals("ratio")) {
            render.setPixselRatio(node.getFloat("ratio", 1));
        }
    }

    public MSymbol getMSymbol() {
        return (MSymbol) edit.getPixselMap();
    }

    public void setMSymbol(MSymbol symbol) {
        edit.setPixselMap(symbol);
        edit.getDocument().setEditedSymbol(symbol);
    }

    public void setDocument(Document doc) {
        edit.setDocument(doc);
    }
}
