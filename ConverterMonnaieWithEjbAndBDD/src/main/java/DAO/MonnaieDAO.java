package DAO;

import DAO.Iterface.IMonnaieDAO;
import Factory.DAOFactory;
import modele.Monnaie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MonnaieDAO implements IMonnaieDAO {

    DAOFactory dao;

    public MonnaieDAO(DAOFactory dao) {
        this.dao = dao;
    }

    @Override
    public void addMonnaie(Monnaie m) {
        String sqlCode = "INSERT INTO monnaies VALUES (?,?,?,?,CURRENT_TIMESTAMP);";
        try {
            PreparedStatement statement = dao.getConnection().prepareStatement(sqlCode);
            statement.setString(1,m.getCurrency());
            statement.setFloat(2,m.getRate());
            statement.setString(3,"m.getFullName()");
            statement.setString(4,"m.getCountryOfMoney()");

            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Monnaie getMoneyWithCode(String code) {
        Monnaie m = null;
        PreparedStatement statement = null;
        try{
            String sqlCode = "SELECT * FROM monnaies WHERE currency = ? AND addedat in (SELECT MAX(addedat) FROM monnaies WHERE currency = ? GROUP BY currency)";

            statement = dao.getConnection().prepareStatement(sqlCode);
            statement.setString(1,code);
            statement.setString(2,code);

            System.out.println(statement);
            ResultSet result = statement.executeQuery();
            result.next();

            if(result != null){
                m = new Monnaie();
                m.setRate(result.getFloat("rate"));
                m.setCurrency(result.getString("currency"));
                System.out.println("    =================   "+m.getCurrency());
            }


        }catch (SQLException e){
            e.printStackTrace();
        }/*finally {
            try{
                statement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }*/

        return m;
    }
}
