package io.github.vhula.solsm.view;

import io.github.vhula.solsm.controller.Controller;
import io.github.vhula.solsm.model.Scheme;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow extends JFrame implements Serializable {

    public Controller controller = new Controller(this);

    public io.github.vhula.solsm.view.SchemeView schemeView = new io.github.vhula.solsm.view.SchemeView(new Scheme());

    public io.github.vhula.solsm.view.SchemePanel schemePanel = new io.github.vhula.solsm.view.SchemePanel(controller, schemeView);

    public io.github.vhula.solsm.view.ModelingTable modelingTable;

    public JScrollPane jsp;

    public io.github.vhula.solsm.view.ToolBar toolBar = new io.github.vhula.solsm.view.ToolBar(controller);;

    public MainWindow() {
        super("SoLSM by Hula Vadym");
        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JMenuBar menuBar = new io.github.vhula.solsm.view.MenuBar(controller);
        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.NORTH);
        jsp = new JScrollPane();
        jsp.setViewportView(schemePanel);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(jsp, BorderLayout.CENTER);
        setVisible(true);
    }


}
