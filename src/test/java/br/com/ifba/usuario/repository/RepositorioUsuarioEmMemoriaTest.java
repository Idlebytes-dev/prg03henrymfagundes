package br.com.ifba.usuario.repository;

import br.com.ifba.usuario.entity.Usuario;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioUsuarioEmMemoriaTest {

    @Test
    void deveListarUsuarioCadastrado() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();
        Usuario usuario = usuarioComLogin("henry");

        repositorio.cadastrar(usuario);

        assertEquals(1, repositorio.listarTodos().size());
        assertSame(usuario, repositorio.listarTodos().get(0));
    }

    @Test
    void deveBuscarUsuarioCorretoEntreDoisCadastros() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();
        Usuario primeiro = usuarioComLogin("henry");
        Usuario segundo = usuarioComLogin("ana");
        repositorio.cadastrar(primeiro);
        repositorio.cadastrar(segundo);

        assertSame(primeiro, repositorio.buscarPorLogin("henry"));
        assertSame(segundo, repositorio.buscarPorLogin("ana"));
    }

    @Test
    void deveRetornarNullQuandoLoginNaoExiste() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();
        repositorio.cadastrar(usuarioComLogin("henry"));

        assertNull(repositorio.buscarPorLogin("inexistente"));
    }

    @Test
    void deveRejeitarLoginDuplicadoSemAlterarCadastros() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();
        Usuario original = usuarioComLogin("henry");
        repositorio.cadastrar(original);

        assertThrows(IllegalArgumentException.class,
                () -> repositorio.cadastrar(usuarioComLogin("henry")));

        assertEquals(1, repositorio.listarTodos().size());
        assertSame(original, repositorio.listarTodos().get(0));
        assertSame(original, repositorio.buscarPorLogin("henry"));
    }

    @Test
    void deveIniciarSemUsuarios() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();

        assertTrue(repositorio.listarTodos().isEmpty());
        assertNull(repositorio.buscarPorLogin("henry"));
    }

    @Test
    void deveProtegerColecoesInternasAoListar() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();
        Usuario usuario = usuarioComLogin("henry");
        repositorio.cadastrar(usuario);
        List<Usuario> resultado = repositorio.listarTodos();

        assertThrows(UnsupportedOperationException.class, resultado::clear);
        repositorio.cadastrar(usuarioComLogin("ana"));

        assertEquals(1, resultado.size());
        assertEquals(2, repositorio.listarTodos().size());
        assertSame(usuario, repositorio.buscarPorLogin("henry"));
    }

    @Test
    void deveRejeitarUsuarioOuLoginAusenteSemAlterarColecoes() {
        RepositorioUsuarioEmMemoria repositorio = new RepositorioUsuarioEmMemoria();

        assertThrows(IllegalArgumentException.class, () -> repositorio.cadastrar(null));
        assertThrows(IllegalArgumentException.class, () -> repositorio.cadastrar(new Usuario()));
        assertThrows(IllegalArgumentException.class,
                () -> repositorio.cadastrar(usuarioComLogin("   ")));

        assertTrue(repositorio.listarTodos().isEmpty());
        assertNull(repositorio.buscarPorLogin(null));
        assertNull(repositorio.buscarPorLogin("   "));
    }

    private Usuario usuarioComLogin(String login) {
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        return usuario;
    }
}
