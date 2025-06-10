package anillo;

import java.util.Stack;
import java.util.function.Supplier;

abstract class Link {
    public abstract Link next();
    public abstract Link prev();
    public abstract Object cargo();
    public abstract Link add(Object cargo, Stack<Supplier<Link>> stack);
    public abstract Link remove(Stack<Supplier<Link>> stack);
}

class EmptyLink extends Link {
    public EmptyLink() {}

    public Link next(){
        throw new RuntimeException("El anillo está vacío, no hay elemento siguiente");
    }

    public Link prev(){
        throw new RuntimeException("El anillo está vacío, no hay elemento anterior");
    }

    public Object cargo(){ 
    }

    public Link add(Object cargo, Stack<Supplier<Link>> stack){
        ValidLink newLink = new ValidLink(cargo);
        stack.push(EmptyLink::new); //si se elimina el unico nodo --> pasamos al vacio
        return new ValidLink(cargo);
    }

    public Link remove(Stack<Supplier<Link>> stack){
        throw new RuntimeException("No hay elementos para eliminar");
    }
}

class ValidLink extends Link {
    public Object cargo;
    public ValidLink prev;
    public ValidLink next;

    public ValidLink(Object cargo){
        this.cargo = cargo;
        this.next = this;
        this.prev = this;
    }

    public Link next(){
        return next;
    }

    public Link prev(){
        return prev;
    }

    public Object cargo(){
        return cargo;
    }

    public Link add(Object cargo, Stack<Supplier<Link>> stack) {
        ValidLink newLink = new ValidLink(cargo);
//        newLink.next = this;
//        newLink.prev = this.prev;
//
//        this.prev.next = newLink;
//        this.prev = newLink;

        linkBeforeSelf(newLink);
        stack.push(() -> this.next); // volvés al que está después del eliminado
        return newLink; // el nuevo current pasa a ser el que agrego
    }

    public Link remove(Stack<Supplier<Link>> stack){

        unlinkSelf();
        return stack.pop().get();
        }

    private void unlinkSelf() {
        this.prev.next = this.next;
        this.next.prev = this.prev;
    }

    private void linkBeforeSelf(ValidLink newLink) {
        newLink.next = this;
        newLink.prev = this.prev;

        this.prev.next = newLink;
        this.prev = newLink;
    }
}


