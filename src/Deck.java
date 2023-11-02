import java.util.ArrayList;
import java.util.Random;

public class Deck {
    
    private ArrayList<Card> deck;
    private String[] numbers = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private String[] suits = {"C", "D", "H", "S"};

    public Deck() {
        deck = new ArrayList<Card>();
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < suits.length; j++) {
                deck.add(new Card(numbers[i], suits[j]));
            }
        }
    }

    public void shuffle(){
        Random rand = new Random();
        for (int i = 0; i < deck.size(); i++) {
            int index = rand.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(index));
            deck.set(index, temp);
        }
    }

    public ArrayList<Card> getDeck() {
        return this.deck;
    }
}
