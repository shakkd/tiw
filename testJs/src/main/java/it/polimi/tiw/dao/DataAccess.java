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
				add = " INNER JOIN Utente U ON C.idUtente = U.idUtente WHERE U.idUtente = ?";
				break;
			case "S":
				tab = "IscrizAppello";
				add = " WHERE idUtente = ?";
				break;
		}
		String query = String.format("SELECT DISTINCT nomeCorso FROM %s%s ORDER BY nomeCorso DESC", tab, add);
		
		
		try (PreparedStatement stmnt = connection.prepareStatement(query)) {
			
			stmnt.setString(1, idUtente);
			ResultSet result = stmnt.executeQuery();

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
			add2 = " AND U.idUtente = I.idUtente and U.idUtente = ?";
		}
		
		String query = String.format(
				"SELECT DISTINCT data FROM Corso C, IscrizAppello I%s WHERE I.nomeCorso = C.nomeCorso AND I.nomeCorso = ?%s ORDER BY data DESC"
		, add1, add2);
		
		try (PreparedStatement stmnt = connection.prepareStatement(query)) {
			
			if(!add2.equals("")) stmnt.setString(2, id);
			stmnt.setString(1, corso);
			
			ResultSet result = stmnt.executeQuery();

			while (result.next()) ret.add(result.getDate("data"));

		}
		return ret;
		
	}

	public List<UtenteVoto> findUtentiVoto(String arg1, String arg2, String flag1, String flag2) throws SQLException {
		List<UtenteVoto> ret = new ArrayList<>();
		
		String query = "SELECT * FROM IscrizAppello I, Utente U  "
				+ "WHERE data = ? AND nomeCorso = ? AND U.idUtente = I.idUtente";
	
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
			default:
				conc1 = "";
				
		}
		
		if (flag1 != null) switch (flag2) {
			case "asc":
				conc2 = " ASC";
				break;
			case "desc":
				conc2 = " DESC";
				break;
		}
		
		try ( PreparedStatement stmnt = connection.prepareStatement(query.concat(conc1).concat(conc2)) ) {
			
			stmnt.setString(1, arg1);
			stmnt.setString(2, arg2);

			
			ResultSet result = stmnt.executeQuery();

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
		try (PreparedStatement stmnt = connection.prepareStatement("SELECT idUtente FROM Utente WHERE email = ?")) {
			
			
			stmnt.setString(1, email);
			ResultSet result = stmnt.executeQuery();

			result.next();
			return result.getInt("idUtente");
		}
	}
	
	public void updateUtenteVoto(UtenteVoto utenteVoto) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement("UPDATE IscrizAppello SET  esito = ?, stato = ?"
				+ "WHERE idUtente = (SELECT idUtente FROM Utente WHERE matricola = ?)" ) ){	
			
			stmnt.setString(1, utenteVoto.getVoto());
			stmnt.setString(2, utenteVoto.getStato());
			stmnt.setInt(3, utenteVoto.getUtente().getMatricola());
			
			
			stmnt.executeUpdate();
			
			return;
		}
	}
	
	
	public void pubblicaEsiti(String data, String corso) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement(
					"UPDATE IscrizAppello SET stato = 'Pubblicato' WHERE data = ? AND nomeCorso = ? AND stato = 'Inserito'"
				) ){	
			
			stmnt.setString(1, data);
			stmnt.setString(2, corso);
			
			stmnt.executeUpdate();
			
			return;
		}
		
	}
	
	public void verbalizzaEsiti(String data, String corso) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement(
				
					"INSERT INTO Verbale (dataVerb, ora, nomeCorso, data) VALUES (curdate(), curtime() , ?, ?)"
								
				); ){	
			
			stmnt.setString(1, data);
			stmnt.setString(2, corso);
			
			stmnt.executeUpdate();
			
		}
		
		try (PreparedStatement stmnt = connection.prepareStatement(
				
				"UPDATE IscrizAppello SET stato = 'Verbalizzato', idVerbale = "
				+ "(SELECT idVerbale FROM Verbale WHERE data = ? AND nomeCorso = ?) "
				+ "WHERE data = ? AND nomeCorso = ? AND (stato = 'Pubblicato' OR stato = 'Rifiutato')"
							
			); ){	
		
			stmnt.setString(1, data);
			stmnt.setString(2, corso);
			stmnt.setString(3, data);
			stmnt.setString(4, corso);
			
			stmnt.executeUpdate();
		
		}
		
		
	}
	
	
	public UtenteVoto findEsito(String data, String corso, String id) throws SQLException {
		
		UtenteVoto ret = new UtenteVoto();

		try (PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM IscrizAppello I"
				+ " INNER JOIN Utente U on I.idUtente = U.idUtente WHERE data = ? AND nomeCorso = ? AND I.idUtente = ?");
				 ){

			stmnt.setString(1, data);
			stmnt.setString(2, corso);
			stmnt.setString(3, id);
			ResultSet result = stmnt.executeQuery();
			
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
				"UPDATE IscrizAppello SET stato = 'Rifiutato' WHERE data = ? AND nomeCorso = ? AND idUtente = ?" ) ){	
			
			stmnt.setString(1, data);
			stmnt.setString(2, corso);
			stmnt.setString(3, idUtente);
			stmnt.executeUpdate();
			
			return;
		}
		
	}

	
	public int getIdByMatricola(String matr) throws SQLException {
		try (PreparedStatement stmnt = connection.prepareStatement("SELECT idUtente FROM Utente WHERE matricola = ?") ){

			stmnt.setString(1, matr);
			
			ResultSet result = stmnt.executeQuery();
			
			result.next();
			return result.getInt("idUtente");
		}
	}
	
	
	public void updateEsitoById(String data, String corso, String idUtente, String esito) throws SQLException {
		
		try (PreparedStatement stmnt = connection.prepareStatement(
				"UPDATE IscrizAppello SET stato = 'Inserito', esito = ? WHERE data = ? AND nomeCorso = ? AND idUtente = ?" ) ){	
			
			
			stmnt.setString(1, esito);
			stmnt.setString(2, data);
			stmnt.setString(3, corso);
			stmnt.setString(4, idUtente);
			stmnt.executeUpdate();
			
			return;
		}
		
	}
	
	
}
