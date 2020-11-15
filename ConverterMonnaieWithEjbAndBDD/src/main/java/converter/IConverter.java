package converter;

import modele.Monnaie;

import java.util.List;
import java.util.Map;

public interface IConverter {
    double euroToOtherCurrency(double amount, String currencyCode);
    List<Monnaie> getAvailableCurrencies();
}
