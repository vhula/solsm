package io.github.vhula.solsm.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 17.03.13
 * Time: 12:20
 *
 */
public enum SchemeModel implements Serializable {

    TWO,
    THREE,
    FIVE;

    public String toString() {
        switch (this) {
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FIVE:
                return "5";
            default:
                return "";
        }
    }

    public String getDefaultValue() {
        switch (this) {
            case TWO:
                return "0";
            case THREE:
                return "x";
            case FIVE:
                return "x";
            default:
                return "";
        }
    }

    public PinState getValue() {
        switch (this) {
            case TWO:
                return PinState.ZERO;
            case THREE:
                return PinState.X;
            case FIVE:
                return PinState.X;
            default:
                return PinState.X;
        }
    }

}
