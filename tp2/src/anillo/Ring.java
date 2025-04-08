package anillo;

import java.util.Stack;
import java.util.function.Supplier;

public class Ring {
    private Link current; // Eslabón actual
    private Stack<Supplier<Link>> stack;

    // Constructor
    public Ring() {
        this.current = new EmptyLink();
        this.stack = new Stack<>();
    }

    public Ring next() {
        current = current.next();
        return this;
    }

    public Object current() {
        return current.cargo(); // Devuelve el valor del eslabón actual
    }

    public Ring add(Object cargo) {
        current = current.add(cargo, stack);
        return this;
    }

    public Ring remove() {                            // el current pasa a ser el next
        current = current.remove(stack);
        return this;
    }
}