package forms;

import gui.IToggleButton;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import logic.Application;
import microfont.MSymbol;
import microfont.gui.MSymbolEditor;

public class EditPanel extends JPanel
{
    public MSymbolEditor edit;

    public EditPanel() {
        JToolBar tools;
        IToggleButton btnXPensil, btnPensil, btnRuber, btnPointer;

        this.setMaximumSize(new Dimension(100000, 1000));
        this.setLayout(new BorderLayout());

        tools = new JToolBar(JToolBar.VERTICAL);
        tools.setFloatable(false);

        edit = new MSymbolEditor(new MSymbol(24, 12, new byte [400]));

        btnPointer = new IToggleButton(Application.actModePointer);
        btnXPensil = new IToggleButton(Application.actModeXPensil);
        btnPensil = new IToggleButton(Application.actModePensil);
        btnRuber = new IToggleButton(Application.actModeRuber);
        Application.updateButtonMode();

        tools.add(btnPointer);
        tools.add(btnXPensil);
        tools.add(btnPensil);
        tools.add(btnRuber);
        tools.add(new JToolBar.Separator());

        this.add(tools, BorderLayout.WEST);
        this.add(new JScrollPane(edit), BorderLayout.CENTER);
    }
    
    public void setMSymbol(MSymbol symbol) {
        edit.setSymbol(symbol);
    }
}
