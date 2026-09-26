/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.sessao.estado;

/**
 * Represents a session neutralized by the Virtual Safety Car procedure.
 *
 * <p>Its current pit-lane loss is the provisional 12.0-second estimate
 * recorded in the project DER. The estimate is not calibrated for a specific
 * circuit.</p>
 */
public class VirtualSafetyCarState extends AbstractSessionState {

    /** {@inheritDoc} */
    @Override
    public double getPitLaneLossSeconds() {
        return 12.0;
    }
}
