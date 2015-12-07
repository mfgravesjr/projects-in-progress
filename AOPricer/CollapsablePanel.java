import org.jdesktop.swingx.JXCollapsiblePane;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Graphics;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JFrame;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;

public class CollapsiblePanel extends JPanel implements ItemListener
{
   ItemTitleBar itemTitleBar;
   GridBagLayout layout = new GridBagLayout();
   GridBagConstraints con = new GridBagConstraints();
   JXCollapsiblePane collapsingContainer = new JXCollapsiblePane();
   CollapsiblePanel collapsiblePanel = this;
   
   public CollapsiblePanel(String title, boolean expanded)
   {
      setLayout(layout);
      itemTitleBar = new ItemTitleBar(title,expanded);
      con.weightx = 1;
      con.weighty = 1;
      con.anchor = con.NORTHWEST;
      con.fill = con.BOTH;
      con.gridx = 0;
      con.gridy = 0;
      super.add(itemTitleBar,con);
      con.gridy = 1;
      super.add(collapsingContainer,con);
      collapsingContainer.setLayout(new FlowLayout());
      collapsingContainer.setBorder(new LineBorder(Color.BLACK,1));
      collapsingContainer.setCollapsed(!expanded);
      itemTitleBar.addItemListener(this);
   }
   
   public void itemStateChanged(ItemEvent e)
   {
      collapsingContainer.setCollapsed(!itemTitleBar.isSelected());
   }
   
   public Component add(Component comp)
   {
      collapsingContainer.add(comp);
      return comp;
   }
   
   class ItemTitleBar extends JToggleButton
   {
      JLabel titleLbl = new JLabel();
      
      public ItemTitleBar(String title, boolean selected)
      {
         super("",selected);
         titleLbl.setText(title);
         add(titleLbl);
      }
      
      public void paint(Graphics g)
      {
         if(isSelected())super.paint(g);
         else paintComponents(g);
      }
   }
   
   public static void main(String[]args)
   {
      GridBagConstraints gbc = new GridBagConstraints();
      JFrame fr = new JFrame();
      fr.setDefaultCloseOperation(fr.EXIT_ON_CLOSE);
      fr.setLayout(new GridBagLayout());
      fr.setSize(300,300);
      fr.setLocation(300,300);
      CollapsiblePanel cp = new CollapsiblePanel("Title",true);
      cp.add(new JComboBox<String>(new String[]{"A","B","C","D"}));
      CollapsiblePanel cp2 = new CollapsiblePanel("Title 2",false);
      cp2.add(new JCheckBox("Hello"));
      
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1;
      gbc.weighty = 1;
      gbc.anchor = gbc.NORTHWEST;
      fr.add(cp,gbc);
      gbc.gridy = 1;
      fr.add(cp2,gbc);
      fr.setVisible(true);
   }
   
}