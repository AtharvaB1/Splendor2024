import java.io.*;
import java.util.*;
public class Deck {
    ArrayList<Card> cards;
    boolean isEmpty;

    public Deck(int level){
        File card = new File("src\\Cards\\Card"+level+".txt");
        Scanner scan;
        try {
            scan = new Scanner(card);
        } catch (FileNotFoundException e) {
            System.out.println(e+"your file is screwed");
            e.printStackTrace();
            return;
        }
        scan.nextLine();
        cards = new ArrayList<>();
        while(scan.hasNextLine()){
            cards.add(new Card(scan.nextLine()));
        }
    }

    public ArrayList<Card> getDeck(){
        return cards;
    }

    public Card peekCard(){
        return cards.get(0); //remember to print deck with index 0 on top and last index on the table
    }

    public Card drawCard(){
        Card ret = cards.get(0);
        cards.remove(0);
        return ret; 
    }

    public boolean deckEmpty(){
        if(cards.isEmpty()){
            return true;
        }
        return false;
    }
}
