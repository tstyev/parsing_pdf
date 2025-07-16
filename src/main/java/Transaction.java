public class Transaction {
    private String creationDate;
    private String npqwReference;
    private String businessUnit;
    private double amount;
    private String currency;
    private String cardType;
    private String status;

    public Transaction(String creationDate, String npqwReference, String businessUnit, double amount, String currency, String cardType, String status) {
        this.creationDate = creationDate;
        this.npqwReference = npqwReference;
        this.businessUnit = businessUnit;
        this.amount = amount;
        this.currency = currency;
        this.cardType = cardType;
        this.status = status;
    }

    public String getCreationDate() { return creationDate; }
    public String getNpqwReference() { return npqwReference; }
    public String getBusinessUnit() { return businessUnit; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getCardType() { return cardType; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Transaction{" +
                "creationDate='" + creationDate + '\'' +
                ", npqwReference='" + npqwReference + '\'' +
                ", businessUnit='" + businessUnit + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", cardType='" + cardType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
