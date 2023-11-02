import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Blackjack {

    Deck deck = new Deck();
    //dealer
    Card hole;
    ArrayList<Card> dealerHand;
    int dealerTotal;
    int dealerAces;

    //player
    ArrayList<Card> playerHand;
    int playerTotal;
    int playerAces;
    int balance = 1000;

    //window
    int boardWidth = 1280;
    int boardHeight = 720;

    int cardWidth = 165;
    int cardHeight = 231;

    JFrame frame = new JFrame("Blackjack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if(balance > 0){
                try{
                    //draw hole card
                    Image holeCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                    if(!standButton.isEnabled()){
                        holeCardImg = new ImageIcon(getClass().getResource(hole.getImagePath())).getImage();
                    }
                    g.drawImage(holeCardImg, 20, 20, cardWidth, cardHeight, null);

                    //draw dealer's hand
                    for(int i = 0; i < dealerHand.size(); i++){
                        Card card = dealerHand.get(i);
                        Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                        g.drawImage(cardImg, cardWidth + 40 + (cardWidth + 20)*i, 20, cardWidth, cardHeight, null);
                    }

                    //draw player's hand
                    for(int i = 0; i < playerHand.size(); i++){
                        Card card = playerHand.get(i);
                        Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                        g.drawImage(cardImg, 20 + (cardWidth + 20)*i, 390, cardWidth, cardHeight, null);
                    }

                    //if player stands
                    if(!standButton.isEnabled()){
                        dealerTotal = reduceDealerAce();
                        playerTotal = reducePlayerAce();

                        String message = "";
                        if(playerTotal > 21) message = "You Busted!";
                        else if(dealerTotal > 21) {
                            message = "You Win!";
                            balance += 200;
                        }
                        else if(playerTotal == dealerTotal) {
                            message = "It's a Tie!";
                            balance += 100;
                        }
                        else if(playerTotal > dealerTotal) {
                            message = "You Win!";
                            balance += 200;
                        }
                        else message = "You Lose!";

                        //print result
                        g.setFont(new Font("Arial", Font.BOLD, 30));
                        g.setColor(new Color(236,236,224));
                        int x = (gamePanel.getWidth() - g.getFontMetrics().stringWidth(message)) / 2 - 155;
                        int y = 330;
                        g.drawString(message, x, y);

                        newGameButton.setEnabled(true);
                    }

                    //draw balance
                    g.setColor(new Color(236,236,224));
                    g.fillRect(990, 70, 300, 100);
                    String balanceString = "Balance: $" + balance;
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(new Color(59, 50, 74));
                    g.drawString(balanceString, 1000, 130);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                newGameButton.setEnabled(false);
                setBackground(new Color(59, 50, 74));
                String end = "You've lost all your money!";
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.setColor(new Color(236,236,224));
                int x = (gamePanel.getWidth() - g.getFontMetrics().stringWidth(end)) / 2;
                int y = 330;
                g.drawString(end, x, y);
            }
        }
    };

    //create buttons
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton standButton = new JButton("Stand");
    JButton newGameButton = new JButton("New Game");
    
    public Blackjack(){

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(59, 50, 74));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        standButton.setFocusable(false);
        buttonPanel.add(standButton);
        newGameButton.setFocusable(false);
        newGameButton.setEnabled(false);
        buttonPanel.add(newGameButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        //if player clicks hit button
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.getDeck().remove(deck.getDeck().size() - 1);
                playerTotal += card.getNumber();
                playerAces += card.isAce()? 1 : 0;
                playerHand.add(card);
                if(reducePlayerAce() > 21){
                    hitButton.setEnabled(false);
                    standButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });

        //if player clicks stand button
        standButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                standButton.setEnabled(false);

                while(dealerTotal < 17) {
                    Card card = deck.getDeck().remove(deck.getDeck().size() - 1);
                    dealerTotal += card.getNumber();
                    dealerAces += card.isAce()? 1 : 0;
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });

        //if player clicks new game button
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
                newGameButton.setEnabled(false);
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    public void startGame(){

        if(deck.getDeck().size() < 10) deck = new Deck();

        balance -= 100;
        //build deck
        deck.shuffle();

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerTotal = 0;
        dealerAces = 0;

        hole = deck.getDeck().remove(deck.getDeck().size() - 1);
        dealerTotal += hole.getNumber();
        dealerAces += hole.isAce() ? 1 : 0;

        Card card = deck.getDeck().remove(deck.getDeck().size() - 1);
        dealerTotal += card.getNumber();
        dealerAces += card.isAce()? 1 : 0;
        dealerHand.add(card);

        //player
        playerHand = new ArrayList<Card>();
        playerTotal = 0;
        playerAces = 0;

        for(int i = 0; i < 2; i++){
            card = deck.getDeck().remove(deck.getDeck().size() - 1);
            playerTotal += card.getNumber();
            playerAces += card.isAce()? 1 : 0;
            playerHand.add(card);
        }
    }

    //checks if there is an ace in the player's hand and if total is greater than 21, makes ace a 1
    public int reducePlayerAce(){
        while (playerTotal > 21 && playerAces > 0){
            playerAces--;
            playerTotal -= 10;
        }
        return playerTotal;
    }


    //checks if there is an ace in the dealer's hand and if total is greater than 21, makes ace a 1
    public int reduceDealerAce(){
        while (dealerTotal > 21 && dealerAces > 0){
            dealerAces--;
            dealerTotal -= 10;
        }
        return dealerTotal;
    }

}   
