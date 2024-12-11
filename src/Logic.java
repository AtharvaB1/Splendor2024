import java.io.*;
import java.util.*;

public class Logic {
    private ArrayList<Patron> patrons;
    private ArrayList<Player> players;
    private ArrayList<Deck> decks;
    private HashMap<String, Integer> tokens;
    private int numPlayers;
    private int currPlayer;
    private boolean isLastTurn; 
    private boolean isGameOver;
    private Card[][] matrix;
    
    //constructor
    public Logic(int count){
        tokens = new HashMap<>();
        numPlayers = count;
        currPlayer = 0;
        isLastTurn = false;
        isGameOver = false;
        
        if(count==4){
            tokens.put("White",7); tokens.put("Blue",7); tokens.put("Green",7); tokens.put("Red",7); tokens.put("Black",7); tokens.put("Wild",5);    
        } else if(count==3){
            tokens.put("White",5); tokens.put("Blue",5); tokens.put("Green",5); tokens.put("Red",5); tokens.put("Black",5); tokens.put("Wild",5);    
        } else{
            tokens.put("White",4); tokens.put("Blue",4); tokens.put("Green",4); tokens.put("Red",4); tokens.put("Black",4); tokens.put("Wild",5);
        }
        players = new ArrayList<>();
        for(int i=0; i<count;i++){
            players.add(new Player(i+1));
        }
        decks = new ArrayList<>();
        for(int i=1; i<=3;i++){
            decks.add(new Deck(i));
        }
        matrix = getAllFirstFour();
        patrons = new ArrayList<>();
        InputStream pats = getClass().getResourceAsStream("/TextFiles/Patrons/Patron.txt");
        Scanner patCreate;
        try {
            patCreate = new Scanner(pats);
        } catch (Error e) {
            System.out.println(e+"your file is screwed- patron");
            e.printStackTrace();
            return;
        }
        patCreate.nextLine();
        while(patCreate.hasNext()){
            patrons.add(new Patron(patCreate.nextLine()));
        }
        Collections.shuffle(patrons);
    }

    //returns the current player
    public Player getPlayer(){
        return players.get(currPlayer);
    }

    public void switchPlayer(){
        if(currPlayer < numPlayers){
            currPlayer++;
        } else{
            currPlayer = 1;
        }
    }

    //returns the entrie players arrayList
    public ArrayList <Player> getAllPlayers(){
        return players;
    }

    // returns isLastTurn
    public boolean isItLastTurn(){
        return isLastTurn;
    }

