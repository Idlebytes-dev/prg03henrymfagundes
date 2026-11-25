package br.com.ifba.usuario.service;

import br.com.ifba.infrastructure.exception.RegraNegocioException;
import br.com.ifba.usuario.dto.DadosUsuario;
import br.com.ifba.usuario.entity.Status;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {
    private final UsuarioService service = new UsuarioService(new FakeDao.Usuarios());

    private DadosUsuario dados(String login, String senha, String repetir) {
        return new DadosUsuario(" Maria ", "123.456.789-10", "Outro", "01/01/2000", "1234",
                "maria@example.com", login, senha, repetir, Status.ATIVO);
    }

    @Test
    void cadastraNormalizaFiltraEExclui() {
        var maria = service.cadastrar(dados(" piloto ", "123", "123"));
        var outro = service.cadastrar(dados("paulo", "123", "123"));
        assertEquals("Maria", maria.getName());
        assertEquals("piloto", maria.getLogin());
        assertEquals(Status.INATIVO, maria.getStatus());
        assertEquals(List.of(maria), service.listar(" ILO "));
        assertEquals(List.of(maria, outro), service.listar("  "));
        service.excluir(maria.getId());
        assertEquals(List.of(outro), service.listar(null));
        assertThrows(RegraNegocioException.class, () -> service.buscar(maria.getId()));
    }

    @ParameterizedTest
    @CsvSource({"ROOT123,abc,abc", "' ',abc,abc", "piloto,'',''", "piloto,'   ','   '", "piloto,abc,xyz"})
    void recusaCadastroInvalido(String login, String senha, String repetir) {
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(dados(login, senha, repetir)));
        assertTrue(service.listar("").isEmpty());
    }

    @Test
    void recusaCpfInvalido() {
        var invalido = new DadosUsuario("Maria", "123", "Outro", "01/01/2000", "1234",
                "maria@example.com", "piloto", "abc", "abc", Status.INATIVO);
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(invalido));
        assertTrue(service.listar(null).isEmpty());
    }

    @Test
    void recusaLoginDuplicadoNoCadastroENaEdicao() {
        var dadosMaria = dados("maria", "abc", "abc");
        service.cadastrar(dadosMaria);
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(dados(" maria ", "123", "123")));
        var outra = service.cadastrar(dados("outra", "antiga", "antiga"));
        assertThrows(RegraNegocioException.class, () -> service.atualizar(outra.getId(), dadosMaria));
        assertEquals(2, service.listar("").size());
        assertTrue(service.autenticar("outra", "antiga"));
    }

    @Test
    void editaPreservandoSenhaOuTrocandoComConfirmacao() {
        Long id = service.cadastrar(dados("piloto", "antiga", "antiga")).getId();
        assertEquals(Status.ATIVO, service.atualizar(id, dados("piloto", "", "")).getStatus());
        assertTrue(service.autenticar("piloto", "antiga"));
        assertThrows(RegraNegocioException.class, () -> service.atualizar(id, dados("piloto", "", "nova")));
        service.atualizar(id, dados("piloto2", "nova", "nova"));
        assertFalse(service.autenticar("piloto", "antiga"));
        assertFalse(service.autenticar("piloto2", "antiga"));
        assertTrue(service.autenticar("piloto2", "nova"));
    }

    @Test
    void recusaAlteracaoDeUsuarioAusente() {
        assertThrows(RegraNegocioException.class, () -> service.atualizar(10L, dados("ana", "", "")));
        assertThrows(RegraNegocioException.class, () -> service.excluir(10L));
        assertTrue(service.listar(null).isEmpty());
    }
}
