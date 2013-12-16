
package forms;

import gui.IToggleButton;
import java.awt.BorderLayout;
import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import logic.Application;
import microfont.Document;
import microfont.MSymbol;
import microfont.gui.MSymbolEditor;

public class EditPanel extends JPanel {
    private MSymbolEditor edit;

    public EditPanel(ActionMap am) {
        JToolBar tools;
        IToggleButton btnXPensil, btnPensil, btnRuber, btnPointer;

        setLayout(new BorderLayout());

        edit = new MSymbolEditor();

        tools = new JToolBar(JToolBar.VERTICAL);
        tools.setFloatable(false);

        btnPointer = new IToggleButton(am.get(Application.ON_MODE_POINTER));
        btnXPensil = new IToggleButton(am.get(Application.ON_MODE_XPENSIL));
        btnPensil = new IToggleButton(am.get(Application.ON_MODE_PENSIL));
        btnRuber = new IToggleButton(am.get(Application.ON_MODE_RUBER));
        Application.updateButtonMode();

        tools.add(btnPointer);
        tools.add(btnXPensil);
        tools.add(btnPensil);
        tools.add(btnRuber);
        tools.add(new JToolBar.Separator());

        add(tools, BorderLayout.WEST);
        add(new JScrollPane(edit), BorderLayout.CENTER);
    }

    public MSymbol getMSymbol() {
        return edit.getSymbol();
    }

    public void setMSymbol(MSymbol symbol) {
        edit.setSymbol(symbol);
    }

    public void setDocument(Document doc) {
        edit.setDocument(doc);
    }
}
