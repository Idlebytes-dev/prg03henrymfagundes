/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.sessao.estado;

/**
 * Defines the behavior shared by race-session flag states.
 *
 * <p>Each state reports the pit-lane time loss used by the current strategy
 * calculation. The values are provisional estimates from the project DER and
 * are not calibrated for a specific circuit.</p>
 */
public interface SessionState {

    /**
     * Returns the estimated pit-lane time loss for this state.
     *
     * @return pit-lane time loss in seconds
     */
    double getPitLaneLossSeconds();
}
