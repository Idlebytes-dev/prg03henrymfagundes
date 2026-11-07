package br.com.ifba.infrastructure.dao;

import br.com.ifba.infrastructure.exception.PersistenciaException;
import br.com.ifba.usuario.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GenericDaoTest {
    @ParameterizedTest
    @ValueSource(strings = {"persist", "merge", "remove", "commit", "begin", "getTransaction"})
    void deveTraduzirFalhasDeEscritaReverterEFechar(String etapa) {
        JpaSimulado jpa = new JpaSimulado();
        var causa = new PersistenceException("Falha simulada em " + etapa);
        jpa.falhas.put(etapa, causa);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        PersistenciaException erro = assertThrows(PersistenciaException.class, () -> {
            switch (etapa) {
                case "merge" -> jpa.dao.update(usuario);
                case "remove" -> jpa.dao.delete(usuario);
                default -> jpa.dao.save(usuario);
            }
        });

        assertSame(causa, erro.getCause());
        assertEquals("close", jpa.chamadas.getLast());
        assertFalse(jpa.ativa);
        if (List.of("persist", "merge", "remove", "commit").contains(etapa)) {
            assertTrue(jpa.chamadas.contains("rollback"));
        }
    }

    @Test
    void devePreservarCausaMesmoQuandoRollbackEFechamentoFalham() {
        JpaSimulado jpa = new JpaSimulado();
        var causa = new PersistenceException("Falha original");
        jpa.falhas.put("persist", causa);
        jpa.falhas.put("rollback", new IllegalStateException("Falha rollback"));
        jpa.falhas.put("close", new IllegalStateException("Falha close"));

        var erro = assertThrows(PersistenciaException.class, () -> jpa.dao.save(new Usuario()));

        assertSame(causa, erro.getCause());
        assertEquals(2, erro.getSuppressed().length);
        assertEquals(List.of("getTransaction", "begin", "persist", "rollback", "close"), jpa.chamadas);
    }

    @Test
    void deveTraduzirFalhaAoAbrirManager() {
        var causa = new PersistenceException("Banco indisponível");
        GenericDao<Usuario> dao = new GenericDao<>(Usuario.class, () -> { throw causa; });

        assertSame(causa, assertThrows(PersistenciaException.class,
                () -> dao.save(new Usuario())).getCause());
    }

    @Test
    void deveFecharManagerQuandoLeituraFalhaSemAbrirTransacao() {
        JpaSimulado jpa = new JpaSimulado();
        var causa = new PersistenceException("Falha de leitura");
        jpa.falhas.put("find", causa);

        assertSame(causa, assertThrows(PersistenciaException.class,
                () -> jpa.dao.findById(1L)).getCause());
        assertEquals(List.of("find", "close"), jpa.chamadas);
    }

    @Test
    void deveRetornarEntidadeSalvaAposCommitEFechamento() {
        JpaSimulado jpa = new JpaSimulado();
        Usuario usuario = new Usuario();

        assertSame(usuario, jpa.dao.save(usuario));
        assertEquals(List.of("getTransaction", "begin", "persist", "commit", "close"), jpa.chamadas);
    }

    @Test
    void deveRetornarResultadoDoMergeEExcluirInstanciaGerenciada() {
        JpaSimulado jpa = new JpaSimulado();
        Usuario desanexado = new Usuario();
        desanexado.setId(1L);

        assertSame(jpa.gerenciado, jpa.dao.update(desanexado));
        jpa.chamadas.clear();
        jpa.dao.delete(desanexado);

        assertEquals(List.of("getTransaction", "begin", "find", "remove", "commit", "close"), jpa.chamadas);
        assertSame(jpa.gerenciado, jpa.removido);
    }

    @Test
    void deveTraduzirFalhaAoFecharAposLeitura() {
        JpaSimulado jpa = new JpaSimulado();
        var causa = new IllegalStateException("Falha close");
        jpa.falhas.put("close", causa);

        assertSame(causa, assertThrows(PersistenciaException.class,
                () -> jpa.dao.findById(1L)).getCause());
    }

    private static class JpaSimulado {
        final List<String> chamadas = new ArrayList<>();
        final Map<String, RuntimeException> falhas = new HashMap<>();
        final Usuario gerenciado = new Usuario();
        Usuario removido;
        boolean ativa;

        final EntityTransaction transacao = proxy(EntityTransaction.class, (objeto, metodo, args) -> {
            if (metodo.getName().equals("isActive")) return ativa;
            registrar(metodo.getName());
            switch (metodo.getName()) {
                case "begin" -> ativa = true;
                case "commit", "rollback" -> ativa = false;
                default -> throw new UnsupportedOperationException(metodo.getName());
            }
            return null;
        });

        final EntityManager manager = proxy(EntityManager.class, (objeto, metodo, args) -> {
            registrar(metodo.getName());
            return switch (metodo.getName()) {
                case "getTransaction" -> transacao;
                case "persist", "close" -> null;
                case "merge", "find" -> gerenciado;
                case "remove" -> { removido = (Usuario) args[0]; yield null; }
                default -> throw new UnsupportedOperationException(metodo.getName());
            };
        });

        final GenericDao<Usuario> dao = new GenericDao<>(Usuario.class, () -> manager);

        private void registrar(String etapa) {
            chamadas.add(etapa);
            if (falhas.containsKey(etapa)) throw falhas.get(etapa);
        }

        private static <T> T proxy(Class<T> tipo, InvocationHandler handler) {
            return tipo.cast(Proxy.newProxyInstance(tipo.getClassLoader(), new Class<?>[]{tipo}, handler));
        }
    }
}
