import java.util.*;
public class Player
{
    private int victoryPoints;
    private ArrayList<Card> reservedCards;
    private ArrayList<Card> heldCards;
    private ArrayList<Patron> heldPatrons;
    private Map<String, Integer> gems;
   
    //constructor
    public Player(){
        victoryPoints = 0;
        reservedCards = new ArrayList<Card>();
        heldCards = new ArrayList<Card>();
        heldPatrons = new ArrayList<Patron>();
        gems = new HashMap<String, Integer>();
    }


    //returns the hashmap of the gems the player has
    public Map<String, Integer> getTotalGems(){
        return gems;
    }
   
    //gets the total acount of gems the player has
    public int getGemSize(){
        int ret = 0;
        Iterator<String> iter = gems.keySet().iterator();
        for(int i = 0; i < gems.size(); i++)
            ret += gems.get(iter.next());
        return ret;
    }


    //gets the total acount of gems the inputed list has
    public int getGemSize(Map<String, Integer> addGems){
        int ret = 0;
        Iterator<String> iter = addGems.keySet().iterator();
        while(iter.hasNext())
            ret += addGems.get(iter.next());
        return ret;
    }


    //returns an int amount of gems that the player has for that type
    public int getGemType(String gem){
        return gems.get(gem);
    }


    // adds the gems in the arrayList to the player,
    // returns true if it is legal to add, does nothing and returns false if it is not legal
    public boolean addGems(Map<String, Integer> addGems){
        if(getGemSize() + getGemSize(addGems) > 10)
            return false;


        Iterator iter = addGems.keySet().iterator();
        while(iter.hasNext())
        {
            String gemType = (String) iter.next();
            if(gems.containsKey(gemType))
                gems.replace(gemType, gems.get(gemType)+addGems.get(gemType));
            else
                gems.put(gemType, 1);  
        }  
        return true;  
    }


    //removes the gems in the arrayList from the player
    // returns true if it is legal to add, does nothing and returns false if it is not legal
    public boolean removeGems(Map<String, Integer> removeGems){
        Iterator iter = removeGems.keySet().iterator();
        while(iter.hasNext())
        {
            String gemType = (String) iter.next();
            if(gems.get(gemType)-removeGems.get(gemType) < 0)
                return false;
        }


        iter = removeGems.keySet().iterator();
        while(iter.hasNext())
        {
            String gemType = (String) iter.next();
            if(gems.containsKey(gemType))
                gems.replace(gemType, gems.get(gemType)-removeGems.get(gemType));
        }
        return true;
    }


    //returns a hashMap of all the gems Types and the amount of discpount the player ha for them (can easily remove the hashmap if needed)
    public Map<String, Integer> getTotalDiscount(){
        Map retMap = new HashMap<String, Integer>();
        for(int i = 0; i < heldCards.size(); i++)
        {
            if(retMap.containsKey(heldCards.get(i).getGem()))
                retMap.replace(heldCards.get(i).getGem(), retMap.get(heldCards.get(i).getGem())+1);
            else
                retMap.put(heldCards.get(i).getGem(), 1);
        }
        return retMap;
    }


    //returns the amount of discount the player has for the specified material(ruby, sapphire, etc)
    public int getDiscountType(String thisGem){
        if(getTotalDiscount().containsKey(thisGem))
            return getTotalDiscount().get(thisGem);
        else
            return 0;
    }


    //returns if the player has enough discounts to buy a patreon card
    //returns true if they can, and false if they cant, will not effect any values
    public boolean canBuyPatreon(Patreon thisPatreon)
    {
        Map<String, Integer> comp = getTotalDiscount();
        Map<String, Integer> reqDiscount = thisPatreon.getPatCost();
        Iterator<String> iter = reqDiscount.keySet().iterator();
        while(iter.hasNext())
        {
            String discount = iter.next();
            if(comp.get(discount) < reqDiscount.get(discount))
                return false;
        }
        return true;
    }


    //returns if the gem lists length is over 10 or not
    public boolean isOverTen()
    {
        if(getGemSize() > 10)
        {
            return true;
        }
        return false;
    }


    //returns the player score
    public int getTotalVP(){
        return victoryPoints;
    }


    //returns a list of all held player cards
    public ArrayList<Card> getTotalCards(){
        return heldCards;
    }


    //returns a list of all reserved player cards
    public ArrayList<Card> getTotalReservedCards(){
        return reservedCards;
    }


    //returns a list of all held patreon cards
    public ArrayList<Patron> getTotalPatrons(){
        return heldPatrons;
    }


    //takes the selected card and adds it into the cards ArrayList
    public void takeCard(Card taken){
        heldCards.add(taken);
    }


    //takes the selected patron and add it to the patrons ArrayList
    public void takePatron(Patron taken){
        heldPatrons.add(taken);
    }


    //checks if the player has less than 3 reserved cards.
    public boolean canReserve(){
        if(heldCards.size() < 3)
            return true;
        return false;
    }


    //takes the selected card and adds it to the reservedCards arraylist, also adds a wild gem to the gems arrayList (if one is available)
    //returns true if the player actually can reserve a card, does nothing and returns false if not (it will still return true if it can reserve a card but cant add a wild token)
    public boolean reserveCard(Card reserve){
        if(canReserve())
        {
            reservedCards.add(reserve);
            Map<String, Integer> wild = new HashMap<String,Integer>();
            wild.put("Wild", 1);
            addGems(wild);
            return true;
        }
        return false;  
    }


    //removes the gems that are unaccounted by discounts and adds the card into the cards ArrayList
    //(returns true if the player has enough tokens and discounts, returns false and does not do anything if player does not have enough)
    public boolean buyCard(Card card){
        Map<String,Integer> cardCost = card.cardCost();
        if(removeGems(cardCost))
        {
            heldCards.add(card);
            return true;
        }
        return false;
    }


    //runs after every turn, checks if player has cards required to achieve Patron, automatically gives player the patron and doesn’t require their input.
    //(true if player has enough discounts, returns false and does not do anything if player does not have engough discounts)
    //only covers a sigle patreon, so we are gonna have to call this as many times as there are patreons on the board
    public boolean buyPatron(Patron patron){
        if(canBuyPatreon(patron))
        {
            heldPatrons.add(patron);
            return true;
        }
        return false;
    }


}//end of class

