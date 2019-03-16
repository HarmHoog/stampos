package stampos

class BetaalVerzoek {

    String sourceID
    Date date = new Date()
    int klantID
    //Amount in cent
    int amount
    //Used to make sure that a source chargeable request can be issued only once
    //Also to check whether failed payments should result in a negative payment
    //for the user.
    boolean uitbetaald = false


    static constraints = {
        //Payments lower than 1 euro are too expensive and not accepted by Stripe.
        amount(min: 100)
    }

    @Override
    String toString() {
        return "Betaalverzoek aangemaakt op: " + date.toString() + " voor klant met id: " + klantID + " voor " + (amount / 100.0) + " euro"
    }
}
