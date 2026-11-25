package br.com.ifba;

import br.com.ifba.usuario.controller.PerfilIController;
import br.com.ifba.usuario.controller.UsuarioIController;
import java.util.function.BiFunction;
import javax.swing.JFrame;

/** Compatibilidade com os pontos de entrada das atividades anteriores. */
public final class ApexOptima {
    private ApexOptima() {
    }

    public static void main(String[] args) {
        ApexOptimaApplication.main(args);
    }

    public static void iniciar(String[] args,
            BiFunction<UsuarioIController, PerfilIController, ? extends JFrame> tela) {
        ApexOptimaApplication.iniciar(args, contexto -> tela.apply(
                contexto.getBean(UsuarioIController.class), contexto.getBean(PerfilIController.class)));
    }
}
