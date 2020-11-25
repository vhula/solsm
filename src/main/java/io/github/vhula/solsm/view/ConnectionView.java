package io.github.vhula.solsm.view;

import io.github.vhula.solsm.model.Connection;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 20:38
 *
 */
public class ConnectionView extends Line2D.Double implements Serializable {

    public Connection connection;

    public ElementView source;

    public ElementView dest;

    Polygon polygon = new Polygon();

    public Color color = Color.BLACK;

    public ConnectionView(Connection connection, ElementView source, ElementView dest) {
        this.connection = connection;
        this.source = source;
        this.dest = dest;
        Random r = new Random();
        color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }

    public void draw(Graphics2D g2) {
        Color c = g2.getColor();
        g2.setColor(color);
        int[] px = polygon.xpoints;
        int[] py = polygon.ypoints;
        px[0] = source.x + source.width + 10;
        py[0] = source.y + source.element.getInputsCount() * 10;
        for (int i = 0; i < polygon.npoints - 1; i++) {
            g2.drawLine(px[i], py[i], px[i + 1], py[i + 1]);
        }
        g2.setColor(c);
    }

}
