package io.github.vhula.solsm.view;

import io.github.vhula.solsm.controller.Controller;
import io.github.vhula.solsm.model.ToolItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 15:46
 *
 */
public class ToolBar extends JToolBar implements ActionListener, Serializable {

    public Controller controller;

    public JButton arrow = new JButton("Arrow");

    public JButton addElement = new JButton("Add");

    public JButton addConnection = new JButton("Connection");

    public JButton remove = new JButton("Remove");

    public JButton stepButton = new JButton("Step");

    public JButton runButton = new JButton("Run");

    public JButton inputsButton = new JButton("Inputs");

    public ToolBar(Controller controller) {
        this.controller = controller;
        initButtons();
        initToolBar();
    }

    private void initButtons() {
        addElement.addActionListener(this);
        addConnection.addActionListener(this);
        remove.addActionListener(this);
        arrow.addActionListener(this);
        stepButton.addActionListener(this);
        runButton.addActionListener(this);
        inputsButton.addActionListener(this);
        addElement.setIcon(new ImageIcon(readIconContent("icons/el.png"), "Element"));
        addConnection.setIcon(new ImageIcon(readIconContent("icons/conn.png"), "Connection"));
        remove.setIcon(new ImageIcon(readIconContent("icons/remove.png"), "Remove"));
        arrow.setIcon(new ImageIcon(readIconContent("icons/arrow.png"), "Arrow"));
        stepButton.setIcon(new ImageIcon(readIconContent("icons/step.png"), "Step"));
        runButton.setIcon(new ImageIcon(readIconContent("icons/run.png"), "Run"));
        addElement.setText("");
        addConnection.setText("");
        remove.setText("");
        arrow.setText("");
        stepButton.setText("");
        runButton.setText("");
    }

    private byte[] readIconContent(String iconResource) {
        try (BufferedInputStream bis = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(iconResource))) {
            return bis.readAllBytes();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initToolBar() {
        this.add(arrow);
        this.add(addElement);
        this.add(addConnection);
        this.add(remove);
        this.addSeparator();
        this.add(stepButton);
        this.add(runButton);
        this.addSeparator();
        this.add(inputsButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addElement)) {
            System.out.println("Add Element Selected");
            controller.selectedItem = ToolItem.ELEMENT;
        }
        if (e.getSource().equals(addConnection)) {
            controller.selectedItem = ToolItem.CONNECTION;
        }
        if (e.getSource().equals(remove)) {
            controller.selectedItem = ToolItem.REMOVE;
        }
        if (e.getSource().equals(arrow)) {
            controller.selectedItem = ToolItem.ARROW;
        }
        if (e.getSource().equals(stepButton)) {
            controller.step();
        }
        if (e.getSource().equals(runButton)) {
            controller.run();
        }
        if (e.getSource().equals(inputsButton)) {
            controller.inputs();
        }
    }
}
