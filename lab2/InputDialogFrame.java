package lab2;

//This program shows a series of input dialog boxes.
//The next dialog is launched on the closing of the current dialog. 
//It provides examples of how to create dialog boxes
//with a text field, combo box and list box.

//Imports are listed in full to show what's being used
//could just import javax.swing.* and java.awt.* etc..
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.Icon;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

public class InputDialogFrame extends JFrame{
  
  private JTextArea tracker;
  
  //Using a standard Java icon
  private Icon optionIcon = UIManager.getIcon("FileView.computerIcon");
  
  //Application start point   
  public static void main(String[] args) {
   
   //Use the event dispatch thread for Swing components
   EventQueue.invokeLater(new Runnable()
   {
       public void run()
       {
           //create GUI frame
           new InputDialogFrame().setVisible(true);          
       }
   });
            
  }
  
  public InputDialogFrame()
  {
      //make sure the program exits when the frame closes
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Dialog Box Example");
      setSize(500,300);
      
      //This will center the JFrame in the middle of the screen
      setLocationRelativeTo(null);
      
      
      //Using JTextArea to show clicks and responses
      tracker = new JTextArea("Click tracker:");
      add(tracker);
      setVisible(true);
      
      //Options for the combo box dialog
      String[] choices = {"Monday", "Tuesday"
              ,"Wednesday", "Thursday", "Friday"};
      
      //Options for the list dialog
      //There are more than 20 entries to make the showInputDialog method
      //choose to use a list box
      String[] jumboChoices = {"Abe", "Billy", "Colin", "Dexter"
              , "Edward", "Fred", "Gus", "Harry", "Ira", "Jeff"
              , "Kirk", "Larry", "Monty", "Nigel", "Orville", "Paul"
              , "Quint", "Richard", "Steve", "Tony", "Umberto", "Vinnie"
              , "Wade", "Xavier", "Yogi", "Zigmund"};
      
      //Input dialog with a text field
      String input =  JOptionPane.showInputDialog(this 
              ,"Enter in some text:");
      
      TrackResponse(input);
      
      //Input dialog with default text in the text field
      String defaultText =  JOptionPane.showInputDialog(this 
              ,"Enter in some text:","some text..");
      
      TrackResponse(defaultText);
      
      //Input dialog with a textfield, a message type and title
      String warningText =  JOptionPane.showInputDialog(this 
              ,"Erm, enter in a warning:" ,"Warning Message"
              ,JOptionPane.WARNING_MESSAGE);
      
      TrackResponse(warningText);
      
      //If an icon is used then it overrides the icon from the
      //message type. Likewise if a null is entered for the selection values
      //the dialog box will use a text field
      String entered = (String)JOptionPane.showInputDialog(this
              , "Enter a Day of the week:"
              , "Text Field Dialog", JOptionPane.QUESTION_MESSAGE
              , optionIcon, null, null);
      
      TrackResponse(entered);
      
      //If the String Array has more than 20 entries a JList is used
      //as the method the user gets to select a value
      String boyNames = (String)JOptionPane.showInputDialog(this, "Pick a Name:"
              , "ComboBox Dialog", JOptionPane.QUESTION_MESSAGE
              , optionIcon, jumboChoices, jumboChoices[0]);
      
      TrackResponse(boyNames);
      
      //Input dialog with a combo box created by
      //using a String array for the object message. Note how
      //a null icon for the icon results in the QUESTION_MESSAGE
      //message type being used.
      String picked = (String)JOptionPane.showInputDialog(this, "Pick a Day:"
              , "ComboBox Dialog", JOptionPane.QUESTION_MESSAGE
              , null, choices, choices[0]);
      
      TrackResponse(picked);      
  }
  
  //Append the picked choice to the tracker JTextArea
  public void TrackResponse(String response)
  {
      //showInputDialog method returns null if the dialog is exited
      //without an option being chosen
      if (response == null)
      {
          tracker.append("\nYou closed the dialog without any input..");
      }
      else
      {
          tracker.append("\nYou picked " + response + "..");
      }
  }
}
