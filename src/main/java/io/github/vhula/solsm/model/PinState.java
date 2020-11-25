package io.github.vhula.solsm.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 01.03.13
 * Time: 22:58
 * Represents states of the pin.
 */
public enum PinState implements Serializable {

    ZERO,
    ONE,
    X,
    LAMBDA,                //0 -> 1
    EPSYLON;               //1 -> 0

    public String toString() {
        switch (this) {
            case ZERO:
                return "0";
            case ONE:
                return "1";
            case X:
                return "X";
            case LAMBDA:
                return "λ";
            case EPSYLON:
                return "ε";
            default:
                return "";
        }
    }

}
