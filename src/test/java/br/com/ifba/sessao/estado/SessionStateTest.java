package br.com.ifba.sessao.estado;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SessionStateTest {

    private static final double TOLERANCE = 0.001;

    @Test
    void deveSomarPerdaPitLaneUsandoMetodoHerdadoDaClasseAbstrata() {
        AbstractSessionState state = new GreenFlagState();

        double totalStrategyTime = state.addPitLaneLossTo(100.0);

        assertEquals(121.5, totalStrategyTime, TOLERANCE);
    }

    @Test
    void deveRetornarPerdaEspecificaParaBandeiraVerde() {
        SessionState state = new GreenFlagState();

        assertEquals(21.5, state.getPitLaneLossSeconds(), TOLERANCE);
    }

    @Test
    void deveRetornarPerdaEspecificaParaSafetyCar() {
        SessionState state = new SafetyCarState();

        assertEquals(12.0, state.getPitLaneLossSeconds(), TOLERANCE);
    }

    @Test
    void deveRetornarPerdaEspecificaParaVirtualSafetyCar() {
        SessionState state = new VirtualSafetyCarState();

        assertEquals(12.0, state.getPitLaneLossSeconds(), TOLERANCE);
    }
}
