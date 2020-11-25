package io.github.vhula.solsm.view;

import io.github.vhula.solsm.model.Scheme;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 18:36
 *
 */
public class SchemeView implements Serializable {

    public Scheme scheme;

    public ArrayList<ElementView> elementViews = new ArrayList<ElementView>();

    public ArrayList<ConnectionView> connectionViews = new ArrayList<ConnectionView>();

    public SchemeView(Scheme scheme) {
        this.scheme = scheme;
    }

    public void addElement(ElementView elementView) {
        elementViews.add(elementView);
        scheme.addElement(elementView.element);
    }

    public void addConnection(ConnectionView connectionView) {
        connectionViews.add(connectionView);
        scheme.addConnection(connectionView.connection);
    }

    public void removeConnection(ConnectionView connectionView) {
        connectionViews.remove(connectionView);
        scheme.removeConnection(connectionView.connection);
    }

    public void removeElement(ElementView elementView) {
        elementViews.remove(elementView);
        scheme.removeElement(elementView.element);
    }

    public void draw(Graphics2D g2) {
        for (ElementView e : elementViews) {
            e.draw(g2);
        }
        for (ConnectionView c : connectionViews) {
            c.draw(g2);
        }
    }

}
