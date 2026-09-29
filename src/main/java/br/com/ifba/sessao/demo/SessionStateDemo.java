/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.sessao.demo;

import br.com.ifba.sessao.estado.AbstractSessionState;
import br.com.ifba.sessao.estado.GreenFlagState;
import br.com.ifba.sessao.estado.SafetyCarState;
import br.com.ifba.sessao.estado.VirtualSafetyCarState;

/**
 * Shows how different session states change the result of the same calculation.
 */
public class SessionStateDemo {

    /**
     * Adds the given state's pit-lane loss to a strategy time.
     *
     * @param baseTimeSeconds strategy time before the pit-lane loss, in seconds
     * @param state session state used for the calculation
     * @return strategy time including the pit-lane loss, in seconds
     */
    public static double calculateStrategyTime(
            double baseTimeSeconds, AbstractSessionState state) {
        return state.addPitLaneLossTo(baseTimeSeconds);
    }

    /**
     * Prints the calculated time for each session state.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        double baseTimeSeconds = 100.0;

        System.out.println("Green flag: "
                + calculateStrategyTime(baseTimeSeconds, new GreenFlagState()));
        System.out.println("Safety Car: "
                + calculateStrategyTime(baseTimeSeconds, new SafetyCarState()));
        System.out.println("VSC: "
                + calculateStrategyTime(baseTimeSeconds, new VirtualSafetyCarState()));
    }
}
