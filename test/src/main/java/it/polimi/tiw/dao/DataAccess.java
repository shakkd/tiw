package it.polimi.tiw.dao;

import java.util.List;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.Utente;


public class DataAccess {

	private Connection connection;
	
	public DataAccess() {
		// TODO Auto-generated constructor stub
	}
	
	public DataAccess(Connection con) {
		this.connection = con;
	}

	
	public List<Utente> findAllUtenti() throws SQLException {
		List<Utente> utenti = new ArrayList<Utente>();
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT * FROM Utente");) {

			while (result.next()) {
				Utente tmp = new Utente();
				tmp.setNome(result.getString("nome"));
				tmp.setCognome(result.getString("cognome"));
				tmp.setEmail(result.getString("email"));
				tmp.setPassword(result.getString("password"));
				tmp.setTipo(result.getString("flag"));
				tmp.setMatricola(result.getInt("matricola"));
				utenti.add(tmp);
			}

		}
		return utenti;
	}
	
	
	public List<String> getCorsi() throws SQLException {
		List<String> corsi = new ArrayList<>();
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT * FROM Corso");) {

			while (result.next()) corsi.add(result.getString("nomeCorso"));

		}
		
		return corsi;
	}

	

}
