package io.github.vhula.solsm.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 01.03.13
 * Time: 22:17
 * Class which represents type of the logic element.
 */
public enum ElementType implements Serializable {

    AND,
    OR,
    NOT_AND,
    NOT_OR,
    XOR,
    NOT_XOR;

    public String toString() {
        switch (this) {
            case AND:
                return "&";
            case OR:
                return "1";
            case NOT_AND:
                return "!&";
            case NOT_OR:
                return "!1";
            case XOR:
                return "+";
            case NOT_XOR:
                return "!+";
            default:
                return "";
        }
    }

}
