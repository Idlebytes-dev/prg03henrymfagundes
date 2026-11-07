package br.com.ifba.usuario.view;

import br.com.ifba.ApexOptima;
import br.com.ifba.infrastructure.view.TarefaDaInterface;
import br.com.ifba.usuario.dao.UsuarioDao;
import br.com.ifba.usuario.dao.UsuarioIDao;
import br.com.ifba.usuario.entity.Usuario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/** Consulta, edição e exclusão por contrato DAO, sem acesso direto ao JPA. */
public class TelaListagemUsuario extends JFrame {
    private final UsuarioIDao usuarioDao;
    private final JTextField busca = new JTextField(22);
    private final JButton buscar = new JButton("Buscar");
    private final JButton editar = new JButton("Editar usuário");
    private final JButton excluir = new JButton("Excluir usuário");
    private final JLabel resumo = new JLabel("Nenhum usuário carregado.");
    private final DefaultTableModel modelo;
    private final JTable tabela;
    private List<Usuario> usuarios = List.of();

    public TelaListagemUsuario() {
        this(new UsuarioDao());
    }

    public TelaListagemUsuario(UsuarioIDao usuarioDao) {
        super("ApexOptima - Usuários");
        this.usuarioDao = Objects.requireNonNull(usuarioDao);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel conteudo = new JPanel(new BorderLayout(8, 8));
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel rotuloBusca = new JLabel("Nome ou login:");
        rotuloBusca.setLabelFor(busca);
        busca.getAccessibleContext().setAccessibleName("Buscar usuário por nome ou login");
        buscar.addActionListener(event -> carregar());
        busca.addActionListener(event -> carregar());
        JButton novo = new JButton("Novo usuário");
        novo.addActionListener(event -> {
            TelaCadastroUsuario cadastro = new TelaCadastroUsuario(usuarioDao);
            cadastro.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            cadastro.setLocationRelativeTo(this);
            atualizarAoFechar(cadastro);
            cadastro.setVisible(true);
        });
        cabecalho.add(rotuloBusca);
        cabecalho.add(busca);
        cabecalho.add(buscar);
        cabecalho.add(novo);

        modelo = new DefaultTableModel(
                new String[]{"Nome", "Login", "Email", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFillsViewportHeight(true);
        tabela.setPreferredScrollableViewportSize(new Dimension(680, 280));
        tabela.getAccessibleContext().setAccessibleName("Lista de usuários");
        tabela.getSelectionModel().addListSelectionListener(event -> atualizarSelecao());

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.add(resumo, BorderLayout.WEST);
        editar.setEnabled(false);
        excluir.setEnabled(false);
        editar.addActionListener(event -> editarSelecionado());
        excluir.addActionListener(event -> excluirSelecionado());
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(editar);
        acoes.add(excluir);
        rodape.add(acoes, BorderLayout.EAST);
        conteudo.add(cabecalho, BorderLayout.NORTH);
        conteudo.add(new JScrollPane(tabela), BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);
        setContentPane(conteudo);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent event) {
                carregar();
            }
        });
    }

    private void carregar() {
        if (!buscar.isEnabled()) return;
        String termo = busca.getText().trim();
        TarefaDaInterface.executar(this, buscar,
                () -> termo.isEmpty() ? usuarioDao.findAll() : usuarioDao.findByNomeOuLogin(termo),
                this::mostrarUsuarios);
    }

    private void mostrarUsuarios(List<Usuario> encontrados) {
        usuarios = encontrados;
        modelo.setRowCount(0);
        for (Usuario usuario : usuarios) {
            modelo.addRow(new Object[]{usuario.getName(), usuario.getLogin(), usuario.getEmail(), usuario.getStatus()});
        }
        resumo.setText(usuarios.size() + " usuário(s) encontrado(s).");
        atualizarSelecao();
    }

    private void atualizarSelecao() {
        boolean selecionado = tabela.getSelectedRow() >= 0 && buscar.isEnabled();
        editar.setEnabled(selecionado);
        excluir.setEnabled(selecionado);
    }

    private void editarSelecionado() {
        if (!buscar.isEnabled() || tabela.getSelectedRow() < 0) return;
        Long id = usuarios.get(tabela.getSelectedRow()).getId();
        TarefaDaInterface.executar(this, buscar, () -> usuarioDao.findById(id), usuario -> {
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "O usuário não foi encontrado. Atualize a listagem.");
                return;
            }
            var tela = new TelaEdicaoUsuario(usuarioDao, usuario);
            atualizarAoFechar(tela);
            tela.setLocationRelativeTo(this);
            tela.setVisible(true);
        });
    }

    private void atualizarAoFechar(JFrame tela) {
        tela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                if (isDisplayable()) carregar();
            }
        });
    }

    private void excluirSelecionado() {
        if (!buscar.isEnabled() || tabela.getSelectedRow() < 0) return;
        Usuario usuario = usuarios.get(tabela.getSelectedRow());
        if (JOptionPane.showConfirmDialog(this, "Excluir o usuário " + usuario.getLogin() + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        String termo = busca.getText().trim();
        TarefaDaInterface.executar(this, buscar, () -> {
            usuarioDao.delete(usuario);
            return termo.isEmpty() ? usuarioDao.findAll() : usuarioDao.findByNomeOuLogin(termo);
        }, this::mostrarUsuarios);
    }

    public static void main(String[] args) {
        ApexOptima.iniciar(args, TelaListagemUsuario::new);
    }
}
