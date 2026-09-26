/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.sessao.estado;

/**
 * Provides behavior shared by concrete race-session states.
 *
 * <p>Subclasses supply their own pit-lane time loss through
 * {@link SessionState#getPitLaneLossSeconds()} and inherit the common
 * calculation in {@link #addPitLaneLossTo(double)}.</p>
 */
public abstract class AbstractSessionState implements SessionState {

    /**
     * Adds this state's pit-lane time loss to an existing strategy time.
     *
     * @param strategyTimeSeconds strategy time before adding the pit-lane loss,
     *        in seconds
     * @return strategy time including this state's estimated pit-lane loss,
     *         in seconds
     */
    public double addPitLaneLossTo(double strategyTimeSeconds) {
        return strategyTimeSeconds + getPitLaneLossSeconds();
    }
}
