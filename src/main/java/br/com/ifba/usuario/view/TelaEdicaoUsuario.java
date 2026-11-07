package br.com.ifba.usuario.view;

import br.com.ifba.ApexOptima;
import br.com.ifba.infrastructure.view.TarefaDaInterface;
import br.com.ifba.usuario.dao.UsuarioDao;
import br.com.ifba.usuario.dao.UsuarioIDao;
import br.com.ifba.usuario.entity.Status;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.validar.ValidadorUsuario;
import java.awt.BorderLayout;
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
import javax.swing.JOptionPane;
import java.util.Objects;

/** Edita o usuário selecionado e delega a persistência ao contrato DAO. */
public class TelaEdicaoUsuario extends JFrame {
    private final UsuarioIDao usuarioDao;
    private final Usuario usuario;
    private final JTextField nome = new JTextField(28);
    private final JTextField cpf = new JTextField(28);
    private final JComboBox<String> genero = new JComboBox<>(new String[]{
        "Selecione", "Feminino", "Masculino", "Outro", "Prefiro nao informar"});
    private final JTextField nascimento = new JTextField(28);
    private final JTextField telefone = new JTextField(28);
    private final JTextField email = new JTextField(28);
    private final JTextField login = new JTextField(28);
    private final JComboBox<Status> status = new JComboBox<>(Status.values());
    private final JPasswordField senha = new JPasswordField(28);
    private final JPasswordField confirmacao = new JPasswordField(28);
    private final JButton salvar = new JButton("Salvar alterações");

    public TelaEdicaoUsuario() {
        this(new UsuarioDao(), null);
    }

    public TelaEdicaoUsuario(UsuarioIDao usuarioDao, Usuario usuario) {
        super("ApexOptima - Editar usuário");
        this.usuarioDao = Objects.requireNonNull(usuarioDao);
        this.usuario = usuario;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        adicionarCampo(formulario, 0, "Nome completo:", nome);
        adicionarCampo(formulario, 1, "CPF:", cpf);
        adicionarCampo(formulario, 2, "Gênero:", genero);
        adicionarCampo(formulario, 3, "Data de nascimento:", nascimento);
        adicionarCampo(formulario, 4, "Telefone:", telefone);
        adicionarCampo(formulario, 5, "Email:", email);
        adicionarCampo(formulario, 6, "Login:", login);
        status.setSelectedItem(Status.INATIVO);
        adicionarCampo(formulario, 7, "Status:", status);
        adicionarCampo(formulario, 8, "Nova senha (opcional):", senha);
        adicionarCampo(formulario, 9, "Confirmar nova senha:", confirmacao);

        salvar.setEnabled(usuario != null);
        salvar.addActionListener(event -> salvarAlteracoes());
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
        if (usuario != null) preencher();
    }

    private void preencher() {
        nome.setText(usuario.getName());
        cpf.setText(usuario.getCpf());
        if (usuario.getGender() != null) {
            var modelo = (javax.swing.DefaultComboBoxModel<String>) genero.getModel();
            if (modelo.getIndexOf(usuario.getGender()) < 0) modelo.addElement(usuario.getGender());
            genero.setSelectedItem(usuario.getGender());
        }
        nascimento.setText(usuario.getBirthDay());
        telefone.setText(usuario.getPhoneNumber());
        email.setText(usuario.getEmail());
        login.setText(usuario.getLogin());
        status.setSelectedItem(usuario.getStatus());
    }

    private void salvarAlteracoes() {
        if (usuario == null) return;
        String novaSenha = new String(senha.getPassword());
        String repetirSenha = new String(confirmacao.getPassword());
        if (!ValidadorUsuario.camposPreenchidos(nome.getText(), cpf.getText(), nascimento.getText(),
                telefone.getText(), email.getText(), login.getText()) || genero.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios.");
            return;
        }
        if (!ValidadorUsuario.cpfValido(cpf.getText())
                || ValidadorUsuario.contemPalavraProibida(login.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Confira o CPF e o login informados.");
            return;
        }
        if (!ValidadorUsuario.senhasConferem(novaSenha, repetirSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.");
            return;
        }
        usuario.setName(nome.getText().trim());
        usuario.setCpf(cpf.getText().trim());
        usuario.setGender((String) genero.getSelectedItem());
        usuario.setBirthDay(nascimento.getText().trim());
        usuario.setPhoneNumber(telefone.getText().trim());
        usuario.setEmail(email.getText().trim());
        usuario.setLogin(login.getText().trim());
        usuario.setStatus((Status) status.getSelectedItem());
        if (!novaSenha.isEmpty()) usuario.setPassword(novaSenha);
        TarefaDaInterface.executar(this, salvar, () -> usuarioDao.update(usuario), salvo -> {
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso.");
            dispose();
        });
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
        ApexOptima.iniciar(args, TelaListagemUsuario::new);
    }
}
