package io.github.vhula.solsm.view;

import io.github.vhula.solsm.model.Element;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 16:19
 *
 */
public class ElementView extends Rectangle2D.Double implements Serializable {

    public Element element;

    public int x = 0;

    public int y = 0;

    int width = 50;

    public Color color = Color.BLACK;

    public ElementView(Element element, int x, int y) {
        super(x, y, 50, element.getInputsCount() * 20 + 20);
        this.x = x;
        this.y = y;
        this.element = element;
    }

    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(2f));
        Color c = g2.getColor();
        g2.setColor(color);
        g2.draw(this);
        g2.draw(this);
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        for (int i = 0; i < element.getInputsCount(); i++) {
            int x1 = x - 10;
            int y1 = y + 20 * i + 20;
            g2.drawLine(x1, y1, x, y1);
            g2.setColor(Color.GREEN.darker().darker());
            g2.drawString(element.inputs.get(i).name, x - 20, y1 - 5);
            g2.setColor(color);
            g2.drawString(element.inputs.get(i).state.toString(), x + 5, y1 + 5);
//            if (element.inputs.size() > i) {
//                g2.drawString(element.inputs.get(i).toString(), x + 5, y1 + 5);
//            } else {
//                g2.drawString(element.model.getDefaultValue(), x + 5, y1 + 5);
//            }
        }
        g2.setColor(Color.BLUE);
        g2.drawString(element.rang + "", x + width / 2, y + (int) height - 5);
        g2.setColor(color);
        g2.drawString(element.output.state.toString(), x + width - 10, y + element.getInputsCount() * 10);
        g2.drawLine(x + width, y + element.getInputsCount()* 10,
                x + width + 10, y + element.getInputsCount() * 10);
        g2.setColor(Color.GREEN.darker().darker());
        g2.drawString(element.output.name, x + width + 5, y + element.getInputsCount() * 10 - 10);
        g2.setColor(Color.RED);
        Font buf = g2.getFont();
        g2.drawString(element.type.toString(), x + width / 3, y + element.getInputsCount() * 5);
        switch (element.type) {
            case NOT_AND:
            case NOT_OR:
            case NOT_XOR:
                g2.drawOval(x + width - 7, y + element.getInputsCount() * 10 - 7, 14, 14);
        }
        g2.setFont(buf);
        g2.setColor(c);
    }

    public void setWidth() {
        setRect(x, y, width,  element.getInputsCount() * 20 + 20);
    }

}
