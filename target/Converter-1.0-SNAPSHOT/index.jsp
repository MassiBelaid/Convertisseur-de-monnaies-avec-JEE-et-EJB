<%--
  Created by IntelliJ IDEA.
  User: Massi
  Date: 04/11/2020
  Time: 10:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Convertisseur de monnaie</title>
</head>
<body>

    <jsp:useBean class="converter.ConverterEJBBean" id="onverterEJBEJB"/>
    <%@page import="java.util.*" %>

    <form action="index.jsp">
        <label for="montant">Montant en euro a convertire </label>
        <input type="number" name="montant" id="montant" required>

        <label for="money">Monnaie souhaitée </label>
        <select name="money" id="money">
            <option value="USD">USD</option>
            <option value="JPY">JPY</option>
            <option value="BGN">BGN</option>
            <option value="CZK">CZK</option>
            <option value="DKK">DKK</option>
            <option value="GBP">GBP</option>
            <option value="HUF">HUF</option>
            <option value="PLN">PLN</option>
            <option value="RON">RON</option>
            <option value="SEK">SEK</option>
            <option value="CHF">CHF</option>
            <option value="ISK">ISK</option>
            <option value="NOK">NOK</option>
            <option value="HRK">HRK</option>
            <option value="RUB">RUB</option>
            <option value="TRY">TRY</option>
            <option value="AUD">AUD</option>
            <option value="BRL">BRL</option>
            <option value="CAD">CAD</option>
            <option value="CNY">CNY</option>
            <option value="HKD">HKD</option>
            <option value="IDR">IDR</option>
            <option value="ILS">ILS</option>
            <option value="INR">INR</option>
            <option value="KRW">KRW</option>
            <option value="MXN">MXN</option>
            <option value="MYR">MYR</option>
            <option value="PHP">PHP</option>
            <option value="SGD">SGD</option>
            <option value="THB">THB</option>
            <option value="ZAR">ZAR</option>
        </select>

        <input type="submit" value="Convertir"/>

    </form>

    <% if(request.getParameter("montant") != null){
        double amount = Double.parseDouble(request.getParameter("montant"));
        String money = request.getParameter("money");

        //out.println("<p>"+amount+"   "+money+"</p>");
        double amountResult = onverterEJBEJB.euroToOtherCurrency(amount,money);
        out.println("<p> Resultat :   "+amountResult+"</p>");
    }


    %>

</body>
</html>
