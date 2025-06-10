// MatchNotFoundException.java
package org.udesa.unoback.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String message) {
        super(message);
    }
}