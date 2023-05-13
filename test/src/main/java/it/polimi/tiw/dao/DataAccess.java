package it.polimi.tiw.dao;

import java.util.List;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.beans.*;


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
				tmp.setNomeCorsoLaurea(result.getString("nomeCorsoLaurea"));				
				tmp.setMatricola(result.getInt("matricola"));
				utenti.add(tmp);
			}

		}
		return utenti;
	}
	
	
	public List<String> getCorsi() throws SQLException {
		List<String> ret = new ArrayList<>();
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT nomeCorso FROM Corso ORDER BY nomeCorso DESC");) {

			while (result.next()) ret.add(result.getString("nomeCorso"));

		}
		
		return ret;
	}

	public List<Date> findAppelliFromCorso(String corso) throws SQLException {
		List<Date> ret = new ArrayList<>();
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery(
					"SELECT data FROM Corso C, Appello A WHERE A.nomeCorso = C.nomeCorso AND A.nomeCorso = '" + corso
					+ "' ORDER BY data DESC"
				);) {

			while (result.next()) ret.add(result.getDate("data"));

		}
		return ret;
		
	}

	public List<UtenteVoto> findUtentiVoto(String arg1, String arg2) throws SQLException {
		List<UtenteVoto> ret = new ArrayList<>();
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery(
						
						"SELECT * FROM IscrizAppello I, Utente U "
						+ "WHERE data = '" + arg1 + "' AND nomeCorso = '"
						+ arg2 + "' And U.idUtente = I.idUtente"
					
				);) {

			while (result.next()) {
				UtenteVoto tmp = new UtenteVoto();
				tmp.setUtente(new Utente());
				
				tmp.getUtente().setNome(result.getString("nome"));
				tmp.getUtente().setCognome(result.getString("cognome"));
				tmp.getUtente().setEmail(result.getString("email"));
				tmp.getUtente().setPassword(result.getString("password"));
				tmp.getUtente().setTipo(result.getString("flag"));
				tmp.getUtente().setNomeCorsoLaurea(result.getString("nomeCorsoLaurea"));
				tmp.getUtente().setMatricola(result.getInt("matricola"));
				
				tmp.setStato(result.getString("stato"));
				tmp.setVoto(result.getString("esito"));

				ret.add(tmp);
			}

		}
		return ret;
	}
	
}
