/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author henrymf
 */
//User class
public class Usuario implements Autenticavel {

    private String name;
    private String cpf;
    private String gender;
    private String birthDay;
    private String phoneNumber;
    private String email;
    private String login;
    private String password;
    private Status status = Status.INATIVO;
    private Perfil perfilAtivo;
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

    @Override
    public boolean autenticar(String login, String password) {
        return this.login != null && this.login.equals(login)
                && this.password != null && this.password.equals(password);
    }
}
