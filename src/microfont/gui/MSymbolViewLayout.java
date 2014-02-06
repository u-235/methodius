
package microfont.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

/**
 */
public class MSymbolViewLayout implements LayoutManager {
    Dimension   pref;
    MSymbolView owner;

    public MSymbolViewLayout(MSymbolView view) {
        owner = view;
        pref = new Dimension();
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeLayoutComponent(Component comp) {
        // TODO Auto-generated method stub

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        calculate();
        return pref;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        calculate();
        layout();
    }

    void calculate() {
        pref.width = owner.render().getWidth()+90;
        pref.height = owner.render().getHeight()+10;
    }

    void layout() {
        Rectangle renderPos = owner.elementPosition(0);
        renderPos.width = owner.render().getWidth();
        renderPos.height = owner.render().getHeight();

        renderPos.x = 90;
        renderPos.y = 10;
    }
}
