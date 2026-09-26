package br.com.ifba.sessao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.ifba.sessao.estado.GreenFlagState;
import br.com.ifba.sessao.estado.SafetyCarState;
import org.junit.jupiter.api.Test;

class SessionTest {
    private static final double TOLERANCE = 0.001;

    @Test
    void deveDelegarPerdaPitLaneAoEstadoInicial() {
        Session session = new Session(new GreenFlagState());

        assertEquals(21.5, session.getPitLaneLossSeconds(), TOLERANCE);
    }

    @Test
    void deveUsarNovoEstadoAposAlteracao() {
        Session session = new Session(new GreenFlagState());

        session.changeState(new SafetyCarState());

        assertEquals(12.0, session.getPitLaneLossSeconds(), TOLERANCE);
    }
}
