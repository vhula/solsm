package io.github.vhula.solsm.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 01.03.13
 * Time: 22:16
 * Class which represents input in the element.
 */
public class Input implements Serializable {

    public PinState state = PinState.ZERO;

    public io.github.vhula.solsm.model.Output output = null;

    public io.github.vhula.solsm.model.SchemeModel model = io.github.vhula.solsm.model.SchemeModel.TWO;

    public String name;

    public Input() {
    }

    public Input(io.github.vhula.solsm.model.Output output) {
        setOutput(output);
    }

    public Input(io.github.vhula.solsm.model.Output output, PinState state) {
        setOutput(output);
        setState(state);
    }

    public void setOutput(io.github.vhula.solsm.model.Output output) {
        if (output == null) {
            throw new IllegalArgumentException("Output cannot be null!");
        }
        this.output = output;
    }

    public void setState(PinState state) {
        this.state = state;
    }

    public String toString() {
        return state.toString();
    }

    public void setModel(io.github.vhula.solsm.model.SchemeModel model) {
        this.model = model;
    }

}
