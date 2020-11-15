package converter;

import Factory.DAOFactory;
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
        float rate = 0;
        /*try {
            URL url = new URL("http://currencies.apps.grandtrunk.net/getlatest/EUR/"+currencyCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null){
                rate = Float.parseFloat(inputLine);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Monnaie m = new Monnaie();
        m.setCurrency(currencyCode);
        m.setRate(rate);

        DAOFactory.getInstance().getMonnaiDao().addMonnaie(m);*/

        Monnaie m = DAOFactory.getInstance().getMonnaiDao().getMoneyWithCode(currencyCode);

        return amount*m.getRate();
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

        /*for(Monnaie m : lisMonnaies){
            URL url = null;
            try {
                url = new URL("http://currencies.apps.grandtrunk.net/getlatest/EUR/"+m.getCurrency());

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null){
                    m.setRate(Float.parseFloat(inputLine));
                    DAOFactory.getInstance().getMonnaiDao().addMonnaie(m);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }*/
        return lisMonnaies;
    }

}
