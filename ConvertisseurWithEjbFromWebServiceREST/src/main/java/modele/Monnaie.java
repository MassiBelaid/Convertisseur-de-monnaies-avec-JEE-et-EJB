package modele;

import java.io.Serializable;

public class Monnaie implements Serializable {
    private String currency;
    private float rate;
    private String fullName;
    private String countryOfMoney;

    public Monnaie(){};

    public Monnaie(String currency, float rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public float getRate() {
        return rate;
    }

    public void setCurrency(String cirrency) {
        this.currency = cirrency;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountryOfMoney() {
        return countryOfMoney;
    }

    public void setCountryOfMoney(String countryOfMoney) {
        this.countryOfMoney = countryOfMoney;
    }
}
