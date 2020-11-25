package io.github.vhula.solsm.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 15:41
 *
 */
public class Scheme implements Serializable {

    public ArrayList<io.github.vhula.solsm.model.Element>  elements = new ArrayList<io.github.vhula.solsm.model.Element>();

    public ArrayList<Connection> connections = new ArrayList<Connection>();

    public SchemeModel model = SchemeModel.TWO;

    public ArrayList<ArrayList<PinState>> inSets = new ArrayList<ArrayList<PinState>>();

    public void addConnection(Connection connection) {
        connections.add(connection);
        for (Input in : connection.source.inputs) {
            if (connection.dest.output.equals(in.output)) {
                return;
            }
        }
        if (connection.dest.rang < connection.source.rang) {
            connection.dest.rang += connection.source.rang + 1;
        } else {
            if (connection.dest.rang == connection.source.rang) {
                connection.dest.rang += 1;
            }
        }
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
        for (Input in : connection.dest.inputs) {
            for (io.github.vhula.solsm.model.Element el : elements) {
                if (el.output.equals(in.output)) {
                    if (el.rang >= connection.dest.rang) {
                        connection.dest.rang = el.rang + 1;
                    }
                }
            }
        }
    }

    public void addElement(io.github.vhula.solsm.model.Element element) {
        elements.add(element);
    }

    public void removeElement(io.github.vhula.solsm.model.Element element) {
        elements.remove(element);
    }

    public void setModel(SchemeModel model) {
        this.model = model;
        for (io.github.vhula.solsm.model.Element element : elements) {
            element.setModel(model);
        }
    }

    public int getFreeInputsCount() {
        int res = 0;
        for (io.github.vhula.solsm.model.Element el : elements) {
            for (Input in : el.inputs) {
                if (in.output == null) {
                    res++;
                }
            }
        }
        return res;
    }

    public ArrayList<Input> getFreeInputs() {
        ArrayList<Input> res = new ArrayList<Input>();
        for (io.github.vhula.solsm.model.Element el : elements) {
            for (Input in : el.inputs) {
                if (in.output == null) {
                    res.add(in);
                }
            }
        }
        return res;
    }

    public io.github.vhula.solsm.model.Element getElement(Input in) {
        for (io.github.vhula.solsm.model.Element el : elements) {
            if (el.inputs.contains(in)) {
                return el;
            }
        }
        return null;
    }

    public io.github.vhula.solsm.model.Element getElement(Output out) {
        for (io.github.vhula.solsm.model.Element el : elements) {
            if (out.equals(el.output)) {
                return el;
            }
        }
        return null;
    }

    public int maxRang() {
        int max = 0;
        for (io.github.vhula.solsm.model.Element el : elements) {
            if (el.rang > max) {
                max = el.rang;
            }
        }
        return max;
    }

}
