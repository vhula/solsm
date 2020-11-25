package io.github.vhula.solsm.view;

import io.github.vhula.solsm.controller.Controller;
import io.github.vhula.solsm.model.Element;
import io.github.vhula.solsm.model.Input;
import io.github.vhula.solsm.model.PinState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 25.03.13
 * Time: 11:23
 */
public class ModelingTable extends JFrame {

    public JTable table = null;

    public JTextArea textArea;

    public JScrollPane jsp;

    public StringBuilder text = new StringBuilder();

    public Controller controller;

    public MainWindow mainWindow;

    public ModelingTable(Controller controller, MainWindow mainWindow) {
        super("Modeling");
        this.controller = controller;
        this.mainWindow = mainWindow;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(640, 480);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        jsp = new JScrollPane();
        jsp.setViewportView(textArea);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(jsp, BorderLayout.CENTER);
        init();
        textArea.setText(text.toString());
        setVisible(true);
    }

    public void init() {
        controller.modeling();
        ArrayList<Element> elements = mainWindow.schemeView.scheme.elements;
        ArrayList<ArrayList<PinState>> inSets = controller.mainWindow.schemeView.scheme.inSets;
        text.append("Inputs Sets:\n");
        for (Element el : elements) {
            for (Input in : el.inputs) {
                if (in.output == null) {
                    text.append(in.name);
                    text.append("   ");
                }
            }
        }
        text.append("      ");
        text.append("\n");
        for (ArrayList<PinState> ins : inSets) {
            for (int i = 0; i < ins.size(); i++) {
                text.append(ins.get(i).toString());
                text.append("    ");
            }
            text.append("\n");
        }
//        text.append("\n\n");
        text.append("Ranging Result:\n");
        text.append("Element");
        text.append("       ");
        text.append("Rang\n");
        for (Element el : controller.mainWindow.schemeView.scheme.elements) {
            text.append(el.name);
            text.append("              ");
            text.append(el.rang);
            text.append("\n");
        }
        text.append("\n");
        text.append("Modeling:\n");
        for (Element el : elements) {
            for (Input in : el.inputs) {
                if (in.output == null) {
                    text.append(in.name);
                    text.append("   ");
                }
            }
        }
        text.append("      ");
        for (int i = 0; i <= mainWindow.schemeView.scheme.maxRang(); i++) {
            for (Element el : elements) {
                if (el.rang == i) {
                    text.append(el.output.name);
                    text.append("   ");
                }
            }
        }
        text.append("\n");
    }

    public void step() {
        ArrayList<Element> elements = mainWindow.schemeView.scheme.elements;
        if (controller.curEl == 0) {
            for (Element el : elements) {
                for (Input in : el.inputs) {
                    if (in.output == null) {
                        text.append(in.state);
                        text.append(" ");
                        text.append("   ");
                    }
                }
            }
            text.append("      ");
        }
        text.append("" + controller.els.get(controller.curEl).output.state.toString() + "       ");
        if (controller.curEl == controller.els.size() - 1) {
            text.append("\n");
        }
        textArea.setText(text.toString());
        String[] strs = text.toString().split("\n");
        if (strs.length > 2) {
            if (strs[strs.length - 1].equals(strs[strs.length - 2])) {
                controller.nextSet();
            }
        }
        if (strs.length > 3) {
            if (strs[strs.length - 1].equals(strs[strs.length - 3])) {
                controller.nextSet();
            }
        }
    }

}
