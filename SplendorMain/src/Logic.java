import java.io.*;
import java.util.*;

public class Logic {
    private ArrayList<Patron> patrons;
    private ArrayList<Player> players;
    private ArrayList<Deck> decks;
   // @SuppressWarnings("unused")//remove later, suppress currplayer not used (lots of methods not worked on yet)
    private int currPlayer;
    //@SuppressWarnings("unused")//remove later, suppress boolean not used (i havent worked on the method yet)
    private boolean isLastTurn; //add more here
    private boolean isGameOver;
    private int numPlayers;
    private HashMap<String, Integer> tokens;
    
    public Logic(int count){
        tokens = new HashMap<>();
        numPlayers = count;
        
        if(count==4){
            tokens.put("White",7); tokens.put("Blue",7); tokens.put("Green",7); tokens.put("Red",7); tokens.put("Black",7); tokens.put("Wild",5);    
        } else if(count==3){
            tokens.put("White",5); tokens.put("Blue",5); tokens.put("Green",5); tokens.put("Red",5); tokens.put("Black",5); tokens.put("Wild",3);    
        } else{
            tokens.put("White",4); tokens.put("Blue",4); tokens.put("Green",4); tokens.put("Red",4); tokens.put("Black",4); tokens.put("Wild",2);
        }
        players = new ArrayList<>();
        for(int i=0; i<count;i++){
            players.add(new Player());
        }
        decks = new ArrayList<>();
        for(int i=1; i<=3;i++){
            decks.add(new Deck(i));
        }
        patrons = new ArrayList<>();
        Scanner patCreate;
        File pats = new File("src\\Patrons\\Patron.txt");
        try {
            patCreate = new Scanner(pats);
        } catch (FileNotFoundException e) {
            System.out.println(e+"your file is screwed");
            e.printStackTrace();
            return;
        }
        patCreate.nextLine();
        while(patCreate.hasNext()){
            patrons.add(new Patron(patCreate.nextLine()));
        }
    }

    //returns the current player
    public Player getPlayer()
    {
        return players.get(currPlayer);
    }

    //increases curr player, and passes turn to next player, if last turn then it will check if next player is player 1
    public void endTurn()
    {
        //god i want to use mod here so baddddllllyyy but i cannnt it has to start at one
        currPlayer++;
        if(currPlayer == numPlayers)
            currPlayer = 1;

        if(isLastTurn && currPlayer == 1)
            isGameOver = true;
    }

    //called at end of every turn, will check if currPlayer has >=15 VP before passing turn
    public void isLastTurn()
    {
        for(int i = 0; i < numPlayers; i++)
        {
            if(players.get(i).getTotalVP() >= 15);
            {
                isLastTurn = true;
                return;
            }
        }
    }

    //buys a card foir the current player, checks player’s tokens and does nothing if they do not have enough
    public void buyCard(Card thisCard)
    {
        Player thisPlayer = players.get(currPlayer);
        if(thisPlayer.canBuyCard(thisCard))
        {
           thisPlayer.takeCard(thisCard);
           thisPlayer.removeTokens(thisCard.getCost());
        }

    }
    
    //reserved a card for the current player, does nothing if they cannot reserve
    public void reserveCard(Card thisCard)
    {
        Player thisPlayer = players.get(currPlayer);
        if(thisPlayer.canReserve())
        {
            thisPlayer.reserveCard(thisCard);
            if(!thisPlayer.isOverTen())
            {
                Map<String,Integer> temp = new HashMap<String,Integer>();
                temp.put("Wild", 1);
                thisPlayer.addTokens(temp);
            }
            
        }
    }

    
    //will be called at the end of turn, gets ArrayList of player cards token type and checks if it matches with any patron.
    public void getAPatron()
    {
        Player thisPlayer = players.get(currPlayer);
        for(int i = 0; i < patrons.size(); i++)
        {
            if(thisPlayer.canBuyPatreon(patrons.get(i)))
            {
                thisPlayer.takePatron(patrons.get(i));
                patrons.remove(i);
                i--;
            }
        }
    }
    
    //returns a ArrayList of all the player scores, with player one being the first entry, player 2 being the secong, and so on
    public ArrayList<Integer> getScores()
    {
        ArrayList<Integer> retList = new ArrayList<Integer>();
        for(int i = 0; i < players.size(); i++)
        {
            retList.add(players.get(i).getTotalVP());
        }
        return retList;
    }
    
   /* //returns a TREEMAP of the player scores, sience it is a t
    public TreeMap<String, Integer> getSortedScores()
    {
        TreeMap<String, Integer> retList = new TreeMap<String, Integer>();
        for(int i = 0; i < players.size(); i++)
        {
            retList.put("Player" + (i+1), players.get(i).getTotalVP());
        }
        return retList;
    }*/ 

    public void getTokens(HashMap<String,Integer> thisTokens)//atuv wanted this to be a hashset he is a DUMBASS
    {
        Player thisPlayer = getPlayer();
        if(thisPlayer.tokenCount() + thisTokens.size() > 10){return;}

        
        if(tokenCount(thisTokens) == 3)
        {
            thisPlayer.addTokens(thisTokens);
        }
        else if(tokenCount(thisTokens) == 2 )
        {
            Iterator<String> iter = thisTokens.keySet().iterator();
            String gemChecked = iter.next();
            if(tokens.get(gemChecked) >= 4)
                thisPlayer.addTokens(thisTokens);
        }
        else 
        {
            System.out.println("WRONG NUMBER OF TOKENS!!!!!!!!!");
        }
    }

    //gets the total acount of tokens the inputted hashMap has, identical to tokencount in player
    public int tokenCount(HashMap<String,Integer> thisTokens){
        int count = 0;
        Iterator<String> iter = thisTokens.keySet().iterator();
        for(int i = 0; i < thisTokens.size(); i++)
            count += thisTokens.get(iter.next());
        return count;
    }
    public ArrayList<Deck> getDecks(){
        return decks;
    }
    public ArrayList<Patron> getPatrons(){
        return patrons;
    }
    public HashMap <String, Integer> getTokens(){
        return tokens;
    }
}
