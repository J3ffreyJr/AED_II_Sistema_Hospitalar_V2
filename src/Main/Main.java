package Main;

import Funcionamento.Menu;
import Base_d_Dados.DB;

public class Main{
    public static void main(String[] args) {
        DB db = new DB();

        db.criarBaseDeDados();
        /*
        Menu menu = new Menu();
        menu.iniciar();*/
    }
}
