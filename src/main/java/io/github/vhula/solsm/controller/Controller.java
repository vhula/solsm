package io.github.vhula.solsm.controller;


import io.github.vhula.solsm.model.*;
import io.github.vhula.solsm.view.MainWindow;
import io.github.vhula.solsm.view.SchemePanel;
import io.github.vhula.solsm.view.SchemeView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: github.vhula
 * Date: 10.03.13
 * Time: 15:33
 */
public class Controller implements Serializable {

    private Scheme scheme = null;

    public ToolItem selectedItem = ToolItem.ARROW;

    public MainWindow mainWindow;

    public boolean editing = true;

    public int curRang = 0;

    public int curEl = 0;

    public int curSet = 0;

    public boolean xFlag = false;
    public boolean yFlag = false;
    public boolean zFlag = false;

    public boolean modeling = true;

    public int counter = 0;

    public ArrayList<Element> els;

    public Controller(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Controller(Scheme scheme) {
        if (scheme == null) {
            throw new IllegalArgumentException("Schema cannot be null!");
        }
        this.scheme = scheme;
    }

    public void ranging() {
        this.scheme = mainWindow.schemeView.scheme;
        for (Element el : scheme.elements) {
            el.rang = 0;
        }
        for (Element el : scheme.elements) {
            for (Input in : el.inputs) {
                Element e = scheme.getElement(in.output);
                if (e.rang > el.rang) {

                }
            }
        }
    }

    public void step() {
        if (xFlag && zFlag) {
            if (scheme.inSets.size() > 0) {
                ArrayList<Input> ins = scheme.getFreeInputs();
                for (int i = 0; i < ins.size(); i++) {
                    ins.get(i).state = scheme.inSets.get(curSet).get(i);
                }
            }
            xFlag = false;
            yFlag = false;
            zFlag = false;
        }
        els.get(curEl).simulate();
        mainWindow.modelingTable.step();
        curEl++;
        if (curEl == els.size()) {
            curEl = 0;
        }
        if (yFlag && curEl == 0) {
            zFlag = true;
        }
        if (curEl == 0 && xFlag) {
            yFlag = true;
        }
    }

    public void nextSet() {
        curSet++;
        if (curSet >= scheme.inSets.size()) {
            curSet--;
            modeling = false;
            JOptionPane.showMessageDialog(mainWindow,
                    "Modeling is Finished!",
                    "Finish!",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (scheme.inSets.size() > 0) {
            ArrayList<Input> ins = scheme.getFreeInputs();
            for (int i = 0; i < ins.size(); i++) {
                if (scheme.model == SchemeModel.TWO) {
                    ins.get(i).state = scheme.inSets.get(curSet).get(i);
                }
                if (scheme.model == SchemeModel.THREE) {
                    if (ins.get(i).state != scheme.inSets.get(curSet).get(i)) {
                        ins.get(i).state = PinState.X;
                        xFlag = true;
                    }
                }
                if (scheme.model == SchemeModel.FIVE) {
                    PinState[][] FIFTH_MODEL = {
                            {PinState.ZERO, PinState.EPSYLON, PinState.ZERO, PinState.ZERO, PinState.ZERO},
                            {PinState.LAMBDA, PinState.ONE, PinState.X, PinState.X, PinState.ONE},
                            {PinState.X, PinState.X, PinState.LAMBDA, PinState.X, PinState.LAMBDA},
                            {PinState.X, PinState.X, PinState.X, PinState.EPSYLON, PinState.X},
                            {PinState.X, PinState.X, PinState.X, PinState.X, PinState.X}
                    };
                    int k = Element.idxs.get(scheme.inSets.get(curSet).get(i));
                    int j = Element.idxs.get(ins.get(i).state);
                    ins.get(i).state = FIFTH_MODEL[k][j];
                    xFlag = true;
                }
            }
        }
    }

    public void run() {
        modeling = true;
        while(modeling) {
            step();
        }
    }

    public void modeling() {
        this.scheme = mainWindow.schemeView.scheme;
        els = new ArrayList<Element>();
        ArrayList<Element> elements = mainWindow.schemeView.scheme.elements;
        for (int i = 0; i <= mainWindow.schemeView.scheme.maxRang(); i++) {
            for (Element el : elements) {
                if (el.rang == i) {
                    els.add(el);
                }
            }
        }
        if (scheme.inSets.size() > 0) {
            ArrayList<Input> ins = scheme.getFreeInputs();
            for (int i = 0; i < ins.size(); i++) {
                ins.get(i).state = scheme.inSets.get(0).get(i);
            }
        }
    }

    public void inputs() {
        this.scheme = mainWindow.schemeView.scheme;
        String input = JOptionPane.showInputDialog(this.mainWindow,
                "Input "
                + scheme.getFreeInputsCount()
                + " Signals By ',' :",
                "Input",
                JOptionPane.QUESTION_MESSAGE);
        ArrayList<Input> ins = scheme.getFreeInputs();
        String[] signs = input.split(",");
        ArrayList<PinState> inSet = new ArrayList<PinState>();
        for (int i = 0; i < signs.length; i++) {
            if (signs[i].equals("1")) {
//                ins.get(i).state = PinState.ONE;
                inSet.add(PinState.ONE);
            }
            if (signs[i].equals("0")) {
//                ins.get(i).state = PinState.ZERO;
                inSet.add(PinState.ZERO);
            }
            if (signs[i].equals("x")) {
//                ins.get(i).state = PinState.X;
                inSet.add(PinState.X);
            }
            if (signs[i].equals("01")) {
//                ins.get(i).state = PinState.LAMBDA;
                inSet.add(PinState.LAMBDA);
            }
            if (signs[i].equals("10")) {
//                ins.get(i).state = PinState.EPSYLON;
                inSet.add(PinState.EPSYLON);
            }
        }
        scheme.inSets.add(inSet);
    }

    public void newScheme() {
        System.out.println("New");
        mainWindow.remove(mainWindow.jsp);
        scheme = new Scheme();
        mainWindow.schemeView = new SchemeView(scheme);
        mainWindow.schemePanel = new SchemePanel(this, mainWindow.schemeView);
        mainWindow.jsp = new JScrollPane();
        mainWindow.jsp.setViewportView(mainWindow.schemePanel);
        mainWindow.jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainWindow.jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mainWindow.add(mainWindow.jsp, BorderLayout.CENTER);
        this.addElement();
    }

    public void openScheme() throws IOException, ClassNotFoundException {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return ".solsm";
            }

            @Override
            public boolean accept(File file) {
                if (file.getName().endsWith(".solsm")) {
                    return true;
                }
                if (file.isDirectory()) {
                    return true;
                }
                return false;
            }
        });
        int choice = jfc.showOpenDialog(null);
        String filename = null;
        if (choice == JFileChooser.APPROVE_OPTION) {
            filename = jfc.getSelectedFile().getName();
            int n = filename.length() - "solsm".length();
            String format = filename.substring(n, filename.length());
            if (format.equals("solsm")) {
                File file = new File(jfc.getSelectedFile().getPath());
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                SchemeView temp = (SchemeView) ois.readObject();
                mainWindow.schemeView = temp;
                mainWindow.schemePanel.schemeView = temp;
                ois.close();
                Element.nameStart += mainWindow.schemeView.scheme.elements.size();
                addElement();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Illegal file format."
                                + "\nPlease, try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                filename = null;
            }
        } else {
            filename = null;
        }
    }

    public void closeScheme() {
        newScheme();
    }

    public void saveScheme() throws IOException {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "*.solsm";
            }

            @Override
            public boolean accept(final File file) {
                if (file.getName().endsWith(".solsm")) {
                    return true;
                }
                if (file.isDirectory()) {
                    return true;
                }
                return false;
            }
        });
        int choice = jfc.showSaveDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().getPath() != null) {
                String filename = jfc.getSelectedFile().getPath();
                int n = filename.length() - ".solsm".length();
                String format = filename.substring(n, filename.length());
                if (!format.equals(".solsm")) {
                    filename += ".solsm";
                }
                File file = new File(filename);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(mainWindow.schemeView);
                oos.flush();
                oos.close();
                System.out.println("Saved");
            }
        }
    }

    public void saveAsScheme() {
        //TODO
    }

    public void addElement() {
        mainWindow.schemePanel.invalidate();
        mainWindow.schemePanel.validate();
        mainWindow.schemePanel.repaint();
    }

    public void removeElement(Element element) {
        //TODO
    }

    public void addConnection(Element source, Element dest) {
        //TODO
    }

    public void removeConnection(Element source, Element dest) {
        //TODO
    }

}
