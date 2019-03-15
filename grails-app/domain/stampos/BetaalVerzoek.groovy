package stampos

class BetaalVerzoek {

    String sourceID
    Date date = new Date()
    int klantID
    //Amount in cent
    int amount


    static constraints = {
        //Payments lower than 1 euro are too expensive and not accepted by Stripe.
        amount(min: 100)
    }

    @Override
    String toString() {
        return "Betaalverzoek aangemaakt op: " + date.toString() + " voor klant met id: " + klantID + " voor " + (amount / 100.0) + " euro"
    }
}
