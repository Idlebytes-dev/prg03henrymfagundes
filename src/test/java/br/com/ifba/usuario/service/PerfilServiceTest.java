package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.exception.RegraNegocioException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PerfilServiceTest {
    private final PerfilService service = new PerfilService(new FakeDao.Perfis());

    @Test
    void cadastraEditaListaEExclui() {
        var piloto = service.cadastrar(" Piloto ", null);
        assertEquals("Piloto", piloto.getNome());
        assertNull(piloto.getDescricao());
        service.atualizar(piloto.getId(), "Piloto", "Titular");
        assertEquals("Titular", service.listar().getFirst().getDescricao());
        service.atualizar(piloto.getId(), "Reserva", "");
        assertEquals("Reserva", service.listar().getFirst().getNome());
        service.excluir(piloto.getId());
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void recusaNomesDuplicados() {
        var piloto = service.cadastrar("Piloto", "Equipe");
        var outro = service.cadastrar("Mecânico", null);
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(" Piloto ", null));
        assertThrows(RegraNegocioException.class, () -> service.atualizar(outro.getId(), "Piloto", null));
        assertEquals(List.of(piloto, outro), service.listar());
        assertEquals("Mecânico", outro.getNome());
    }

    @Test
    void validaNomeObrigatorioELimitesDeTamanho() {
        assertThrows(RegraNegocioException.class, () -> service.cadastrar("  ", ""));
        assertThrows(RegraNegocioException.class, () -> service.cadastrar("a".repeat(51), ""));
        assertThrows(RegraNegocioException.class, () -> service.cadastrar("Piloto", "a".repeat(256)));
        service.cadastrar("a".repeat(50), "b".repeat(255));
        assertEquals(1, service.listar().size());
    }

    @Test
    void recusaAlteracaoDePerfilAusente() {
        assertThrows(RegraNegocioException.class, () -> service.atualizar(10L, "Piloto", null));
        assertThrows(RegraNegocioException.class, () -> service.excluir(10L));
        assertTrue(service.listar().isEmpty());
    }
}
