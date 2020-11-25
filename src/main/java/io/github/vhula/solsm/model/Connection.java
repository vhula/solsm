package io.github.vhula.solsm.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 10.03.13
 * Time: 20:37
 * To change this template use File | Settings | File Templates.
 */
public class Connection implements Serializable {

    public io.github.vhula.solsm.model.Element source = null;

    public io.github.vhula.solsm.model.Element dest = null;

    public Connection(io.github.vhula.solsm.model.Element source, io.github.vhula.solsm.model.Element dest) {
        this.source = source;
        this.dest = dest;
    }

}
