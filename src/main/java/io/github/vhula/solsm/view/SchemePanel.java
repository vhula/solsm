package io.github.vhula.solsm.view;

import io.github.vhula.solsm.controller.Controller;
import io.github.vhula.solsm.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 16:36
 *
 */
public class SchemePanel extends JPanel implements Serializable {

    public Controller controller;

    public io.github.vhula.solsm.view.SchemeView schemeView;

    public MouseHandler mouseAdapter = new MouseHandler();

    public int step = 20;

    public Color backColor = Color.WHITE;

    public SchemePanel(Controller controller, io.github.vhula.solsm.view.SchemeView schemeView) {
        setPreferredSize(new Dimension(1400, 1400));
        this.controller = controller;
        this.schemeView = schemeView;
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        setBackground(backColor);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(backColor);
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < getWidth(); i += step) {
            for (int j = 0; j < getHeight(); j += step) {
                g2.drawOval(i, j, 2, 2);
            }
        }
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        mouseAdapter.drawPolygon(g2);
        schemeView.draw(g2);
    }

    public class MouseHandler extends MouseAdapter {

        public int x = 0;

        public int y = 0;

        public ElementView selectedElement = null;

        public ElementView el1 = null;

        public ElementView el2 = null;

        public Polygon p = new Polygon();

        public void drawPolygon(Graphics2D g2) {
            int[] px = p.xpoints;
            int[] py = p.ypoints;
            if (el1 == null)
                return;
            px[0] = el1.x + el1.width + 10;
            py[0] = el1.y + el1.element.getInputsCount() * 10;
            for (int i = 0; i < p.npoints - 1; i++) {
                g2.drawLine(px[i], py[i], px[i + 1], py[i + 1]);
            }
        }

        public void mouseClicked(MouseEvent event) {
            if (event.getButton() == 3) {
                el1 = null;
                el2 = null;
                for (ElementView el : schemeView.elementViews) {
                    if (el.contains(event.getX(), event.getY())) {
                        PopupMenu popupMenu = new PopupMenu(el);
                        popupMenu.show(event.getComponent(), event.getX(), event.getY());
                    }
                }
            }
            if (event.getButton() == 1) {
                if (!controller.editing) {
                    System.out.println("SSSSS");
                    return;
                }
                switch (controller.selectedItem) {
                    case ELEMENT:
                        elementClicked(event);
                        el1 = null;
                        el2 = null;
                        p = new Polygon();
                        p.reset();
                        break;
                    case CONNECTION:
                        boolean flag = true;
                        for (ElementView el : schemeView.elementViews) {
                            if (el.contains(event.getX(), event.getY())) {
                                if (el1 == null) {
                                    el1 = el;
                                    p.addPoint(el1.x + el1.width + 10,
                                            el1.y + el1.element.getInputsCount() * 10);
                                    flag = false;
                                    break;
                                } else {
                                    if (el1 == el)
                                        break;
                                    el2 = el;
                                    int idx = el2.element.getFreeInput();
                                    if (idx == -1) {
                                        JOptionPane.showMessageDialog(controller.mainWindow, "Dest element hasn't free inputs.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                        el1 = null;
                                        el2 = null;
                                        p = new Polygon();
                                        flag = false;
                                        break;
                                    }
                                    p.addPoint(el2.x - 10,
                                            el2.y + idx * 20 + 20);
                                    el2.element.addInput(new Input(el1.element.output));
                                    Connection connection = new Connection(el1.element, el2.element);
                                    ConnectionView cw = new ConnectionView(connection, el1, el2);
                                    for (ConnectionView c : schemeView.connectionViews) {
                                        if (cw.source.equals(c.source)) {
                                            cw.color = c.color;
                                        }
                                    }
                                    cw.polygon = p;
                                    schemeView.addConnection(cw);
                                    controller.addElement();
                                    el1 = null;
                                    el2 = null;
                                    p = new Polygon();
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (flag) {
                            int x = (event.getX() / step) * step;
                            int y = (event.getY() / step) * step;
                            p.addPoint(x, y);
                        }
                        break;
                    case REMOVE:
                        for (ElementView el : schemeView.elementViews) {
                            if (el.contains(event.getX(), event.getY())) {
                                if (el1 == el) {
                                    el2 = el;
                                    for (Element e : schemeView.scheme.elements) {
                                        for (Input in : e.inputs) {
                                            if (el.element.output.equals(in.output)) {
                                                in.output = null;
                                            }
                                        }
                                    }
                                    ArrayList<ConnectionView> fr = new ArrayList<ConnectionView>();
                                    for (ConnectionView cv : schemeView.connectionViews) {
                                        if (cv.connection.source.equals(el.element)
                                                || cv.connection.dest.equals(el.element)) {
                                            fr.add(cv);
                                        }
                                    }
                                    schemeView.connectionViews.removeAll(fr);
                                    break;
                                }
                                if (el1 == null) {
                                    el1 = el;
                                } else {
                                    el2 = el;
                                    for (ConnectionView cv : schemeView.connectionViews) {
                                        boolean removable = false;
                                        if (cv.source.equals(el1) && cv.dest.equals(el2)) {
                                            for (Input in : cv.connection.dest.inputs) {
                                                if (el1.element.output.equals(in.output)) {
                                                    in.output = null;
                                                    removable = true;
                                                }
                                            }
                                            if (removable) {
                                                el1 = null;
                                                el2 = null;
                                                schemeView.removeConnection(cv);
                                                break;
                                            }
                                            removable = false;
                                        }
                                        if (cv.source.equals(el2) && cv.dest.equals(el1)) {
                                            for (Input in : cv.connection.dest.inputs) {
                                                if (el1.element.output.equals(in.output)) {
                                                    removable = true;
                                                    in.output = null;
                                                }
                                            }
                                            if (removable) {
                                                el1 = null;
                                                el2 = null;
                                                schemeView.removeConnection(cv);
                                                break;
                                            }
                                            removable = false;
                                        }
                                    }
                                    el1 = null;
                                    el2 = null;
                                }
                            }
                        }
                        if (el2 != null) {
                            schemeView.removeElement(el1);
                            el1 = null;
                            el2 = null;
                        }
                        break;
                    default:
                        el1 = null;
                        el2 = null;
                        p = new Polygon();
                        p.reset();
                        break;
                }
            }
        }

        class PopupMenu extends JPopupMenu {

            public ElementView element;

            private JMenuItem setType = new JMenuItem("Change Type");
            private JMenuItem setInputsCount = new JMenuItem("Change Inputs Count");
            private JMenuItem setInputs = new JMenuItem("Change In Signals");
            private JMenuItem simulate = new JMenuItem("Simulate");
            private JMenuItem setRang = new JMenuItem("rang...");

            public PopupMenu(final ElementView element) {
                this.element = element;
                setType.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog(null, "Input weight:",
                                "Input", JOptionPane.QUESTION_MESSAGE);
                        Element el = element.element;
                        if (input.equals("and")) {
                            el.setType(ElementType.AND);
                        }
                        if (input.equals("or")) {
                            el.setType(ElementType.OR);
                        }
                        if (input.equals("xor")) {
                            el.setType(ElementType.XOR);
                        }
                        if (input.equals("nand")) {
                            el.setType(ElementType.NOT_AND);
                        }
                        if (input.equals("nor")) {
                            el.setType(ElementType.NOT_OR);
                        }
                        if (input.equals("nxor")) {
                            el.setType(ElementType.NOT_XOR);
                        }
                    }
                });

                setInputsCount.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog(null, "Input weight:",
                                "Input", JOptionPane.QUESTION_MESSAGE);
                        int w = Integer.parseInt(input);
                        element.element.setInputsCount(w);
                        element.setWidth();
                    }
                });
                setInputs.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog(null, "Input Signals By ',' :",
                                "Input", JOptionPane.QUESTION_MESSAGE);
                        String[] signs = input.split(",");
                        for (int i = 0; i < signs.length; i++) {
                            if (signs[i].equals("1")) {
                                element.element.inputs.get(i).setState(PinState.ONE);
                            }
                            if (signs[i].equals("0")) {
                                element.element.inputs.get(i).setState(PinState.ZERO);
                            }
                            if (signs[i].equals("x")) {
                                element.element.inputs.get(i).setState(PinState.X);
                            }
                            if (signs[i].equals("01")) {
                                element.element.inputs.get(i).setState(PinState.LAMBDA);
                            }
                            if (signs[i].equals("10")) {
                                element.element.inputs.get(i).setState(PinState.EPSYLON);
                            }
                        }
                    }
                });
                setRang.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String input = JOptionPane.showInputDialog(null, "Input Signals By ',' :",
                                "Input", JOptionPane.QUESTION_MESSAGE);
                        int r = Integer.parseInt(input);
                        element.element.rang = r;
                    }
                });
                simulate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        element.element.simulate();
                    }
                });
                add(setType);
                add(setInputsCount);
                add(setInputs);
                add(simulate);
                add(setRang);
                controller.addElement();
            }
        }

        protected void removeClicked(MouseEvent event) {

        }

        protected void arrowClicked(MouseEvent event) {

        }

        protected void elementClicked(MouseEvent event) {
            int x = (event.getX() / step) * step;
            int y = (event.getY() / step) * step;
            Element el = new Element();
            el.setModel(schemeView.scheme.model);
            schemeView.addElement(new ElementView(el, x, y));
            controller.addElement();
        }

        protected void connectionClicked(MouseEvent event) {

        }

        @Override
        public void mouseMoved(MouseEvent event) {
            for (ElementView el : schemeView.elementViews) {
                if (el.contains(event.getX(), event.getY())) {
                    el.color = Color.BLUE;
                } else {
                    el.color = Color.BLACK;
                }
            }

            controller.addElement();
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (!controller.editing) {
                return;
            }
            for (ElementView el : schemeView.elementViews) {
                if (el.contains(event.getX(), event.getY())) {
                    selectedElement = el;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            selectedElement = null;
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            if (!controller.editing) {
                return;
            }
            int x = (event.getX() / step) * step;
            int y = (event.getY() / step) * step;
            selectedElement.x = x;
            selectedElement.y = y;
            selectedElement.setRect(x, y, selectedElement.width, selectedElement.height);
            controller.addElement();
        }
    }
}


