/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.sessao.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.ifba.sessao.estado.AbstractSessionState;
import br.com.ifba.sessao.estado.GreenFlagState;
import br.com.ifba.sessao.estado.SafetyCarState;
import br.com.ifba.sessao.estado.VirtualSafetyCarState;
import org.junit.jupiter.api.Test;

/**
 * Verifies the strategy time calculated through the general session-state type.
 */
class SessionStateDemoTest {

    private static final double TOLERANCE = 0.001;

    @Test
    void deveCalcularTempoDaEstrategiaComBandeiraVerde() {
        AbstractSessionState state = new GreenFlagState();

        double strategyTime = SessionStateDemo.calculateStrategyTime(100.0, state);

        assertEquals(121.5, strategyTime, TOLERANCE);
    }

    @Test
    void deveCalcularTempoDaEstrategiaComSafetyCar() {
        AbstractSessionState state = new SafetyCarState();

        double strategyTime = SessionStateDemo.calculateStrategyTime(100.0, state);

        assertEquals(112.0, strategyTime, TOLERANCE);
    }

    @Test
    void deveCalcularTempoDaEstrategiaComVirtualSafetyCar() {
        AbstractSessionState state = new VirtualSafetyCarState();

        double strategyTime = SessionStateDemo.calculateStrategyTime(100.0, state);

        assertEquals(112.0, strategyTime, TOLERANCE);
    }
}
