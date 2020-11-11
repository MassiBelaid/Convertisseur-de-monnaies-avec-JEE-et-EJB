package converter;

import modele.Monnaie;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@Remote
@Stateful(name = "ConverterEJBEJB")
public class ConverterEJBBean implements IConverter{
    private Map<Monnaie, Double> mapMonnaies = new HashMap<Monnaie, Double>();

    public ConverterEJBBean() {
    }

    @Override
    public double euroToOtherCurrency(double amount, String currencyCode) {
        double taux = this.getMoneyValueWithCode(currencyCode);
        return amount*taux;
    }

    @Override
    public List<Monnaie> getAvailableCurrencies() {
        List<Monnaie> listCurrencies = new ArrayList<Monnaie>();
        Element element = this.getElement();

        for(Element ele : element.getChildren()) {
            listCurrencies.add(new Monnaie(ele.getAttributeValue("currency"),
                    Float.parseFloat(ele.getAttributeValue("rate"))));
            }
        return listCurrencies;
        }



    @Override
    public Map<Monnaie, Double> euroToOtherCurrencies(double amount) {


        Map<String, Monnaie> mapFectch = this.setInfosForMonneis();
        Map<Monnaie, Double> mapReturn = new HashMap<Monnaie, Double>();


        for(Monnaie mon : mapFectch.values()){
            mapReturn.put(mon, amount*mon.getRate());
        }

        return mapReturn;
    }


    private double getMoneyValueWithCode(String currencyCode){
        Element element = this.getElement();

        for(Element ele : element.getChildren()) {
            if(ele.getAttributeValue("currency").equals(currencyCode)){
                return Double.parseDouble(ele.getAttributeValue("rate"));
            }
        }

        return 0;
    }

    private Element getElement(){
        SAXBuilder sxb = new SAXBuilder();
        URL url = null;
        try {
            url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connexion = null;
        try {
            connexion = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Document document = null;
        try {
            document = sxb.build(connexion.getInputStream());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element racine = document.getRootElement();
        Namespace nameSpace = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002-08-01/eurofxref");

        Element element = racine.getChild("Cube", nameSpace);
        return element.getChild("Cube", nameSpace);
    }

    private Map<String,Monnaie> setInfosForMonneis(){
        SAXBuilder sxb = new SAXBuilder();
        URL url = null;
        try {
            url = new URL("https://www.currency-iso.org/dam/downloads/lists/list_one.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connexion = null;
        try {
            connexion = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Document document = null;
        try {
            document = sxb.build(connexion.getInputStream());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element racine = document.getRootElement();
        Element CcyTbl = racine.getChild("CcyTbl");

        Map<String, Monnaie> listMonneiRetour = new HashMap<String, Monnaie>();

        List<Monnaie> listMonnaies = this.getAvailableCurrencies();


        for(Monnaie mon : listMonnaies){
            listMonneiRetour.put(mon.getCurrency(),mon);
        }

        for(Element element : CcyTbl.getChildren()){
            try {
                String cle = element.getChild("Ccy").getText();
                if(listMonneiRetour.containsKey(cle)){
                    Monnaie monnaie = listMonneiRetour.get(cle);
                    monnaie.setCountryOfMoney(element.getChild("CtryNm").getText());
                    monnaie.setFullName(element.getChild("CcyNm").getText());
                    this.mapMonnaies.put(monnaie, 0.0);
                }
                //System.out.println(element.getChildren().get(2).getText());
            }catch (NullPointerException e){
                System.out.println("Problem with "+element);
            }
        }

        return listMonneiRetour;
    }


}
