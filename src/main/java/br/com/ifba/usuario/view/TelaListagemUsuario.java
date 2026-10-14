package br.com.ifba.usuario.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Componentes da listagem; a consulta ao banco será implementada na Parte 2. */
public class TelaListagemUsuario extends JFrame {

    public TelaListagemUsuario() {
        super("ApexOptima - Usuários");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel conteudo = new JPanel(new BorderLayout(8, 8));
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel cabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JTextField busca = new JTextField(22);
        JLabel rotuloBusca = new JLabel("Nome ou login:");
        rotuloBusca.setLabelFor(busca);
        busca.getAccessibleContext().setAccessibleName("Buscar usuário por nome ou login");
        JButton buscar = new JButton("Buscar");
        buscar.setEnabled(false);
        JButton novo = new JButton("Novo usuário");
        novo.addActionListener(event -> {
            TelaCadastroUsuario cadastro = new TelaCadastroUsuario();
            cadastro.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            cadastro.setLocationRelativeTo(this);
            cadastro.setVisible(true);
        });
        cabecalho.add(rotuloBusca);
        cabecalho.add(busca);
        cabecalho.add(buscar);
        cabecalho.add(novo);

        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Nome", "Login", "Email", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setFillsViewportHeight(true);
        tabela.setPreferredScrollableViewportSize(new Dimension(680, 280));
        tabela.getAccessibleContext().setAccessibleName("Lista de usuários");

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.add(new JLabel("Nenhum usuário carregado."), BorderLayout.WEST);
        JButton editar = new JButton("Editar usuário");
        editar.setEnabled(false);
        rodape.add(editar, BorderLayout.EAST);
        conteudo.add(cabecalho, BorderLayout.NORTH);
        conteudo.add(new JScrollPane(tabela), BorderLayout.CENTER);
        conteudo.add(rodape, BorderLayout.SOUTH);
        setContentPane(conteudo);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new TelaListagemUsuario().setVisible(true));
    }
}
