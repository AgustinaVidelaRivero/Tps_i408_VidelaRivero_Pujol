package anillo;

class Link {
    Object cargo;  
    Link prev;
    Link next;     

    Link(Object cargo) {
        this.cargo = cargo;  
        this.prev = null; 
        this.next = null; 
    }
}

public class Ring {
    private Link current; // Eslabón actual
    private int size;     // Contador de elementos en el anillo

    // Constructor
    public Ring() {
        this.current = null;
        this.size = 0;
    }

    public Ring next() {
        if (current == null) { // Si no hay elementos, no podemos avanzar
            throw new RuntimeException("El anillo está vacío, no hay elemento siguiente");
        }
        current = current.next; 
        return this;
    }

    public Object current() {
        if (current == null) { // Si no hay elementos, no hay un elemento actual
            throw new RuntimeException("El anillo está vacío, no hay elemento actual");
        }
        return current.cargo; // Devuelve el valor del eslabón actual
    }

    public Ring add(Object cargo) {
        Link newLink = new Link(cargo); // Crea un nuevo eslabón
        if (current == null) { // Si el anillo estaba vacío
            current = newLink; // Establece el eslabón actual al nuevo eslabón
            current.next = current; // Hace referencia a sí mismo, creando un anillo circular
        }
        if (current.next == current) {    // si solo hay un elemento
            current.next = newLink;
            current.prev = newLink;
            newLink.next = current;
            newLink.prev = current;
            current = newLink;  
        } else {
            // Se agregan enlaces por "izquierda" (cambiando el prev en vez del next)
            current.prev.next = newLink;
            newLink.next = current;
            newLink.prev = current.next;
            current = newLink;
        }
        size++;
        return this; 
    }

    public Ring remove() {                            // el current pasa a ser el next
        if (current == null) { // Si está vacío 
            throw new RuntimeException("No hay elementos para eliminar");
        }
        if (current.next == current) {  // Si hay solo un elemento
            current = null;
        } else {
            current.next.next = current.prev;
            current.prev.next = current.next;
            next();                         
        }
        size--; 
        return this; 
    }
    
    public int size() {
        return size; 
    }
}