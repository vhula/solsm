package io.github.vhula.solsm.view;

import io.github.vhula.solsm.controller.Controller;
import io.github.vhula.solsm.model.Element;
import io.github.vhula.solsm.model.Input;
import io.github.vhula.solsm.model.PinState;
import io.github.vhula.solsm.model.SchemeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 15:58
 *
 */
public class MenuBar extends JMenuBar implements ActionListener, ItemListener {

    public Controller controller;

    private JMenu fileMenu = new JMenu("File");

    private JMenuItem newItem = new JMenuItem("New");

    private JMenuItem openItem = new JMenuItem("Open...");

    private JMenuItem saveItem = new JMenuItem("Save");

    private JMenuItem saveAsItem = new JMenuItem("Save As...");

    private JMenuItem closeItem = new JMenuItem("Close");

    public JMenuItem resetItem = new JMenuItem("Reset");

    public JMenu modelMenu = new JMenu("Model");

    public ButtonGroup modelGroup = new ButtonGroup();

    public JRadioButton twoButton = new JRadioButton("2");
    public JRadioButton threeButton = new JRadioButton("3");
    public JRadioButton fiveButton = new JRadioButton("5");

    public JMenu editorModeMenu = new JMenu("Mode");

    public ButtonGroup modeGroup = new ButtonGroup();

    public JRadioButton editButton = new JRadioButton("Editing");
    public JRadioButton modelingButton = new JRadioButton("Modeling");

    public MenuBar(Controller controller) {
        this.controller = controller;
        initItems();
        initMenuBar();
        this.add(fileMenu);
        this.add(modelMenu);
        this.add(editorModeMenu);
    }

    private void initItems() {
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        closeItem.addActionListener(this);

        modelGroup.add(twoButton);
        modelGroup.add(threeButton);
        modelGroup.add(fiveButton);
        twoButton.addItemListener(this);
        threeButton.addItemListener(this);
        fiveButton.addItemListener(this);

        modeGroup.add(modelingButton);
        modeGroup.add(editButton);
        modelingButton.addItemListener(this);
        editButton.addItemListener(this);

        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Element el : controller.mainWindow.schemeView.scheme.elements) {
                    PinState val = controller.mainWindow.schemeView.scheme.model.getValue();
                    el.output.state = val;
                    for (Input in : el.inputs) {
                        in.state = val;
                    }
                }
            }
        });
    }

    private void initMenuBar() {
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
//        fileMenu.add(saveAsItem);
        fileMenu.add(closeItem);
        fileMenu.add(resetItem);

        modelMenu.add(twoButton);
        modelMenu.add(threeButton);
        modelMenu.add(fiveButton);
        twoButton.setSelected(true);

        editorModeMenu.add(editButton);
        editorModeMenu.add(modelingButton);
        editButton.setSelected(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src.equals(newItem)) {
            controller.newScheme();
        }
        if (src.equals(openItem)) {
            try {
                controller.openScheme();
            } catch (IOException exc) {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(null, "File is corrupted!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException exc) {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(null, "File is corrupted!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (src.equals(saveItem)) {
            try {
                controller.saveScheme();
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null, "File is not saved!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (src.equals(saveAsItem)) {
            controller.saveAsScheme();
        }
        if (src.equals(closeItem)) {
            controller.closeScheme();
        }
    }

    public void itemStateChanged(ItemEvent event) {
        JRadioButton src = (JRadioButton) event.getSource();
        if (src.isSelected()) {
            if (src.getText().equals("2")) {
                controller.mainWindow.schemeView.scheme.setModel(SchemeModel.TWO);
                return;
            }
            if (src.getText().equals("3")) {
                controller.mainWindow.schemeView.scheme.setModel(SchemeModel.THREE);
                return;
            }
            if (src.getText().equals("5")) {
                controller.mainWindow.schemeView.scheme .setModel(SchemeModel.FIVE);
            }
            if (src.equals(editButton)) {
                controller.editing = true;
                controller.curSet = 0;
                controller.curEl = 0;
                controller.xFlag = false;
                controller.mainWindow.schemePanel.backColor = Color.WHITE;
                controller.mainWindow.toolBar.stepButton.setEnabled(false);
                controller.mainWindow.toolBar.runButton.setEnabled(false);
                if (controller.mainWindow.modelingTable != null)
                    controller.mainWindow.modelingTable.dispose();
            }
            if (src.equals(modelingButton)) {
                controller.editing = false;
                controller.mainWindow.schemePanel.backColor = new Color(182, 181, 181);
                controller.mainWindow.modelingTable = new ModelingTable(controller, controller.mainWindow);
                controller.mainWindow.toolBar.stepButton.setEnabled(true);
                controller.mainWindow.toolBar.runButton.setEnabled(true);
            }
        }
        controller.addElement();
    }
}
