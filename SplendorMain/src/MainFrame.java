import java.awt.*; //add more imports using frame
import javax.swing.*;
@SuppressWarnings("unused")//remove later, suppress warnings of imports not used

public class MainFrame extends JFrame
{
    private int width;
    private int height;
    //made these static because its what i did last time, could make them not static but this will still work so we dont need to
    private static JFrame frame;
    private static JPanel menu;
    private static JPanel game;
    private static JPanel end;

    //main method
    public static void main(String[] args) {
        
        String title = "Splendor";
        System.out.println(title);
        frame = new JFrame(title);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int)screenSize.getWidth() + 16, (int)screenSize.getHeight() + 10);
        frame.setDefaultCloseOperation(3);
        frame.setExtendedState(6);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(menu);

        //Logic logic = new Logic(4); this is STUPID!!!!!!!!!!1!!!!!!!!!!!!!!!!
    }

    //switches from the menuPanel to SplendorPanel, initilizes a new instance of the splendorpanel while still in meupanel, then removes manupanel and sets it to null, starts the game based on int players
    public static void startGame(int players)
    {
        //we might only need logic to have player amounts but this dosent hurt
        game = new Splendorpanel(players);
        frame.remove(menu);
        menu = null;
        frame.add(game);
        frame.revalidate();
    }

     //switches from the SplendorPanel to EbdPanel, initilizes a new instance of the endpanel while still in splendorpanel, then removes splendorpanel and sets it to null, ends the game
    public static void endGame(int players)
    {
        end = new EndPanel();
        frame.remove(game);
        game = null;
        frame.add(end);
        frame.revalidate();
    }

    //switches from the endPanel to menPanel, initilizes a new instance of the menupanel while still in endpanel, then removes endpanel and sets it to null, restarts the game
    public static void restartGame(int players)
    {
        menu = new MenuPanel();
        frame.remove(end);
        end = null;
        frame.add(menu);
        frame.revalidate();
    }

}//end of class
