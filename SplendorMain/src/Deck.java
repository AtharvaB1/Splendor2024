import java.io.*;
import java.util.*;
public class Deck {
    ArrayList<Card> cards;
    boolean isEmpty;
    
    //constructor
    public Deck(int level){
        File card = new File("src\\Cards\\Card"+level+".txt");
        Scanner scan;
        try {
            scan = new Scanner(card);
        } catch (FileNotFoundException e) {
            System.out.println(e+"your file is screwed - Deck");
            e.printStackTrace();
            return;
        }
        scan.nextLine();
        cards = new ArrayList<>();
        while(scan.hasNextLine()){
            System.out.println("card make" + level );
            cards.add(new Card(scan.nextLine(), level));
        }
    }
    
    //returns deck
    public ArrayList<Card> getDeck(){
        return cards;
    }
    
    //returns the top card from the deck
    public Card drawCard(){
        return cards.getFirst(); //remember to print deck with index 0 on top and last index on the table
    }

    //returns the top card from the selected point in the deck
    public Card drawCard(int loc){
        return cards.get(loc);
    }
    
    //retrurns the top card from the deck, and them removes the top card from the deck
    public Card takeCard(){
        Card ret = cards.get(0);
        cards.remove(0);
        return ret; 
    }

    //returns the card from the selected location and removes it
    public Card takeCard(int loc){
        Card ret = cards.get(0);
        cards.remove(0);
        return ret; 
    }

    //returns the card from the selected location and removes it
    public Card takeCard(Card loc){
        cards.remove(loc);
        return loc; 
    }


    //returns if the deck is empty or not
    public boolean deckEmpty(){
        if(cards.isEmpty()){
            return true;
        }
        return false;
    }
    
}//end of class
