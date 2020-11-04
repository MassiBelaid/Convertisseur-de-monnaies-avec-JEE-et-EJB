package converter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@Remote
@Stateless(name = "ConverterEJBEJB")
public class ConverterEJBBean implements IConverter{
    public ConverterEJBBean() {
    }

    @Override
    public double euroToOtherCurrency(double amount, String currencyCode) {
        double taux = this.getMoneyValueWithCode(currencyCode);

        return amount*taux;
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


}
