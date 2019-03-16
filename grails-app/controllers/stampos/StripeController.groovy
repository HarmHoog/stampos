package stampos

import com.stripe.Stripe
import com.stripe.model.Charge
import com.stripe.model.Source


class StripeController {

    def pushService

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
        //TODO: Add setting for domain
        sourceParams.put("redirect[return_url]", "http://www.harmhoog.ovh:8080/StamPOS")

        //The source created is a response from Stripe which includes among other things the payment url.
        Source source = Source.create(sourceParams)

        BetaalVerzoek betaalVerzoek = new BetaalVerzoek(sourceID: source.getId(),
                klantID: params.klantID as int, amount: params.amount as int)
        betaalVerzoek.save()
        //Start the payment process for the customer
        redirect(url : source.redirect.URL)
    }

    /**
     * The webhook, the link to this webhook should be set in the stripe dashboard.
     * @return 200 if succesful
     */
    def webHook() {
        if (!request.JSON) {
            render ''
            return
        }

        switch(request.JSON.type) {
            //When the customer has authorized the payment confirm it.
            case "source.chargeable":
                //Stripe secret key
                //TODO: Add settings to configure keys in GUI
                Stripe.apiKey = "STRIPE_SECRET_KEY"

                //Create charge
                Map<String, Object> chargeParams = new HashMap<String, Object>()
                String id = request.JSON.data.object.id
                BetaalVerzoek betaalVerzoek = BetaalVerzoek.findBySourceID(id)
                chargeParams.put("amount", betaalVerzoek.amount)
                chargeParams.put("currency", "eur")
                chargeParams.put("source", id)
                Charge.create(chargeParams)

                //Add payment to user
                verwerkBetaling(betaalVerzoek)
                break
            //Should almost never happen. Should only happen when something goes wrong within Stripe.
            //If the charge fails the customer will receive his/her funds back on their bank account.
            //TODO: Inform the customer about the failed charge since it fails somewhere within 14 days of the transaction..
            case "charge.failed":
                String id = request.JSON.data.object.id
                BetaalVerzoek betaalVerzoek = BetaalVerzoek.findBySourceID(id)
                undoBetaling(betaalVerzoek)
                break
        }

        //Return 200
        render ''
    }

    //Add the amount to the users account
    private def verwerkBetaling(BetaalVerzoek betaalVerzoek) {
        Klant klant = Klant.get(betaalVerzoek.klantID)
        BigDecimal amount = BigDecimal.valueOf(betaalVerzoek.amount).movePointLeft(2)
        Betaling betaling = new Betaling(klant: klant, bedrag : amount)
        betaling.save()
        render(betaling.toString())
        pushService.paymentDone(betaling)
    }

    //If the charge fails, just take back the amount issued to the customer
    private def undoBetaling(BetaalVerzoek betaalVerzoek) {
        if (betaalVerzoek.uitbetaald) {
            betaalVerzoek.amount = -betaalVerzoek.amount
            verwerkBetaling(betaalVerzoek)
        }
    }


}
