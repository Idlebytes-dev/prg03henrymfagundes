package br.com.ifba;

import br.com.ifba.login.view.TelaLogin;
import java.awt.EventQueue;
import java.util.Arrays;
import java.util.function.Function;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApexOptimaApplication {
    public static void main(String[] args) {
        iniciar(args, TelaLogin.class);
    }

    public static void iniciar(String[] args, Class<? extends JFrame> telaInicial) {
        iniciar(args, contexto -> contexto.getBean(telaInicial));
    }

    static void iniciar(String[] args, Function<ConfigurableApplicationContext, ? extends JFrame> abrirTela) {
        var contexto = new SpringApplicationBuilder(ApexOptimaApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .run(args);
        if (Arrays.asList(args).contains("--validar-banco")) {
            contexto.close();
            return;
        }
        EventQueue.invokeLater(() -> {
            try {
                for (var tema : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(tema.getName())) {
                        UIManager.setLookAndFeel(tema.getClassName());
                        break;
                    }
                }
            } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(ApexOptimaApplication.class.getName())
                        .log(java.util.logging.Level.WARNING, "Não foi possível aplicar o tema Nimbus.", ex);
            }
            abrirTela.apply(contexto).setVisible(true);
        });
    }
}
