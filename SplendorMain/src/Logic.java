import java.io.*;
import java.util.*;

public class Logic {
    private ArrayList<Patron> patrons;
    private ArrayList<Player> players;
    private ArrayList<Deck> decks;
    private HashMap<String, Integer> tokens;
   // @SuppressWarnings("unused")//remove later, suppress currplayer not used (lots of methods not worked on yet)
    private int numPlayers;
    private int currPlayer;
    public ArrayList<Patron> currPatrons;
    //@SuppressWarnings("unused")//remove later, suppress boolean not used (i havent worked on the method yet)
    private boolean isLastTurn; 
    private boolean isGameOver;
    private boolean tokenSelected;
    
    //constructor
    public Logic(int count){
        tokens = new HashMap<>();
        numPlayers = count;
        currPlayer = 0;
        
        if(count==4){
            tokens.put("White",7); tokens.put("Blue",7); tokens.put("Green",7); tokens.put("Red",7); tokens.put("Black",7); tokens.put("Wild",5);    
        } else if(count==3){
            tokens.put("White",5); tokens.put("Blue",5); tokens.put("Green",5); tokens.put("Red",5); tokens.put("Black",5); tokens.put("Wild",3);    
        } else{
            tokens.put("White",4); tokens.put("Blue",4); tokens.put("Green",4); tokens.put("Red",4); tokens.put("Black",4); tokens.put("Wild",2);
        }
        players = new ArrayList<>();
        for(int i=0; i<count;i++){
            players.add(new Player(i+1));
        }
        decks = new ArrayList<>();
        for(int i=1; i<=3;i++){
            decks.add(new Deck(i));
        }
        patrons = new ArrayList<>();
        Scanner patCreate;
        File pats = new File("src\\TextFiles\\Patrons\\Patron.txt");
        try {
            patCreate = new Scanner(pats);
        } catch (FileNotFoundException e) {
            System.out.println(e+"your file is screwed- patron");
            e.printStackTrace();
            return;
        }
        patCreate.nextLine();
        while(patCreate.hasNext()){
            patrons.add(new Patron(patCreate.nextLine()));
        }

        currPatrons = new ArrayList<Patron>();
        for(int i = 0; i < 5; i++){
            currPatrons.add(patrons.get(0));
            patrons.remove(0);
        }
    }

    //returns the current player
    public Player getPlayer(){
        return players.get(currPlayer);
    }

    //returns the entrie players arrayList
    public ArrayList <Player> getAllPlayers(){
        return players;
    }

    // returns isLastTurn
    public boolean isItLastTurn(){
        return isLastTurn;
    }

    //increases curr player, and passes turn to next player, if last turn then it will check if next player is player 1
    public void endTurn(){
        //god i want to use mod here so baddddllllyyy but i cannnt it has to start at one
        currPlayer++;
        if(currPlayer == numPlayers)
            currPlayer = 0;

        if(isLastTurn && currPlayer == 0)
            isGameOver = true;
    }

    //called at end of every turn, will check if currPlayer has >=15 VP before passing turn
    public void isLastTurn(){
        for(int i = 0; i < numPlayers; i++)
        {
            if(players.get(i).getTotalVP() >= 15);
            {
                isLastTurn = true;
                return;
            }
        }
    }

    //buys a card for the current player, checks player’s tokens and does nothing if they do not have enough, checks all the deck if they have the card and removes the card
    public void buyCard(Card thisCard){
        Player thisPlayer = players.get(currPlayer);
        if(thisPlayer.canBuyCard(thisCard))
        {
           thisPlayer.takeCard(thisCard);
           thisPlayer.removeTokens(thisCard.getCost());
        }
        for(int i = 0; i < 3; i++)
        {
            if(getFirstFour(i).contains(thisCard))
                decks.get(i).takeCard(thisCard);
            return;
        }
    }

    //buys a card foir the current player, checks player’s tokens and does nothing if they do not have enough automaticly does the the provided deck and removes the card, more efficant but need to know the location of the card to use
    public void buyCard(Card thisCard, int deckNum){
        Player thisPlayer = players.get(currPlayer);
        if(thisPlayer.canBuyCard(thisCard))
        {
           thisPlayer.takeCard(thisCard);
           thisPlayer.removeTokens(thisCard.getCost());
        }
        decks.get(deckNum).takeCard(thisCard);
    }
    
