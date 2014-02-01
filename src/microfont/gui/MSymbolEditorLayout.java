package microfont.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

public class MSymbolEditorLayout implements LayoutManager {
    Dimension pref;
    MSymbolEditor owner;

    public  MSymbolEditorLayout( MSymbolEditor editor) {
        owner=editor;
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
        return new Dimension(owner.render().getWidth(), owner.render().getHeight());
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void layoutContainer(Container parent) {
        int w = parent.getWidth();
        int h = parent.getHeight();
        
        Rectangle renderPos=owner.elementPosition(0);
        renderPos.width=owner.render().getWidth();
        renderPos.height=owner.render().getHeight();

        if (renderPos.width >= w) renderPos.x = 0;
        else renderPos.x = (w - renderPos.width) / 2;

        if (owner.render().getHeight() >= h) renderPos.y = 0;
        else renderPos.y = (h - owner.render().getHeight()) / 2;
    }
}
