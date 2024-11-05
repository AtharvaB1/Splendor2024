import java.io.*;
import java.util.*;
public class Deck {
    ArrayList<Card> cards;

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
}
