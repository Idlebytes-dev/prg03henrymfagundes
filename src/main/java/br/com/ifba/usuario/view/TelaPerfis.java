package br.com.ifba.usuario.view;

import br.com.ifba.infrastructure.view.TarefaDaInterface;
import br.com.ifba.usuario.controller.PerfilIController;
import br.com.ifba.usuario.entity.Perfil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** Selecionar um perfil abre seus dados para edição. Novo limpa a seleção. */
@Component
@Scope("prototype")
public final class TelaPerfis extends JFrame {
    private final PerfilIController controller;
    private final JList<String> lista = new JList<>();
    private List<Perfil> perfis = List.of();
    private final JTextField nome = new JTextField(24);
    private final JTextField descricao = new JTextField(32);
    private final JButton salvar = new JButton("Salvar");
    private final JButton excluir = new JButton("Excluir");
    private final JButton novo = new JButton("Novo");
    private final JButton listar = new JButton("Atualizar lista");

    public TelaPerfis(PerfilIController controller) {
        super("ApexOptima - Perfis");
        this.controller = Objects.requireNonNull(controller);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setVisibleRowCount(7);
        lista.getAccessibleContext().setAccessibleName("Perfis cadastrados");
        lista.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) mostrarSelecao();
        });

        var formulario = new JPanel(new GridLayout(0, 1, 4, 4));
        var rotuloNome = new JLabel("Nome do perfil");
        rotuloNome.setLabelFor(nome);
        formulario.add(rotuloNome);
        formulario.add(nome);
        var rotuloDescricao = new JLabel("Descrição");
        rotuloDescricao.setLabelFor(descricao);
        formulario.add(rotuloDescricao);
        formulario.add(descricao);

        var acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        novo.addActionListener(event -> {
            lista.clearSelection();
            mostrarSelecao();
            nome.requestFocusInWindow();
        });
        listar.addActionListener(event -> carregar());
        salvar.addActionListener(event -> salvar());
        excluir.addActionListener(event -> excluir());
        acoes.add(listar);
        acoes.add(novo);
        acoes.add(excluir);
        acoes.add(salvar);
        // Todas as ações compartilham o mesmo formulário e a mesma seleção.
        salvar.addPropertyChangeListener("enabled", event -> {
            boolean livre = salvar.isEnabled();
            novo.setEnabled(livre);
            listar.setEnabled(livre);
            excluir.setEnabled(livre && !lista.isSelectionEmpty());
            lista.setEnabled(livre);
            nome.setEnabled(livre);
            descricao.setEnabled(livre);
        });
        excluir.setEnabled(false);

        var painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        painel.add(new JScrollPane(lista), BorderLayout.NORTH);
        painel.add(formulario, BorderLayout.CENTER);
        painel.add(acoes, BorderLayout.SOUTH);
        setContentPane(painel);
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent event) {
                carregar();
            }
        });
    }

    private Perfil selecionado() {
        int posicao = lista.getSelectedIndex();
        return posicao < 0 ? null : perfis.get(posicao);
    }

    private void mostrarSelecao() {
        Perfil perfil = selecionado();
        nome.setText(perfil == null ? "" : perfil.getNome());
        descricao.setText(perfil == null ? "" : perfil.getDescricao());
        excluir.setEnabled(perfil != null && salvar.isEnabled());
    }

    private void carregar() {
        TarefaDaInterface.executar(this, salvar, controller::listar, this::mostrarLista);
    }

    private void mostrarLista(List<Perfil> encontrados) {
        lista.clearSelection();
        perfis = encontrados;
        lista.setListData(perfis.stream().map(Perfil::getNome).toArray(String[]::new));
        mostrarSelecao();
    }

    private void salvar() {
        Perfil selecionado = selecionado();
        String novoNome = nome.getText();
        String novaDescricao = descricao.getText();
        TarefaDaInterface.executar(this, salvar, () -> {
            if (selecionado == null) {
                controller.cadastrar(novoNome, novaDescricao);
            } else {
                controller.atualizar(selecionado.getId(), novoNome, novaDescricao);
            }
            return controller.listar();
        }, this::mostrarLista);
    }

    private void excluir() {
        Perfil perfil = selecionado();
        if (perfil == null || JOptionPane.showConfirmDialog(this, "Excluir o perfil " + perfil.getNome() + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        TarefaDaInterface.executar(this, salvar, () -> {
            controller.excluir(perfil.getId());
            return controller.listar();
        }, this::mostrarLista);
    }
}
