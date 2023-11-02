

public class Card {
    
    private String number;
    private String suit;

    public Card(String number, String suit) {
        this.number = number;
        this.suit = suit;
    }

    public int getNumber() {
        if("AJQK".contains(this.number)){
            if(this.number.equals("A")) return 11;
            return 10;
        }
        return Integer.parseInt(this.number);
    }

    public boolean isAce(){
        return this.number.equals("A");
    }

    public String getImagePath(){
        return "./cards/" + toString() + ".png";
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSuit() {
        return this.suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String toString() {
        return this.number + "-" + this.suit;
    }
}
