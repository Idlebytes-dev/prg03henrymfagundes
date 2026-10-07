package br.com.ifba.usuario.repository;

import br.com.ifba.usuario.entity.Usuario;
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

    private Usuario usuarioComLogin(String login) {
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        return usuario;
    }
}
