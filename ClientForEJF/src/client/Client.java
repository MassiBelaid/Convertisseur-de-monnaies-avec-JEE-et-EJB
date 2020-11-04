package client;

import java.util.Scanner;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import converter.IConverter;

public class Client {

	public static void main(String[] args) {
		IConverter converter = null;
		
		try {
			converter = (IConverter) InitialContext.doLookup("ejb:/Converter-1.0-SNAPSHOT/ConverterEJBEJB!converter.IConverter");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrer le code de la monnaie pour la quelle convertir");
		String monnaiCode = sc.nextLine();
		
		System.out.println("Entrez en euro un montant Valide : ");
		int montant = sc.nextInt();
		
		

        System.out.println(converter.euroToOtherCurrency(montant,monnaiCode));
	}

}
