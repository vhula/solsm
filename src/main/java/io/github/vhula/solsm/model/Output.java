package io.github.vhula.solsm.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 01.03.13
 * Time: 22:16
 * Class which represents output in the element.
 */
public class Output implements Serializable {

    public PinState state = PinState.ZERO;

    public io.github.vhula.solsm.model.SchemeModel model = io.github.vhula.solsm.model.SchemeModel.TWO;

    public String name;

    public Output() {
    }

    public Output(PinState state) {
        setState(state);
    }

    public void setState(PinState state) {
        this.state = state;
    }

    public void setModel(io.github.vhula.solsm.model.SchemeModel model) {
        this.model = model;
    }

}
