package br.com.ifba.usuario.view;

import br.com.ifba.usuario.entity.Status;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Componentes da edição; carregar e salvar os dados será feito na Parte 2. */
public class TelaEdicaoUsuario extends JFrame {

    public TelaEdicaoUsuario() {
        super("ApexOptima - Editar usuário");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        adicionarCampo(formulario, 0, "Nome completo:", new JTextField(28));
        adicionarCampo(formulario, 1, "CPF:", new JTextField(28));
        adicionarCampo(formulario, 2, "Gênero:",
                new JComboBox<>(new String[]{"Selecione", "Masculino", "Feminino", "Outro"}));
        adicionarCampo(formulario, 3, "Data de nascimento:", new JTextField(28));
        adicionarCampo(formulario, 4, "Telefone:", new JTextField(28));
        adicionarCampo(formulario, 5, "Email:", new JTextField(28));
        adicionarCampo(formulario, 6, "Login:", new JTextField(28));
        JComboBox<Status> status = new JComboBox<>(Status.values());
        status.setSelectedItem(Status.INATIVO);
        adicionarCampo(formulario, 7, "Status:", status);
        adicionarCampo(formulario, 8, "Nova senha:", new JPasswordField(28));
        adicionarCampo(formulario, 9, "Confirmar nova senha:", new JPasswordField(28));

        JButton salvar = new JButton("Salvar alterações");
        salvar.setEnabled(false);
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(event -> dispose());
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        acoes.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));
        acoes.add(salvar);
        acoes.add(cancelar);

        add(formulario, BorderLayout.CENTER);
        add(acoes, BorderLayout.SOUTH);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void adicionarCampo(JPanel formulario, int linha, String texto, JComponent campo) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setLabelFor(campo);
        campo.getAccessibleContext().setAccessibleName(texto);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = linha;
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.anchor = GridBagConstraints.LINE_END;
        formulario.add(rotulo, constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(campo, constraints);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new TelaEdicaoUsuario().setVisible(true));
    }
}
