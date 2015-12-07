import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.border.BevelBorder;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.Border;
import java.awt.Rectangle;
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
   MigLayout layout = new MigLayout();
   GridBagConstraints con = new GridBagConstraints();
   JXCollapsiblePane collapsingContainer = new JXCollapsiblePane();
   
   public CollapsiblePanel(String title, boolean expanded)
   {
      setLayout(layout);
      itemTitleBar = new ItemTitleBar(title,expanded);
//       con.weightx = 1;
//       con.weighty = 1;
//       con.anchor = con.NORTHWEST;
//       con.fill = con.BOTH;
//       con.gridx = 0;
//       con.gridy = 0;
// 
//       layout.setAutoCreateGaps(true);
//       layout.setAutoCreateContainerGaps(true);
//       
//       GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
//       GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//       
//       hGroup.addGroup(layout.createParallelGroup().addComponent(itemTitleBar).addComponent(collapsingContainer));
//       int i = 1/0;
//       vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(itemTitleBar));
//       vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(collapsingContainer));
//       
//       layout.setHorizontalGroup(hGroup);
//       layout.setVerticalGroup(vGroup);
//       super.add(itemTitleBar,con);
//       con.gridy = 1;
//       super.add(collapsingContainer,con);
      super.add(itemTitleBar,"fill, wrap 0");
      super.add(collapsingContainer,"");
      collapsingContainer.setLayout(new FlowLayout());
      collapsingContainer.setBorder(new BevelBorder(BevelBorder.RAISED));
      collapsingContainer.setCollapsed(!expanded);
      itemTitleBar.addItemListener(this);
      collapsingContainer.setAnimated(false);
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
         else
         {
//             Rectangle bounds = getBounds();
//             g.setColor(getParent().getBackground());
//             g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
            paintComponent(g);
            paintComponents(g);
         }
      }
   }
   
   public static void main(String[]args)
   {
//       try{UIManager.setLookAndFeel(new NimbusLookAndFeel());}
//       catch(UnsupportedLookAndFeelException e){e.printStackTrace();}
      JFrame fr = new JFrame();
      MigLayout layout = new MigLayout();
      fr.setDefaultCloseOperation(fr.EXIT_ON_CLOSE);
      fr.setLayout(layout);
      fr.setSize(300,300);
      fr.setLocation(300,300);
      CollapsiblePanel cp = new CollapsiblePanel("Title",true);
      cp.add(new JComboBox<String>(new String[]{"A","B","C","D"}));
      CollapsiblePanel cp2 = new CollapsiblePanel("Title 2",false);
      cp2.add(new JCheckBox("Hello"));
      // 
//       cp.setPreferredSize(cp.itemTitleBar.getPreferredSize());
//       cp2.setPreferredSize(cp2.itemTitleBar.getPreferredSize());
//       
//       layout.setAutoCreateGaps(true);
//       layout.setAutoCreateContainerGaps(true);
//       
//       GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
//       GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//       
//       hGroup.addGroup(layout.createParallelGroup().addComponent(cp).addComponent(cp2));
//       vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cp));
//       vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(cp2));
//       
//       layout.setHorizontalGroup(hGroup);
//       layout.setVerticalGroup(vGroup);
      fr.add(cp,"fill, wrap 0");
      fr.add(cp2,"");
      fr.setVisible(true);
   }
   
}