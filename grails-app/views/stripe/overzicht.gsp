<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="beheer"/>
    <title>Stripe Instellingen</title>
    <style>
    #stripeSettings {
        display:inline-block;
    }

    #stripeSettings input{
        float:right;
        margin-bottom: 3pt;
    }

    h2{
        clear:both;
    }

    #stripeSettings label{
        float:left;
        clear:both;
        line-height: 16px;
    }
    </style>
</head>
<body>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <h2>Instructies</h2>
    1. Maak een account op stripe.com aan en activeer de betaalmethode Sofort. <br/>
    2. Zorg ervoor dat er een SSL certificaat geactiveerd is voor de host. <br/>
    3. Kopieer de Stripe API keys van het Stripe dashboard naar de instellingen hieronder en vul de rest van de instellingen in. <br/>
    4. Zet de webhook in het Stripe dashboard naar de link hieronder. <br/>
    <br/>
    Zet je webhook in de Stripe dashboard naar: <br/>
    ${createLink(controller: 'stripe', action: 'webHook', absolute: 'true')} <br/>
    Zorg ervoor dat de link met https begint en dat er een SSL certificaat geactiveerd is. <br/>
    <br/>
    <div id="stripeSettings">
        <g:form action="save">
            <h2>Stripe Instellingen</h2>
            <label>Enable Stripe: </label>
            <g:checkBox name="isStripeEnabled" checked="${oldIsStripeEnabled}"/><br/>
            <label>Stripe publishable key: </label>
            <g:textField name="publishKey" value="${oldPublishKey}"/><br/>
            <label>Stripe secret key: </label>
            <g:textField name="secretKey" value="${oldSecretKey}"/><br/>
            <label>Return url na betaling: </label>
            <g:textField name="returnURL" value="${oldReturnURL}"/><br/><br/>
            <g:actionSubmit value="Save" style="clear:both;"/>
        </g:form>
    </div>
</body>
</html>