package io.github.vhula.solsm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Vadym Hula
 * Date: 01.03.13
 * Time: 22:14
 * Class which represents logic element of the schema.
 */
public class Element implements Serializable {

    public static PinState[][] OR_5_STATES = {
            {PinState.ZERO, PinState.ONE, PinState.LAMBDA, PinState.EPSYLON, PinState.X},
            {PinState.ONE, PinState.ONE, PinState.ONE, PinState.ONE, PinState.ONE},
            {PinState.LAMBDA, PinState.ZERO, PinState.LAMBDA, PinState.X, PinState.LAMBDA},
            {PinState.EPSYLON, PinState.ONE, PinState.X, PinState.EPSYLON, PinState.X},
            {PinState.X, PinState.ONE, PinState.LAMBDA, PinState.X, PinState.X},
    };

    public static PinState[][] AND_5_STATES = {
            {PinState.ZERO, PinState.ZERO, PinState.ZERO, PinState.ZERO, PinState.ZERO},
            {PinState.ZERO, PinState.ONE, PinState.LAMBDA, PinState.EPSYLON, PinState.X},
            {PinState.ZERO, PinState.LAMBDA, PinState.LAMBDA, PinState.X, PinState.X},
            {PinState.ZERO, PinState.EPSYLON, PinState.X, PinState.EPSYLON, PinState.X},
            {PinState.ZERO, PinState.X, PinState.X, PinState.X, PinState.X},
    };

    public static PinState[] NOT_5_STATES = {
            PinState.ONE, PinState.ZERO, PinState.EPSYLON, PinState.LAMBDA, PinState.X
    };

    public static HashMap<PinState, Integer> idxs = new HashMap<PinState, Integer>();

    static {
        idxs.put(PinState.ZERO, 0);
        idxs.put(PinState.ONE, 1);
        idxs.put(PinState.LAMBDA, 2);
        idxs.put(PinState.EPSYLON, 3);
        idxs.put(PinState.X, 4);
    }

    public static char nameStart = 'a';

    public String name;

    public ArrayList<Input> inputs = new ArrayList<Input>();

    private int inputsCount = 3;

    public int rang = 0;

    public boolean isActive = false;

    public ElementType type = ElementType.AND;

    public Output output = new Output();

    public SchemeModel model = SchemeModel.TWO;

    public Element() {
        name = "" + nameStart;
        output.name = name + "_out";
        nameStart++;
        for (int i = 0; i < inputsCount; i++) {
            Input in = new Input();
            in.setModel(model);
            in.name = name + i;
            inputs.add(in);
        }
        simulate();

    }

    public Element(ElementType type) {
        super();
        setType(type);
    }

    public Element(ElementType type, int inputsCount) {
        super();
        setType(type);
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public int getInputsCount() {
        return inputsCount;
    }

    public void setInputsCount(int inputsCount) {
        if (inputsCount < 1) {
            throw new IllegalArgumentException("Inputs count cannot be less then 1.");
        }
        if (inputsCount > this.inputsCount) {
            for (int i = this.inputsCount; i < inputsCount; i++) {
                Input in = new Input();
                in.setModel(model);
                in.name = name + i;
                inputs.add(in);
            }
        } else {
            for (int i = inputsCount; i < this.inputsCount; i++) {
                inputs.remove(inputs.size() - 1);
            }
        }
        this.inputsCount = inputsCount;
    }

    public void addInput(Input input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null!");
        }
        inputs.get(getFreeInput()).setOutput(input.output);
    }

    public void simulate() {
        switch(model) {
            case TWO:
                for (Input in : inputs) {
                    if (in.output != null) {
                        in.setState(in.output.state);
                    }
                }
                break;
            case THREE:
                for (Input in : inputs) {
                    if (in.output != null) {
                        if (in.state != PinState.X && in.state != in.output.state) {
                            in.setState(PinState.X);
                        } else {
                            in.setState(in.output.state);
                        }
                    }
                }
                break;
            case FIVE:
                PinState[][] FIFTH_MODEL = {
                        {PinState.ZERO, PinState.EPSYLON, PinState.ZERO, PinState.ZERO, PinState.ZERO},
                        {PinState.LAMBDA, PinState.ONE, PinState.X, PinState.X, PinState.ONE},
                        {PinState.X, PinState.X, PinState.LAMBDA, PinState.X, PinState.LAMBDA},
                        {PinState.X, PinState.X, PinState.X, PinState.EPSYLON, PinState.X},
                        {PinState.X, PinState.X, PinState.X, PinState.X, PinState.X}
                };
                for (Input in : inputs) {
                    if (in.output != null) {
                        int i = idxs.get(in.output.state);
                        int j = idxs.get(in.state);
                        PinState res = FIFTH_MODEL[i][j];
                        in.state = res;
                    }
                }
                break;
        }
        switch (type) {
            case AND :
//                for (Input in : inputs) {
//                    if (in.state == PinState.ZERO) {
//                        output.setState(PinState.ZERO);
//                        return;
//                    }
//                }
//                for (Input in : inputs) {
//                    if (in.state == PinState.X) {
//                        output.setState(PinState.X);
//                        return;
//                    }
//                }
//                output.setState(PinState.ONE);
                PinState res = inputs.get(0).state;
                for (Input in : inputs) {
                    int i = idxs.get(res);
                    int j = idxs.get(in.state);
                    res = AND_5_STATES[i][j];
                }
                output.setState(res);
                break;
            case OR :
                PinState res2 = inputs.get(0).state;
                for (Input in : inputs) {
                    int i = idxs.get(res2);
                    int j = idxs.get(in.state);
                    res2 = OR_5_STATES[i][j];
                }
                output.setState(res2);
//                for (Input in : inputs) {
//                    if (in.state == PinState.ONE) {
//                        output.setState(PinState.ONE);
//                        return;
//                    }
//                }
//                for (Input in : inputs) {
//                    if (in.state == PinState.X) {
//                        output.setState(PinState.X);
//                        return;
//                    }
//                }
//                output.setState(PinState.ZERO);
                break;
            case NOT_AND :
                PinState res4 = inputs.get(0).state;
                for (Input in : inputs) {
                    int i = idxs.get(res4);
                    int j = idxs.get(in.state);
                    res4 = AND_5_STATES[i][j];
                }
                int s = idxs.get(res4);
                res4 = NOT_5_STATES[s];
                output.setState(res4);
                break;
            case NOT_OR:
                PinState res3 = inputs.get(0).state;
                for (Input in : inputs) {
                    int i = idxs.get(res3);
                    int j = idxs.get(in.state);
                    res3 = OR_5_STATES[i][j];
                }
                int k = idxs.get(res3);
                res3 = NOT_5_STATES[k];
                output.setState(res3);
                break;
        }
    }

    public void setModel(SchemeModel model) {
        this.model = model;
        for (Input in : inputs) {
            in.setModel(model);
            if (in.output == null) {
                in.setState(model.getValue());
            }
        }
        output.setModel(model);
//        simulate();
    }

    public int getFreeInput() {
        for (int i = 0; i < inputsCount; i++) {
            if (inputs.get(i).output == null) {
                return i;
            }
        }
        return -1;
    }



}
