package converter;

import modele.Monnaie;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@Remote
@Stateful(name = "ConverterEJBEJB")
public class ConverterEJBBean implements IConverter{

    public ConverterEJBBean() {
    }


    @Override
    public double euroToOtherCurrency(double amount, String currencyCode) {
        double rate = 0;
        try {
            URL url = new URL("http://currencies.apps.grandtrunk.net/getlatest/EUR/"+currencyCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null){
                rate = Double.parseDouble(inputLine);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return amount*rate;
    }

    @Override
    public List<Monnaie> getAvailableCurrencies() {
        List<Monnaie> lisMonnaies = new ArrayList<Monnaie>();

        try {
            URL url = new URL("http://currencies.apps.grandtrunk.net/currencies");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null){
                Monnaie monnaie = new Monnaie();
                monnaie.setCurrency(inputLine);
                lisMonnaies.add(monnaie);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lisMonnaies;
    }

}
