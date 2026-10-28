package baseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.jugador;
import util.hashContra;

// DAO (Data Access Object): clase que se encarga de hablar con la base de datos
// para todo lo relacionado a la tabla "jugador". Los controladores la usan
// para no tener SQL mezclado con la lógica de las ventanas.
public class jugadorDAO {

    // ¿Ya hay un jugador con ese nombre?
    // Devuelve true si existe, false si no existe (o si hubo un error).
    public boolean existeNombre(String nombre) {
        // El ? es un hueco que se rellena después con el nombre (evita inyección SQL)
        String sql = "SELECT nombre FROM jugador WHERE nombre = ?";

        // La conexión es el singleton compartido: se pide pero NO se cierra acá
        try {
            Connection conn = conexionBD.obtenerConexion();
            // try-with-resources: la consulta (ps) se cierra sola al terminar
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // Rellena el primer ? (posición 1) con el nombre recibido
            ps.setString(1, nombre);

                // executeQuery se usa para SELECT y devuelve las filas encontradas
                try (ResultSet rs = ps.executeQuery()) {
                    // rs.next() es true si hay al menos una fila, o sea, si el nombre existe
                    return rs.next();
                }
            }

        } catch (SQLException error) {
            // Si falla la base de datos, se informa por consola
            System.out.println("Error al consultar: " + error.getMessage());
        }
        return false;
    }

    // Crea el jugador. Devuelve el jugador creado, o null si falló
    public jugador registrar(String nombre, String contra) {
        // Dos ?: el primero es el nombre y el segundo la contraseña
        String sql = "INSERT INTO jugador (nombre, contra) Values (?, ?)";

        // RETURN_GENERATED_KEYS le pide a la BD que devuelva el id autogenerado
        try {
            Connection conn = conexionBD.obtenerConexion(); // singleton: no se cierra
            try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                // Rellena los ? en orden: 1 = nombre, 2 = contra
                ps.setString(1, nombre);
                ps.setString(2, hashContra.hashear(contra));

                // executeUpdate se usa para INSERT, UPDATE y DELETE (no devuelve filas)
                ps.executeUpdate();

                // Recupera el id que la BD le asignó al jugador recién creado
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        // Arma el objeto jugador con ese id y el nombre
                        return new jugador(claves.getInt(1), nombre);
                    }
                }
            }

        } catch (SQLException error) {
            // Cae acá si el nombre ya existía (UNIQUE) o si falla la BD
            System.out.println("No se pudo registrar: " + error.getMessage());
        }
        // null le avisa al controlador que no se pudo crear
        return null;
    }

    // Busca un jugador con ese nombre y contraseña.
    // Devuelve el jugador si los datos son correctos, o null si no.
    public jugador autenticar(String nombre, String contra) {
        // Los nombres de columna son los de la tabla: idJugador, nombre, contra
        String sql = "SELECT idJugador, nombre FROM jugador WHERE nombre = ? AND contra = ?";
        try {
            Connection conn = conexionBD.obtenerConexion(); // singleton: no se cierra
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                // 1 = nombre, 2 = contra (mismo orden que los ? del SQL)
                ps.setString(1, nombre);
                ps.setString(2, hashContra.hashear(contra));

                try (ResultSet rs = ps.executeQuery()) {
                    // Si hay fila, los datos son correctos: se arma el jugador leyendo las columnas
                    if (rs.next()) {
                        return new jugador(rs.getInt("idJugador"), rs.getString("nombre"));
                    }
                }
            }
        } catch (SQLException error) {
            System.out.println("Error al autenticar: " + error.getMessage());
        }
        // null = usuario o contraseña incorrectos (o error de BD)
        return null;
    }
    // Cambia la contraseña de un jugador. Devuelve true si se modificó
    public boolean modificarContra(int idJugador, String nuevaContra) {
        String sql = "UPDATE jugador SET contra = ? WHERE idJugador = ?";
        try {
            Connection conn = conexionBD.obtenerConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, hashContra.hashear(nuevaContra));   // se guarda hasheada
                ps.setInt(2, idJugador);
                int filas = ps.executeUpdate();   // cuántas filas cambió
                return filas == 1;
            }
        } catch (SQLException error) {
            System.out.println("No se pudo modificar la contraseña: " + error.getMessage());
            return false;
        }
    }
    // Cambia el nombre de un jugador. Devuelve true si se modificó
    // (false si falló, por ejemplo si el nombre ya lo usa otro jugador)
    public boolean modificarNombre(int idJugador, String nuevoNombre) {
        String sql = "UPDATE jugador SET nombre = ? WHERE idJugador = ?";
        try {
            Connection conn = conexionBD.obtenerConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nuevoNombre);
                ps.setInt(2, idJugador);
                int filas = ps.executeUpdate();   // cuántas filas cambió
                return filas == 1;
            }
        } catch (SQLException error) {
            System.out.println("No se pudo modificar el nombre: " + error.getMessage());
            return false;
        }
    }
    // Borra un jugador. Antes hay que borrar sus partidas (partidaDAO.eliminarPartidasDeJugador)
    public boolean eliminarJugador(int idJugador) {
        String sql = "DELETE FROM jugador WHERE idJugador = ?";
        try {
            Connection conn = conexionBD.obtenerConexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idJugador);
                int filas = ps.executeUpdate();
                return filas == 1; // devuelve 1 si encontro y borro al gudaro si es 0 	No había ningún jugador con ese id: no borró nada 
            }
        } catch (SQLException error) {
            System.out.println("No se pudo eliminar el jugador: " + error.getMessage());
            return false;
        }
    }
}
