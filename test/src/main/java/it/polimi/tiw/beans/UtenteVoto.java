package it.polimi.tiw.beans;

import java.io.Serializable;

public class UtenteVoto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Utente utente;
	private String voto;
	private String stato;
	
	public UtenteVoto() {}
	
	public UtenteVoto(Utente arg1, String arg2, String arg3) {
		this.utente = arg1;
		this.voto = arg2;
		this.stato = arg3;
	}
	
	
	public void setUtente (Utente arg) {
		this.utente = arg;
	}
	
	public Utente getUtente () {return this.utente;}
	
	public void setVoto (String arg) {
		this.voto = arg;
	}
	
	public String getVoto () {return this.voto;}

	public void setStato (String arg) {
		this.stato = arg;
	}
	
	public String getStato () {return this.stato;}
	
}