    //reserved a card for the current player, does nothing if they cannot reserve
    public void reserveCard(Card thisCard){
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

    //will be called at the end of turn, gets ArrayList of player cards token type and checks if it matches with any patron, removes the patron if does.
    public void getAPatron(){
        Player thisPlayer = getPlayer();
        for(int i = 0; i < currPatrons.size(); i++)
        {
            if(thisPlayer.canBuyPatreon(currPatrons.get(i)))
            {
                thisPlayer.takePatron(currPatrons.get(i));
                currPatrons.remove(i);
                i--;
            }
        }
    }
    
    //returns a ArrayList of all the player scores, with player one being the first entry, player 2 being the secong, and so on
    public ArrayList<Integer> getScores(){
        ArrayList<Integer> retList = new ArrayList<Integer>();
        for(int i = 0; i < players.size(); i++)
        {
            retList.add(players.get(i).getTotalVP());
        }
        return retList;
    }
    
    //returns a TREEMAP of the player scores, IBELIIIIIVEVVVVVVVVVVEEEEEEEE
    public TreeSet<Player> getSortedPlayers(){
        TreeSet<Player> retList = new TreeSet<Player>();
        for(int i = 0; i < players.size(); i++)
        {
            retList.add(players.get(i));
        }
        return retList;
    }
    
    //returns if the current player can take the selected amount of tokens
    public boolean canGetTokens(HashMap<String,Integer> thisTokens){
        Player thisPlayer = getPlayer();
        if(thisPlayer.tokenCount() + thisTokens.size() > 10){
            return false;
        }

        if(tokenCount(thisTokens) == 3){
            return true;
        }
        else if(tokenCount(thisTokens) == 2){
            Iterator<String> iter = thisTokens.keySet().iterator();
            String gemChecked = iter.next();
            if(tokens.get(gemChecked) >= 4)
                return true;
        }
        else {
            System.out.println("WRONG NUMBER OF TOKENS!!!!!!!!!");
            return false;
        }
        return false;
    }
    
    //takes outs the inputted amount of tokens from the tokens hashmap, if it is possible
    public boolean getTokens(HashMap<String,Integer> thisTokens){
        Player thisPlayer = getPlayer();
        if(thisPlayer.tokenCount() + thisTokens.size() > 10){
            return false;
        }

        if(tokenCount(thisTokens) == 3)
        {
            thisPlayer.addTokens(thisTokens);
            return true;
        }
        else if(tokenCount(thisTokens) == 2 )
        {
            Iterator<String> iter = thisTokens.keySet().iterator();
            String gemChecked = iter.next();
            if(tokens.get(gemChecked) >= 4)
                thisPlayer.addTokens(thisTokens);
                return true;
        }
        else 
        {
            System.out.println("WRONG NUMBER OF TOKENS!!!!!!!!!");
        }
        return false;
    }


    //gets the total acount of tokens the inputted hashMap has, identical to tokencount in player
    public int tokenCount(HashMap<String,Integer> thisTokens){
        int count = 0;
        Iterator<String> iter = thisTokens.keySet().iterator();
        for(int i = 0; i < thisTokens.size(); i++)
            count += thisTokens.get(iter.next());
        return count;
    }
    
    //returns the 3 main decks for the game
    public ArrayList<Deck> getDecks(){
        return decks;
    }
    
    //returns all patreons 
    public ArrayList<Patron> getPatrons(){
        return patrons;
    }

    //returns the all tokens that are not hekld by players
    public HashMap <String, Integer> getTokens(){
        return tokens;
    }

    //returns the current patreons in play
    public ArrayList<Patron> getCurrPatrons(){
        return patrons;
    }

    //gets the first four cards of a selected deck
    public ArrayList<Card> getFirstFour(int deckType)
    {
        ArrayList<Card> retList = new ArrayList<Card>();
        for(int i = 0; i < 4; i++)
        {  
            retList.add(decks.get(deckType).drawCard(i));
        }
        return retList;
    }
    
}//end of class
