package baseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import modelo.filaRanking;

// DAO para guardar las partidas (cuando el jugador se retira) y sus resultados
public class partidaDAO {

    // Guarda una partida y sus puntos. Devuelve true si salió bien, false si falló
    public boolean guardar(int idJugador, int puntos) {
        String sqlPartida = "INSERT INTO partida (idJugador, fecha) VALUES (?, ?)";     
        String sqlResultado = "INSERT INTO resultado (idPartida, puntos) VALUES (?, ?)";  

        try {
            Connection conn = conexionBD.obtenerConexion();   // singleton: no se cierra

            // ---- 1. Insertar en partida y recuperar el id que se generó ----
            int idPartida;
            try (PreparedStatement ps = conn.prepareStatement(sqlPartida, PreparedStatement.RETURN_GENERATED_KEYS)) {
                
                ps.setInt(1, idJugador);
                ps.setString(2, LocalDate.now().toString());
                
                ps.executeUpdate();

                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        idPartida = claves.getInt(1);
                    } else {
                        return false;   // no se generó el id: algo salió mal
                    }
                }
            }

            // ---- 2. Insertar en resultado usando ese id ----
            try (PreparedStatement ps = conn.prepareStatement(sqlResultado)) {
                
                ps.setInt(1, idPartida);
                ps.setInt(2, puntos);
             
                ps.executeUpdate();
            }

            return true;   // llegaron los dos INSERT sin errores

        } catch (SQLException error) {
            System.out.println("No se pudo guardar la partida: " + error.getMessage());
            return false;
        }
    }
    // Devuelve los 10 jugadores con más partidas ganadas, de mayor a menor
    public ArrayList<filaRanking> obtenerRanking() {
        ArrayList<filaRanking> ranking = new ArrayList<filaRanking>();

        String sql = """
                SELECT j.nombre, SUM(r.puntos) AS ganadas
                FROM jugador j
                JOIN partida p   ON p.idJugador = j.idJugador
                JOIN resultado r ON r.idPartida = p.idPartida
                GROUP BY j.idJugador
                ORDER BY ganadas DESC
                LIMIT 10
                """;

        try {
            Connection conn = conexionBD.obtenerConexion();   // singleton: no se cierra
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                // Cada fila del resultado se convierte en un filaRanking
                while (rs.next()) {
                    ranking.add(new filaRanking(rs.getString("nombre"), rs.getInt("ganadas")));
                }
            }
        } catch (SQLException error) {
            System.out.println("No se pudo obtener el ranking: " + error.getMessage());
        }
        return ranking;   // si hubo error, vuelve vacía
    }
    // Borra todas las partidas de un jugador, y sus resultados.
    // Se usa antes de eliminar al jugador
    public boolean eliminarPartidasDeJugador(int idJugador) {
        // Primero los resultados (apuntan a partida), después las partidas
        String sqlResultados = "DELETE FROM resultado WHERE idPartida IN (SELECT idPartida FROM partida WHERE idJugador = ?)";
        String sqlPartidas = "DELETE FROM partida WHERE idJugador = ?";
        try {
            Connection conn = conexionBD.obtenerConexion();

            try (PreparedStatement ps = conn.prepareStatement(sqlResultados)) {
                ps.setInt(1, idJugador);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlPartidas)) {
                ps.setInt(1, idJugador);
                ps.executeUpdate();
            }
            return true;

        } catch (SQLException error) {
            System.out.println("No se pudo eliminar las partidas: " + error.getMessage());
            return false;
        }
    }
}