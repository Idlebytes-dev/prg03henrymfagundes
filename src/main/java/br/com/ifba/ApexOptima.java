package br.com.ifba;

import br.com.ifba.infrastructure.JPAUtil;
import br.com.ifba.login.view.TelaLogin;
import br.com.ifba.usuario.controller.PerfilController;
import br.com.ifba.usuario.controller.PerfilIController;
import br.com.ifba.usuario.controller.UsuarioController;
import br.com.ifba.usuario.controller.UsuarioIController;
import br.com.ifba.usuario.dao.PerfilDao;
import br.com.ifba.usuario.dao.UsuarioDao;
import br.com.ifba.usuario.service.PerfilService;
import br.com.ifba.usuario.service.UsuarioService;
import java.awt.EventQueue;
import java.util.List;
import java.util.function.BiFunction;
import javax.swing.JFrame;
import javax.swing.UIManager;

/** Inicializa migrations/JPA antes das telas e fecha o factory ao encerrar o processo. */
public final class ApexOptima {
    private ApexOptima() {
    }

    public static void main(String[] args) {
        iniciar(args, TelaLogin::new);
    }

    public static void iniciar(String[] args,
            BiFunction<UsuarioIController, PerfilIController, ? extends JFrame> tela) {
        JPAUtil.inicializar();
        if (List.of(args).contains("--validar-banco")) {
            JPAUtil.fechar();
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(JPAUtil::fechar, "fechar-jpa"));
        try {
            for (var info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ApexOptima.class.getName())
                    .log(java.util.logging.Level.WARNING, "Não foi possível aplicar o tema Nimbus.", ex);
        }
        var usuarios = new UsuarioController(new UsuarioService(new UsuarioDao()));
        var perfis = new PerfilController(new PerfilService(new PerfilDao()));
        EventQueue.invokeLater(() -> tela.apply(usuarios, perfis).setVisible(true));
    }
}
