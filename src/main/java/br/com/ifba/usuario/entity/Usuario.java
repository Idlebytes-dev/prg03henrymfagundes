/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author henrymf
 */
//User class
@Entity
@Table(name = "usuarios")
public class Usuario implements Autenticavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String name;
    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;
    @Column(name = "genero", length = 30)
    private String gender;
    @Column(name = "data_nascimento", length = 10)
    private String birthDay;
    @Column(name = "telefone", length = 20)
    private String phoneNumber;
    @Column(name = "email", nullable = false, length = 160)
    private String email;
    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;
    @Column(name = "senha", nullable = false, length = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private Status status = Status.INATIVO;

    // O mapeamento dos relacionamentos com Perfil fica para uma próxima etapa.
    @Transient
    private Perfil perfilAtivo;
    @Transient
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String name, String cpf, String gender, String birthDay,
                   String phoneNumber, String email, String login, String password) {
        this.name = name;
        this.cpf = cpf;
        this.gender = gender;
        this.birthDay = birthDay;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Perfil getPerfilAtivo() {
        return perfilAtivo;
    }

    public void setPerfilAtivo(Perfil perfilAtivo) {
        this.perfilAtivo = perfilAtivo;
    }

    public List<Perfil> getPerfis() {
        return perfis;
    }

    public void adicionarPerfil(Perfil perfil) {
        if (perfil != null) {
            this.perfis.add(perfil);
        }
    }

    // O login identifica o usuário porque é único no cadastro do sistema.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Usuario outro)) {
            return false;
        }
        return login != null && login.equals(outro.login);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }

    @Override
    public boolean autenticar(String login, String password) {
        return this.login != null && this.login.equals(login)
                && this.password != null && this.password.equals(password);
    }
}
