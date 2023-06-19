package it.polimi.tiw.dao;

import java.util.List;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
	
	
	public List<String> getCorsi(String flag, String idUtente) throws SQLException {
		List<String> ret = new ArrayList<>();
		
		String add = null;
		String tab = null;
		switch(flag) {
			case "D":
				tab = "Corso C";
				add = " INNER JOIN Utente U ON C.idUtente = U.idUtente WHERE U.idUtente = " + idUtente;
				break;
			case "S":
				tab = "IscrizAppello";
				add = " WHERE idUtente = " + idUtente;
				break;
		}
		String query = String.format("SELECT DISTINCT nomeCorso FROM %s%s ORDER BY nomeCorso DESC", tab, add);
		
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery(query);) {

			while (result.next()) ret.add(result.getString("nomeCorso"));

		}
		
		return ret;
	}

	public List<Date> findAppelliFromCorso(String corso, String flag, String id) throws SQLException {
		List<Date> ret = new ArrayList<>();
		
		String add1 = "";
		String add2 = "";
		
		if (flag.equals("S")) {
			add1 = ", Utente U";
			add2 = " AND U.idUtente = I.idUtente and U.idUtente =" + id;
		}
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery(
					String.format(
							"SELECT DISTINCT data FROM Corso C, IscrizAppello I%s WHERE I.nomeCorso = C.nomeCorso AND I.nomeCorso = '%s'"
							+ "%s ORDER BY data DESC"
					, add1, corso, add2)
				);) {

			while (result.next()) ret.add(result.getDate("data"));

		}
		
		return ret;
		
	}

	public List<UtenteVoto> findUtentiVoto(String arg1, String arg2, String flag1, String flag2) throws SQLException {
		List<UtenteVoto> ret = new ArrayList<>();
		
		String query = "SELECT * FROM IscrizAppello I, Utente U  "
				+ "WHERE data = '" + arg1 + "' AND nomeCorso = '" 
				+ arg2 + "' And U.idUtente = I.idUtente";
	
		String conc1 = "";
		String conc2 = "";
		
		if (flag1 != null) switch (flag1) {
			case "Matricola":
				conc1 = " ORDER BY matricola";
				break;
			case "Cognome":
				conc1 = " ORDER BY cognome";
				break;
			case "Nome":
				conc1 = " ORDER BY nome";
				break;
			case "Email":
				conc1 = " ORDER BY email";
				break;
			case "Corso di laurea":
				conc1 = " ORDER BY nomeCorsoLaurea";
				break;
			case "Voto":
				conc1 = " ORDER BY esito";
				break;
			case "Stato di valutazione":
				conc1 = " ORDER BY stato";
				break;
		}
		
		if (flag1 != null) switch (flag2) {
			case "asc":
				conc2 = " ASC";
				break;
			case "desc":
				conc2 = " DESC";
				break;
		}
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery(query.concat(conc1).concat(conc2));) {

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
				
				tmp.setVoto(result.getString("esito"));
				tmp.setStato(result.getString("stato"));

				ret.add(tmp);
			}

		}
		return ret;
	}
	
	public List<String> findVoti() throws SQLException {
		List<String> ret = new ArrayList<>();
		
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT nomeVoto from Voto"); ){

			while (result.next()) ret.add(result.getString("nomeVoto"));
			
		}
		
		return ret;
		
	}
	
	public int getIdUtente(String email) throws SQLException {
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT idUtente FROM Utente WHERE email = '" + email + "'"); ){

			result.next();
			return result.getInt("idUtente");
		}
	}
	
	public int getIdByMatricola(String matr) throws SQLException {
		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT idUtente FROM Utente WHERE matricola = " + matr ); ){

			result.next();
			return result.getInt("idUtente");
		}
	}
	
	public void updateUtenteVoto(UtenteVoto utenteVoto) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement("UPDATE IscrizAppello SET  esito = '"
				+ utenteVoto.getVoto() + "', stato = '" + utenteVoto.getStato()
				+ "' WHERE idUtente = (SELECT idUtente FROM Utente WHERE matricola = " + utenteVoto.getUtente().getMatricola() + ")"
				); ){	
			
			stmnt.executeUpdate();
			
			return;
		}
	}
	
	
	public void pubblicaEsiti(String data, String corso) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement(
				"UPDATE IscrizAppello SET stato = 'Pubblicato' WHERE data = '" + data
				+ "' AND nomeCorso = '" + corso + "' AND stato = 'Inserito'"
				); ){	
			
			stmnt.executeUpdate();
			
			return;
		}
		
	}
	
	public void verbalizzaEsiti(String data, String corso) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement(
				
				"INSERT INTO Verbale (dataVerb, ora, nomeCorso, data) VALUES (curdate(), curtime() , '"
				+ corso + "', '" + data + "')"
							
			); ){	
		
			stmnt.executeUpdate();
			
		}
		
		try (PreparedStatement stmnt = connection.prepareStatement(
				
				"UPDATE IscrizAppello SET stato = 'Verbalizzato', idVerbale = "
				+ "(SELECT idVerbale FROM Verbale WHERE data = '" + data + "' AND nomeCorso = '" + corso + "') "
				+ "WHERE data = '" + data + "' AND nomeCorso = '" + corso
				+ "' AND (stato = 'Pubblicato' OR stato = 'Rifiutato')"
							
			); ){	
		
			stmnt.executeUpdate();
		
		}
		
	}
	
	
	public UtenteVoto findEsito(String data, String corso, String id) throws SQLException {
		
		UtenteVoto ret = new UtenteVoto();

		try (Statement stmnt = connection.createStatement();
				ResultSet result = stmnt.executeQuery("SELECT * FROM IscrizAppello I"
						+ " INNER JOIN Utente U on I.idUtente = U.idUtente WHERE data = '"
						+ data + "' AND nomeCorso = '" + corso + "' AND I.idUtente = " + id); ){

			result.next();
			
			ret.setUtente(new Utente());
			
			ret.getUtente().setNome(result.getString("nome"));
			ret.getUtente().setCognome(result.getString("cognome"));
			ret.getUtente().setEmail(result.getString("email"));
			ret.getUtente().setPassword(result.getString("password"));
			ret.getUtente().setTipo(result.getString("flag"));
			ret.getUtente().setNomeCorsoLaurea(result.getString("nomeCorsoLaurea"));
			ret.getUtente().setMatricola(result.getInt("matricola"));
			
			ret.setVoto(result.getString("esito"));
			ret.setStato(result.getString("stato"));

		}
		
		return ret;
		
	}
	
	public void rifiutaVoto(String data, String corso, String idUtente) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement(
				"UPDATE IscrizAppello SET stato = 'Rifiutato' WHERE data = '" + data
				+ "' AND nomeCorso = '" + corso + "' AND idUtente = " + idUtente
				); ){	
			
			stmnt.executeUpdate();
			
			return;
		}
		
	}
	
	
	public void updateEsitoById(String data, String corso, String idUtente, String esito) throws SQLException {
		
		try (PreparedStatement stmnt = connection.prepareStatement(
				"UPDATE IscrizAppello SET stato = 'Inserito', esito = " + esito + " WHERE data = '"
				+ data + "' AND nomeCorso = '" + corso + "' AND idUtente = " + idUtente
				); ){	
			
			stmnt.executeUpdate();
			
			return;
		}
		
	}
	
	
}
