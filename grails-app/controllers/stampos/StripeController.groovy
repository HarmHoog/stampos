package stampos

import com.stripe.Stripe
import com.stripe.model.Charge
import com.stripe.model.Source


class StripeController {

    def pushService

    //TODO: Save the maps somewhere otherwise late payments might not be processed if the server was down.
    Map<String, Integer> sourcesKlantID = new HashMap<String, Integer>()
    Map<String, Integer> sourcesAmounts = new HashMap<String, Integer>()

    def index() {
    }

    def startPayment() {
        //Stripe public key
        //TODO: Add settings to configure keys in GUI
        Stripe.apiKey = "STRIPE_PUBLIC_KEY"

        Map<String, Object> sourceParams = new HashMap<String, Object>()
        sourceParams.put("type", "sofort")
        sourceParams.put("currency", "eur")
        sourceParams.put("amount", params.amount)
        sourceParams.put("sofort[country]", "NL")
        sourceParams.put("redirect[return_url]", "http://www.harmhoog.ovh:8080/StamPOS")
        Source source = Source.create(sourceParams)
        sourcesKlantID.put(source.getId(), params.klantID as int)
        sourcesAmounts.put(source.getId(), params.amount as int)
        redirect(url : source.redirect.URL)
    }

    //The webhook, the link to this webhook should be set in the stripe dashboard.
    //Also the webhook must use HTTPS
    def webHook() {
        if (request.JSON) {
            //Listen for chargeable sources.
            if ("source.chargeable".equals(request.JSON.type)) {

                //Stripe secret key
                //TODO: Add settings to configure keys in GUI
                Stripe.apiKey = "STRIPE_PRIVATE_KEY"

                //Create charge
                Map<String, Object> chargeParams = new HashMap<String, Object>()
                String id = request.JSON.data.object.id
                chargeParams.put("amount", sourcesAmounts.get(id))
                chargeParams.put("currency", "eur")
                chargeParams.put("source", id)
                Charge.create(chargeParams)

                //Add payment to user
                //TODO: Check whether there exists a real possiblity that charges fail.
                verwerkBetaling(sourcesKlantID.get(id), sourcesAmounts.get(id))
            }
        }

        //Return 202
        render ''
    }

    //Add the amount to the users account
    private def verwerkBetaling(Integer klantID, Integer amountCent) {
        Klant klant = Klant.get(klantID)
        BigDecimal amount = BigDecimal.valueOf(amountCent).movePointLeft(2)
        Betaling betaling = new Betaling(klant: klant, bedrag : amount)
        betaling.save()
        render(betaling.toString())
        pushService.paymentDone(betaling)
    }


}
