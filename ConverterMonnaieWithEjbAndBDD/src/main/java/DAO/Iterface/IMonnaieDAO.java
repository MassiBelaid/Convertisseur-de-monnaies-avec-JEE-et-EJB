package DAO.Iterface;

import modele.Monnaie;

public interface IMonnaieDAO {

    void addMonnaie(Monnaie m);
    Monnaie getMoneyWithCode(String code);
}