     // returns isgameover
    public boolean isGameOver(){
        return isGameOver;
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

    //returns the current matrix of cards that will be used
    public Card[][] getMatrix(){
        return matrix;
    }

    public boolean showEndButton(){ //indicates if the screen with the end button must be displayed
        for(int i = 0; i < numPlayers; i++)
        {
            if(players.get(i).getTotalVP() >= 15)
            {
                isLastTurn = true;
                break;
            }
        }
        return isLastTurn;
    }

    //increases curr player, and passes turn to next player, if last turn then it will check if next player is player 1
    public void endTurn(){
        getAPatron();
        //I LOOOOOOOOOOVVVVVVVVVEEEEEEEEEEE MODDDDDDDDDDDDDD YYYYYYEEEEAAAAAAHHHHHHH USA USA USA USA USA USA USA USA USA
        currPlayer = (currPlayer + 1) % numPlayers;

        if(isLastTurn && currPlayer == 0)
            isGameOver = true;

        for(int i = 0; i < numPlayers; i++)
        {
            if(players.get(i).getTotalVP() >= 15)
            {
                System.out.println(players.get(i).getTotalVP());
                isLastTurn = true;
                return;
            }
        }
    }

    //buys a card for the current player, checks player’s tokens and does nothing if they do not have enough, checks all the deck if they have the card and removes the card
    public void buyCard(Card thisCard){
        if(thisCard == null){
            System.out.println("held card is null");
            return;
        }

        Player thisPlayer = players.get(currPlayer);
        if(thisPlayer.canBuyCard(thisCard))
        {
           removeCard(thisCard);
           thisPlayer.takeCard(thisCard);
           thisPlayer.removeTokens(thisCard.getCost());
           
        }
     
    }
    
    public void patronCheck(){
        Player p = players.get(currPlayer);
        getAPatron();
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
                if(tokens.get("Wild") >= 1){
                    thisPlayer.addTokens(temp);
                    removeTokens(temp);
                }
            }
        }
    }

    //will be called at the end of turn, gets ArrayList of player cards token type and checks if it matches with any patron, removes the patron if does.
    public void getAPatron(){
        Player thisPlayer = getPlayer();
        for(int i = 0; i < numPlayers + 1; i++)
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
         Iterator<String> tokIter = tokens.keySet().iterator();
        if(thisPlayer.tokenCount() + thisTokens.size() > 10){
            return false;
        }
        if(tokenCount(thisTokens) == 3 && thisTokens.size() == 3){
            return true;
        }
        else if(tokenCount(thisTokens) == 2){
            Iterator<String> iter = thisTokens.keySet().iterator();
            String gemChecked = iter.next();
            if(tokens.get(gemChecked) >= 4 && thisTokens.size() == 1)
                return true;
            else{
                int count = 0;
                while(tokIter.hasNext())
                {
                    if(tokens.get(tokIter.next()) >= 1)
                        count++;
                }
                if(count >2)
                    return false;
                else
                    return true;
            }
        }
        else {
            return false;
        }
    }

    //add tokens fron the array list to the total amount of tokens
    public void addTokens(Map<String, Integer> thisTokens){
        Iterator<String> iter = thisTokens.keySet().iterator();
        while(iter.hasNext()){
            String gemType = (String) iter.next();
            tokens.replace(gemType, tokens.get(gemType)+thisTokens.get(gemType));
        }
    }
    
    //removes the tokens in the arrayList from the total amount of tokens
    public void removeTokens(Map<String, Integer> removeTokens){
        Iterator<String> iter = removeTokens.keySet().iterator();
        while(iter.hasNext()){
            String gemType = (String) iter.next();
            tokens.replace(gemType, tokens.get(gemType)-removeTokens.get(gemType));
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

    //finds, removes, and replaces the selected card in the matrix
    public void removeCard(Card removed){
        for(int i = 0; i < 3; i++)
        {
            for(int o = 0; o < 4; o++)
            {
                if(matrix[i][o].equals(removed))
                {
                    decks.get(i).takeCard(removed);
                    if(decks.get(i).drawCard(3) != null)
                        matrix[i][o] = decks.get(i).drawCard(3);
                    else
                        matrix[i][o] = null;
                    return;
                }
            }
            if(removed == decks.get(i).drawCard(4))
            {
                decks.get(i).takeCard(removed);
                return;
            }
        }

        for(int i = 0; i < getPlayer().getTotalReservedCards().size(); i++)
        {
            if(removed.equals(getPlayer().getTotalReservedCards().get(i)))
            {
                getPlayer().getTotalReservedCards().remove(i);
                return;
            }
        }
    }

    //returns the entire matrix of cards in a 2d array, only used once at the begening
    public Card[][] getAllFirstFour(){
        Card[][] retList = new Card[3][4];
        for(int i = 0; i<3; i++){ 
            for(int j = 0; j<4; j++){
                retList[i][j] = getDecks().get(i).drawCard(j);
            }
        }
        return retList;
    }

    //returns the diffrence between 2 sets of tokens (can easly rework this to the addTokens method, that that is only what it is used for for now)
    public Map<String, Integer> diffrenceOfTokens(Map<String, Integer> map1, Map<String, Integer> map2){

        Map<String, Integer> difference = new HashMap<String, Integer>();
        Iterator<String> iter = map1.keySet().iterator();
        while(iter.hasNext()){
            String value = iter.next();
            if(map1.get(value) - map2.get(value) < 0){
                difference.put(value, 0);
            }
            else{
                difference.put(value, map1.get(value) - map2.get(value));
            }
        }
        return difference;
        //return addsWilds(difference, getPlayer().getTokens().get("Wilds"));
    }

    public void fixCountingError(){
        Iterator<String> iter = getPlayer().getTokens().keySet().iterator();
        while(iter.hasNext()){
            String value = iter.next();
            if(numPlayers == 4)
            {
                if(tokens.get(value) > numPlayers+3){
                    tokens.replace("Wild", tokens.get("Wild") + (tokens.get(value) - (numPlayers+3)));
                    tokens.replace(value, numPlayers+3);
                }
            }
            else{
                if(tokens.get(value) > numPlayers+2){
                    tokens.replace("Wild", tokens.get("Wild") + (tokens.get(value) - (numPlayers+2)));
                    tokens.replace(value, numPlayers+2); 
                }
            }
           // if(numPlayers == 4){
            //    while(getPlayer().getTokens().get(value) + tokens.get(value) > (numPlayers+3)){
           //         getPlayer().getTokens().replace(value, getPlayer().getTokens().get(value) - 1);
            //    }
            //}
            //else{
            //    while(getPlayer().getTokens().get(value) + tokens.get(value) > (numPlayers+2)){
            //        getPlayer().getTokens().replace(value, getPlayer().getTokens().get(value) - 1);
            //    }
        }
    }
    
}//end of class
