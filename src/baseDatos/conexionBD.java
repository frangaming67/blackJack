package baseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conexionBD {

    private static final String URL = "jdbc:sqlite:blackjack.db";

    private static final String TABLA_JUGADOR = """
            CREATE TABLE IF NOT EXISTS jugador (
                idJugador INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE,
                contra TEXT NOT NULL
            )
            """;

    private static final String TABLA_PARTIDA = """
            CREATE TABLE IF NOT EXISTS partida (
                idPartida INTEGER PRIMARY KEY AUTOINCREMENT,
                idJugador INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                FOREIGN KEY (idJugador) REFERENCES jugador(idJugador)
            )
            """;

    private static final String TABLA_RESULTADO = """
            CREATE TABLE IF NOT EXISTS resultado (
                idResultado INTEGER PRIMARY KEY AUTOINCREMENT,
                idPartida INTEGER NOT NULL UNIQUE,
                puntos INTEGER NOT NULL,
                FOREIGN KEY (idPartida) REFERENCES partida(idPartida)
            )
            """;

    // Singleton: la única conexión de toda la app (empieza en null)
    private static Connection conexion;

    // La crea la primera vez y después devuelve siempre la misma. Quien la usa NO debe cerrarla.
    public static Connection obtenerConexion() throws SQLException {
        if (conexion == null) {
            conexion = DriverManager.getConnection(URL);
        }
        return conexion;
    }

    // Cierra la conexión. Se llama al salir de la app.
    public static void cerrarConexion() throws SQLException {
        if (conexion != null) {// hay una conexion abierta?, Si es null, nunca se abrio  o ya se cerro no hay nada que cerrar, así que no hace nada
            conexion.close();// si no es null entra al if  y se cierra la conexion con conexion.close();
            conexion = null;// se deja la variable vacia para que quede claro que la conexion se cerro
        }
    }

    // Se llama una sola vez al iniciar la app (desde main).
    public static void crearTablas() throws SQLException {
        try (Statement stmt = obtenerConexion().createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON"); // SQLite lo trae apagado por defecto
            stmt.executeUpdate(TABLA_JUGADOR);
            stmt.executeUpdate(TABLA_PARTIDA);
            stmt.executeUpdate(TABLA_RESULTADO);
        }
    }
}