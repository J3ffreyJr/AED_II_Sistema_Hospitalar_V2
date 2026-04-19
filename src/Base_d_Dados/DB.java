package Base_d_Dados;

import java.sql.*;


public class DB {
    private final String url, user, password, name;

    public DB() {
        this.url = "jdbc:mysql://localhost:3306/";
        this.user = "root";
        this.password = "GRo10"; // colocar palavra passe do teu mysql
        this.name = "`" + "Sistema Hospitalar" + "`";
    }

    public void criarBaseDeDados() {
        try (Connection conex = DriverManager.getConnection(url, user, password);
             Statement statt = conex.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS%s".formatted(name);
            statt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }

    // esse metodo vai criar as tabelas Na nossa base de dados do mysql se não existirem claro
    public void criarTabelas() {
        // TODO: continuar a criação de tabelas na base de dados

        String sqlTbl1 = "CREATE TABLE IF NOT EXISTS Paciente(paciente_id,...)";

        try (Connection connex = DriverManager.getConnection(url, user, password);
             Statement stt = connex.createStatement()) {
            connex.setCatalog(name);
            //stt.executeUpdate("USE%s".formatted(name));


        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }
}
