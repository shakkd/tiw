package it.polimi.tiw.beans;

import java.io.Serializable;

public class Utente implements Serializable {
	
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String tipo;
	private int matricola;

	public Utente() {
	}
	
	public Utente(String nome, String cognome, String email, String password, String tipo, int matricola) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.tipo = tipo;
		this.matricola = matricola;
	}
	
	public void setNome(String arg) {this.nome = arg;}
	
	public String getNome() {return this.nome;}
	
	
	public void setCognome(String arg) {this.cognome = arg;}
	
	public String getCognome() {return this.cognome;}
	
	
	public void setEmail(String arg) {this.email = arg;}
	
	public String getEmail() {return this.email;}
	
	
	public void setPassword(String arg) {this.password = arg;}
	
	public String getPassword() {return this.password;}
	
	
	public void setTipo(String arg) {this.tipo = arg;}
	
	public String getTipo() {return this.tipo;}
	
	
	public void setMatricola(int arg) {this.matricola = arg;}
	
	public int getMatricola() {return this.matricola;}
	

}
