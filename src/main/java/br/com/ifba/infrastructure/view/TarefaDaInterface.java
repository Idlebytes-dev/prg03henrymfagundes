package br.com.ifba.infrastructure.view;

import br.com.ifba.infrastructure.exception.PersistenciaException;
import java.awt.Component;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/** Executa acesso a dados fora da thread gráfica e apresenta apenas erros do domínio. */
public final class TarefaDaInterface {
    private TarefaDaInterface() {
    }

    public static <T> void executar(Component tela, AbstractButton botao,
                                   Supplier<T> operacao, Consumer<T> sucesso) {
        botao.setEnabled(false);
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() {
                return operacao.get();
            }

            @Override
            protected void done() {
                botao.setEnabled(true);
                if (!tela.isDisplayable()) return;
                try {
                    sucesso.accept(get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ex) {
                    String mensagem = ex.getCause() instanceof PersistenciaException
                            ? ex.getCause().getMessage() : "Não foi possível concluir a operação.";
                    JOptionPane.showMessageDialog(tela, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
