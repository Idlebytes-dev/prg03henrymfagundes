/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.sessao.entity;

import br.com.ifba.sessao.estado.SessionState;
import java.util.Objects;

/**
 * Represents a motorsport session and the state currently governing it.
 *
 * <p>The session delegates state-specific pit-lane time-loss queries to its
 * current {@link SessionState}.</p>
 */
public class Session {
    private SessionState currentState;

    /**
     * Creates a session with its initial state.
     *
     * @param initialState state that governs the session initially
     * @throws NullPointerException if {@code initialState} is {@code null}
     */
    public Session(SessionState initialState) {
        this.currentState = Objects.requireNonNull(initialState);
    }

    /**
     * Changes the state that governs this session.
     *
     * @param newState state to use from this point onward
     * @throws NullPointerException if {@code newState} is {@code null}
     */
    public void changeState(SessionState newState) {
        this.currentState = Objects.requireNonNull(newState);
    }

    /**
     * Returns the pit-lane time loss estimated by the current state.
     *
     * @return pit-lane time loss in seconds
     */
    public double getPitLaneLossSeconds() {
        return currentState.getPitLaneLossSeconds();
    }
}
