package juegoUnoAgus;

//public interface ControladorTurnos {
//    Jugador siguiente(Jugador actual);
//    ControladorTurnos opuesto();
//}
public abstract class ControladorTurnos {
    protected ControladorTurnos opuesto;

    public void setOpuesto(ControladorTurnos opuesto) {
        this.opuesto = opuesto;
    }

    public ControladorTurnos opuesto() {
        return opuesto;
    }

    public abstract Jugador siguiente(Jugador actual);
}

//class ControladorDerecha implements ControladorTurnos {
//    private ControladorTurnos opuesto;
//
//    public void setOpuesto(ControladorTurnos opuesto) {
//        this.opuesto = opuesto;
//    }
//
//    public Jugador siguiente(Jugador actual) {
//        return actual.derecha();
//    }
//
//    public ControladorTurnos opuesto() {
//        return opuesto;
//    }
//}
//
//class ControladorIzquierda implements ControladorTurnos {
//    private ControladorTurnos opuesto;
//
//    public void setOpuesto(ControladorTurnos opuesto) {
//        this.opuesto = opuesto;
//    }
//
//    public Jugador siguiente(Jugador actual) {
//        return actual.izquierda();
//    }
//
//    public ControladorTurnos opuesto() {
//        return opuesto;
//    }
//}

