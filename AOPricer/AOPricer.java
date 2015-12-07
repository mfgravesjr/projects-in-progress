import javax.swing.JToggleButton;
import java.util.Vector;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;
import javax.swing.JList;
import java.awt.Desktop;
import javax.swing.JFileChooser;
import javax.swing.border.CompoundBorder;
import java.awt.event.KeyAdapter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.io.File;
import java.awt.FontMetrics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javax.swing.border.SoftBevelBorder;
import javax.swing.CellRendererPane;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.Font;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import javax.swing.Icon;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.ArrayList;
import java.awt.AWTException;
import java.awt.Robot;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;
import java.util.Collections;
import javax.swing.AbstractAction;
import java.awt.KeyboardFocusManager;
import javax.swing.text.JTextComponent;
import javax.swing.JFormattedTextField;
import javax.swing.text.JTextComponent;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.CardLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SpinnerListModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.tree.TreePath;
import javax.swing.JPopupMenu;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;

public class AOPricer extends JFrame implements ActionListener
{
   AOPricer self = this;
   
   //window contents
   private JMenuBar menuBar = new JMenuBar();
   private JTabbedPane orderTabs = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
   
   //menuBar contents
   private JMenu file = new JMenu("File");
   private JMenuItem newOrder = new JMenuItem("New Order"); // + in tabs for new order as well
   private JMenuItem open = new JMenuItem("Open Order");
   private JMenuItem importOrder = new JMenuItem("Import Order");
   private JMenuItem search = new JMenuItem("Search");
   private JMenuItem exit = new JMenuItem("Exit");
   
   private JMenu order = new JMenu("Order");
   private JMenuItem save = new JMenuItem("Save"); //Order name has changed. Use previous name. Overwrite. Save as New Order.
   private JMenuItem export = new JMenuItem("Export Item as Template");
   private JMenuItem load = new JMenuItem("Load Item Template");
   private JMenuItem finish = new JMenuItem("Finish Order"); //Order name has changed...
   private JMenuItem forward = new JMenuItem("Forward...");
   private JMenuItem print = new JMenuItem("Print");
   private JMenuItem close = new JMenuItem("Close"); // x in tabs will also close
   
   private JMenu settings = new JMenu("Settings"); //only available for admin
   private JMenuItem prices = new JMenuItem("Prices...");
   
   private JMenuItem nextTab = new JMenuItem();
   private JMenuItem previousTab = new JMenuItem();
   
   private ImageIcon trash = null;
   private ImageIcon plus = null;
   private ImageIcon shirt = null;
   
   //running variables
   private int tabCount = 0;
   private ArrayList<Order> orders = new ArrayList<Order>();
   private int currentTabIndex = 0;
   
   public AOPricer()
   {
      try{trash = new ImageIcon(ImageIO.read(getClass().getResource("trash.png")));
         plus = new ImageIcon(ImageIO.read(getClass().getResource("plus.png")));
         shirt = new ImageIcon(ImageIO.read(getClass().getResource("shirt2.png")));}
      catch(IOException e){e.printStackTrace();}
      newOrder.setMnemonic('n');
      newOrder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK));
      file.add(newOrder);
      open.setMnemonic('o');
      open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,KeyEvent.CTRL_MASK));
      file.add(open);
      importOrder.setMnemonic('i');
      importOrder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK));
      file.add(importOrder);
      search.setMnemonic('s');
      search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_MASK));
      file.add(search);
      file.add(new JSeparator());
      exit.setMnemonic('x');
      exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,KeyEvent.ALT_MASK));
      file.add(exit);
      file.setMnemonic('f');
      menuBar.add(file);
      
      save.setMnemonic('s');
      save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
      order.add(save);
      export.setMnemonic('e');
      export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
      order.add(export);
      load.setMnemonic('l');
      load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
      order.add(load);
      finish.setMnemonic('f');
      finish.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK));
      order.add(finish);
      forward.setMnemonic('w');
      forward.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END,KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK));
      order.add(forward);
      order.add(new JSeparator());
      print.setMnemonic('p');
      print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
      order.add(print);
      close.setMnemonic('l');
      close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,KeyEvent.CTRL_MASK));
      order.add(close);
      order.setMnemonic('o');
      menuBar.add(order);
      
      prices.setMnemonic('p');
      settings.add(prices);
      settings.setMnemonic('s');
      menuBar.add(settings);
      
      //HIDDEN JMENUITEM// 
   //       nextTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.CTRL_MASK));
   //       previousTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.CTRL_MASK|KeyEvent.SHIFT_MASK));
      
      newOrder.addActionListener(this);
      close.addActionListener(this);
      exit.addActionListener(this);
      
      setTitle("AO-Pricer");
      setJMenuBar(menuBar);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(720,480);
      WindowPositioner.setLocation(this,WindowPositioner.CENTER,true);
      add(orderTabs);
      order.setEnabled(false);
      setVisible(true);
      addOrder();
      orderTabs.addChangeListener(
            new ChangeListener()
            {
               public void stateChanged(ChangeEvent e)
               {
                  if(orderTabs.getSelectedIndex()!=orderTabs.getTabCount()-1)currentTabIndex = orderTabs.getSelectedIndex();
               }
            });
      orderTabs.addMouseListener(
            new MouseAdapter()
            {  
               public void mouseReleased(MouseEvent e)
               {
                  SwingUtilities.invokeLater(
                        new Runnable(){
                           public void run(){orderTabs.setSelectedIndex(currentTabIndex);}});
               }
            
               public void mouseClicked(MouseEvent e)
               {
                  if(orderTabs.getSelectedIndex()==orderTabs.getTabCount()-1)
                  {
                     addOrder();
                  }
               }
            });
            
   }
   
   public void actionPerformed(ActionEvent e)
   {
      if(e.getSource()==newOrder)
      {
         addOrder();
      }
      if(e.getSource()==close)
      {
         int option = JOptionPane.showConfirmDialog(this,"This order has not been saved. Your progress will be lost. Are you sure you want to close this order?","Exit?",JOptionPane.YES_NO_OPTION);
         if(option == JOptionPane.YES_OPTION)closeOrder(orderTabs.getSelectedIndex());
      }
      if(e.getSource()==exit)
      {
         int option = JOptionPane.showConfirmDialog(this,"There are unsaved orders. Your progress will be lost. Are you sure you want to exit?","Exit?",JOptionPane.YES_NO_OPTION);
         if(option == JOptionPane.YES_OPTION)System.exit(0);
      }
   }
   
   private void closeOrder(int index)
   {
      boolean sameIndex = (index==orderTabs.getSelectedIndex());
      orderTabs.removeTabAt(index);
      for(int i = 0; i<orderTabs.getTabCount();i++)orderTabs.setMnemonicAt(i,(i>8?57:49+i));
      Order selectedComponent = null;
      if(sameIndex&&orderTabs.getTabCount()>1)
      {
         if(orderTabs.getSelectedIndex()==orderTabs.getTabCount()-1)orderTabs.setSelectedIndex(orderTabs.getSelectedIndex()-1);
         if(orderTabs.getSelectedComponent() instanceof Order)selectedComponent = (Order)orderTabs.getSelectedComponent();
      }
      if(selectedComponent!=null)selectedComponent.tree.requestFocus();
      if(orderTabs.getTabCount()<2)order.setEnabled(false);
      if(orderTabs.getTabCount()==1)orderTabs.removeAll();
   }
   
   private void addOrder()
   {
      if(orderTabs.getTabCount()==0)
      {
         JLabel addLbl = new JLabel("+");
         addLbl.setFont(new Font("Gill Sans Ultra Bold",Font.PLAIN,12));
         addLbl.setForeground(Color.GRAY);
         orderTabs.add(
               new Component(){});
         orderTabs.setTabComponentAt(orderTabs.getTabCount()-1,addLbl);
      }
      JLabel tabX = new JLabel("  X");
      tabX.setFont(new Font("Forte",Font.PLAIN,12));
      tabX.setForeground(Color.GRAY);
      JPanel pnl = new JPanel(new BorderLayout());
      JLabel lbl = new JLabel("New Order"+(tabCount>0?" "+(tabCount+1):"")+"*");
      pnl.setOpaque(false);
      pnl.add(tabX,BorderLayout.EAST);
      pnl.add(lbl,BorderLayout.WEST);
      order.setEnabled(true);
      Order order = new Order();
   //       orders.add(order);
   //       orderTabs.add(order,"New Order"+(tabCount>1?" "+tabCount:"")+"*",orderTabs.getTabCount()-1);
      orderTabs.add(order,null,orderTabs.getTabCount()-1);
      orderTabs.setTabComponentAt(orderTabs.getTabCount()-2, pnl);
      orderTabs.setSelectedIndex(orderTabs.getTabCount()-2);
      order.tree.requestFocus();
      for(int i = 0; i<orderTabs.getTabCount();i++)orderTabs.setMnemonicAt(i,(i>8?57:49+i));
      tabX.addMouseListener(
            new MouseAdapter()
            {
               boolean buttonDown = false;
            
               public void mouseEntered(MouseEvent e)
               {
                  if(!buttonDown)tabX.setForeground(Color.RED);
               }
            
               public void mouseExited(MouseEvent e)
               {
                  if(!buttonDown)tabX.setForeground(Color.GRAY);
               }
            
               public void mousePressed(MouseEvent e)
               {
                  tabX.setForeground(Color.RED.darker());
                  buttonDown = true;
               }
            
               public void mouseReleased(MouseEvent e)
               {
                  buttonDown = false;
                  if(e.getXOnScreen()>tabX.getLocationOnScreen().x&&e.getXOnScreen()<tabX.getLocationOnScreen().x+tabX.getWidth()
                  &&e.getYOnScreen()>tabX.getLocationOnScreen().y&&e.getYOnScreen()<tabX.getLocationOnScreen().y+tabX.getHeight())
                  {
                     closeOrder(orderTabs.indexOfComponent(order));
                  }
                  tabX.setForeground(Color.GRAY);
               }
            });
   }
   
   public static void main(String[]args)
   {
      try{UIManager.setLookAndFeel(new NimbusLookAndFeel());}
      catch(UnsupportedLookAndFeelException e){e.printStackTrace();}
      new AOPricer();
   }
   
   class Order extends JPanel implements ActionListener, KeyListener, MouseListener, TreeSelectionListener
   {
      //MAIN LAYOUT
      GridBagConstraints gbc = new GridBagConstraints();
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Clothing");
      JTree tree = new JTree(root);
      JTabbedPane detailsSummaryTabs = new JTabbedPane(JTabbedPane.RIGHT);
      JLabel detailTabLabel = new JLabel("Order Details");
      JLabel summaryTabLabel = new JLabel("Order Summary");
      JPanel customerDetails = new JPanel();
      JPanel centerPnl = new JPanel();
      JLabel customerLbl = new JLabel("Customer Name");
      JTextField customerFld = new JTextField();
      JLabel contactLbl = new JLabel("Contact Info");
      JTextField contactFld = new JTextField();
      JLabel salesLbl = new JLabel("Salesperson");
      JTextField salesFld = new JTextField();
      JButton addItem = new JButton("Add Clothing");
      JPanel leftPnl = new JPanel(new GridBagLayout());
      DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
      JButton deleteItem = new JButton("X");
      JPanel detailsHolder = new JPanel();
      JPanel summaryHolder = new JPanel();
      // JPanel cuttingHolder = new JPanel();
      // JPanel embroideryHolder = new JPanel();
      // JPanel rhinestonesHolder = new JPanel();
      // JPanel advancedImagingHolder = new JPanel();
      // JPanel orderSummaryHolder = new JPanel();
      int itemCount = 1;
      CardLayout clay = new CardLayout();
      boolean valueChanged = false;
      Order self_order = this;
      JPopupMenu nodeContextMenu = new JPopupMenu();
      JMenuItem multiply = new JMenuItem("Multiply");
      JMenuItem duplicate = new JMenuItem("Duplicate");
      JMenuItem rename = new JMenuItem("Rename");
      JMenuItem delete = new JMenuItem("Delete");
      // ArrayList<Integer> associatedTabIndex = new ArrayList<Integer>();
      ArrayList<DefaultMutableTreeNode> items = new ArrayList<DefaultMutableTreeNode>();
      JDialog quantityDialog = new JDialog(self,"Enter Quantity", true);
      JLabel quantityLbl = new JLabel("<html>Enter the quantity you would like to add of this item.</html>");
      SpinnerNumberModel quantityModel = new SpinnerNumberModel(2,2,100,1);
      JSpinner quantitySpinner = new JSpinner(quantityModel);
      JButton okayBtn = new JButton("OK");
      JButton cancelBtn = new JButton("Cancel");
      ArrayList<DefaultMutableTreeNode> quantityNodes = new ArrayList<DefaultMutableTreeNode>();
      JPopupMenu quantityPopup = new JPopupMenu();
      JMenuItem remove = new JMenuItem("Remove");
      JPopupMenu addPopup = new JPopupMenu();
      JMenuItem add = new JMenuItem("Add Clothing");
      
      JTextArea summary = new JTextArea();
      JScrollPane summaryPane = new JScrollPane(summary);
      JLabel priceAdjLbl = new JLabel("Price Adj: ");
      JTextField priceAdjFld = new JTextField();
      JCheckBox payOnPickup = new JCheckBox("Pay On Pickup ",false);
      JButton forwardSumBtn = new JButton("Forward Order");
      JButton finishSumBtn = new JButton("Finish Order");
   
      private Order()
      {
         //MAIN LAYOUT
         tabCount++;
         GridBagLayout gLay = new GridBagLayout();
         int w = (int)(((double)(self.getContentPane().getWidth()))/3.);
         gLay.columnWidths = new int[]{w,w,w};
         customerDetails.setLayout(gLay);
         gbc.insets = new Insets(0,10,0,0);
         gbc.anchor = gbc.LINE_START;
         gbc.fill = gbc.HORIZONTAL;
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 1;
         customerDetails.add(customerFld,gbc);
         gbc.gridx = 1;
         gbc.insets = new Insets(0,0,0,0);
         customerDetails.add(contactFld,gbc);
         gbc.gridx = 2;
         gbc.insets = new Insets(0,0,0,10);
         customerDetails.add(salesFld,gbc);
         gbc.gridy = 1;
         gbc.insets = new Insets(0,20,0,0);
         gbc.gridx = 0;
         customerDetails.add(customerLbl,gbc);
         gbc.gridx = 1;
         gbc.insets = new Insets(0,10,0,0);
         customerDetails.add(contactLbl,gbc);
         gbc.gridx = 2;
         gbc.insets = new Insets(0,10,0,0);
         customerDetails.add(salesLbl,gbc);
         
         //summary tab layout
         GridBagLayout summaryGridBagLayout = new GridBagLayout();
         summaryGridBagLayout.rowHeights = new int[]{300,0};
         summaryGridBagLayout.columnWidths = new int[]{0,50,0,0,0};
         summaryHolder.setLayout(summaryGridBagLayout);
         summary.setLineWrap(true);
         summary.setWrapStyleWord(true);
         summary.setEditable(false);
      //                summary.setBackground(new Color(180,190,250));
      //          summary.setPreferredSize(new Dimension(250,0));
         GridBagConstraints gbConst = new GridBagConstraints();
         gbConst.gridx = 0;
         gbConst.gridy = 0;
         gbConst.gridwidth = 5;
         gbConst.gridheight = 1;
         gbConst.weighty = 1;
         gbConst.weightx = 1;
         gbConst.anchor = gbConst.CENTER;
         gbConst.fill = gbConst.BOTH;
         summaryHolder.add(summaryPane,gbConst);
         
         gbConst.gridwidth = 1;
         gbConst.insets = new Insets(0,5,0,0);
         gbConst.anchor = gbConst.CENTER;
         gbConst.fill = gbConst.NONE;
         gbConst.gridy = 1;
         summaryHolder.add(priceAdjLbl,gbConst);
         
         gbConst.insets = new Insets(0,0,0,0);
         gbConst.gridx = 1;
         gbConst.fill = gbConst.HORIZONTAL;
         summaryHolder.add(priceAdjFld,gbConst);
         
         gbConst.gridx = 2;
         gbConst.fill = gbConst.NONE;
         summaryHolder.add(payOnPickup,gbConst);
         
         gbConst.gridx = 3;
         summaryHolder.add(forwardSumBtn,gbConst);
         
         gbConst.gridx = 4;
         gbConst.insets = new Insets(0,0,0,5);
         summaryHolder.add(finishSumBtn,gbConst);
         
         priceAdjFld.setPreferredSize(priceAdjFld.getPreferredSize());
         priceAdjFld.setToolTipText("<html>Type any dollar amount to add cost.<br>Type the minus sign (-) before the amount for discounts.<br>You may format the discount as a percentage by typing X%<br>You may not discount more than 25% of the listed price.</html>");
         detailsHolder.setLayout(clay);
         detailTabLabel.setUI(new RotatedLabelUI(true));
         summaryTabLabel.setUI(new RotatedLabelUI(true));
         detailsSummaryTabs.add("",detailsHolder);
         detailsSummaryTabs.setTabComponentAt(0,detailTabLabel);
         detailsSummaryTabs.add("",summaryHolder);
         detailsSummaryTabs.setTabComponentAt(1,summaryTabLabel);
      //          cuttingHolder.setLayout(clay);
      //          embroideryHolder.setLayout(clay);
      //          rhinestonesHolder.setLayout(clay);
      //          advancedImagingHolder.setLayout(clay);
      //          orderSummaryHolder.setLayout(clay);
      //          orderDetails.add("Cutting",cuttingHolder);
      //          orderDetails.add("Embroidery",embroideryHolder);
      //          orderDetails.add("Rhinestones",rhinestonesHolder);
      //          orderDetails.add("Advanced Imaging",advancedImagingHolder);
      //          orderDetails.add("Order Summary",orderSummaryHolder);
         setLayout(new BorderLayout(5,0));
         GridBagLayout gLay2 = new GridBagLayout();
         gbc = new GridBagConstraints();
         centerPnl.setLayout(gLay2);
         w = (int)(((double)(self.getContentPane().getWidth()))/3.);
         gLay2.columnWidths = new int[]{w-(detailsHolder.getPreferredSize().width/3),(w-detailsHolder.getPreferredSize().width/3)*2};
         gbc.gridx = 1;
         gbc.gridy = 1;
         leftPnl.add(deleteItem,gbc);
         gbc.fill = gbc.BOTH;
         gbc.anchor = gbc.LINE_START;
         gbc.weightx = 1;
         gbc.gridx = 0;
         leftPnl.setBorder(new BevelBorder(BevelBorder.LOWERED));
         leftPnl.add(addItem,gbc);
         gbc.weighty = 1;
         gbc.gridy = 0;
         gbc.gridwidth = 2;
         leftPnl.add(tree,gbc);
         gbc.gridwidth = 1;
         gbc.insets = new Insets(0,10,0,0);
         centerPnl.add(leftPnl,gbc);
         gbc.insets = new Insets(0,0,0,10);
         gbc.gridwidth = 1;
         gbc.gridheight = 2;
         gbc.gridy = 0;
         gbc.gridx = 1;
         detailsSummaryTabs.setBorder(new BevelBorder(BevelBorder.LOWERED));
      //         orderDetails.setBorder(new BevelBorder(BevelBorder.LOWERED));
         // centerPnl.add(orderDetails,gbc);
         centerPnl.add(detailsSummaryTabs,gbc);
         add(centerPnl,BorderLayout.CENTER);
         add(customerDetails,BorderLayout.SOUTH);
         
         addItem.addActionListener(this);
         deleteItem.addActionListener(this);
         tree.setEditable(false);
         tree.setRootVisible(false);
         tree.addKeyListener(this);
         tree.addTreeSelectionListener(this);
         tree.addMouseListener(this);
      //          cuttingHolder.addMouseListener(this);
         // orderDetails.addChangeListener(this);
         // 
         getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,KeyEvent.SHIFT_MASK),"addingclothes");
         getActionMap().put("addingclothes",
               new AbstractAction(){
                  public void actionPerformed(ActionEvent e){
                     if(!textAreaHasFocus())addClothing();}});
         getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),"renamingnode");
         getActionMap().put("renamingnode",
               new AbstractAction(){
                  public void actionPerformed(ActionEvent e){renameNode();}});
         getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTEXT_MENU,0),"opencontextmenu");
         getActionMap().put("opencontextmenu",
               new AbstractAction(){
                  public void actionPerformed(ActionEvent e)
                  {
                     if(!textAreaHasFocus())nodeContextMenu.show(self,tree.getLocationOnScreen().x-self.getLocation().x+(tree.getWidth()/2),tree.getLocationOnScreen().y-self.getLocation().y+((tree.getRowForPath(new TreePath(model.getPathToRoot((DefaultMutableTreeNode)tree.getLastSelectedPathComponent())))+1)*tree.getRowBounds(0).height-(tree.getRowBounds(0).height/2)));
                  }});//fix this
      
         getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0),"summarytab");
         getActionMap().put("summarytab",
               new AbstractAction(){
                  public void actionPerformed(ActionEvent e){
                     if(!textAreaHasFocus())goToTab(summaryHolder);}});
         getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0),"detailtab");
         getActionMap().put("detailtab",
               new AbstractAction(){
                  public void actionPerformed(ActionEvent e){
                     if(!textAreaHasFocus())goToTab(detailsHolder);}});
         
         
         //tree context menus
         nodeContextMenu.add(multiply);
         nodeContextMenu.add(duplicate);
         nodeContextMenu.add(rename);
         nodeContextMenu.add(delete);
         multiply.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));
         duplicate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_MASK));
         rename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0));
         delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
         multiply.setMnemonic('m');
         duplicate.setMnemonic('d');
         rename.setMnemonic('n');
         delete.setMnemonic('l');
         ActionListener contextMenuListener = 
            new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                  if(e.getSource()==multiply) quantityDialog.setVisible(true);
                  if(e.getSource()==duplicate) duplicateNode();
                  if(e.getSource()==rename) renameNode();
                  if(e.getSource()==delete) deleteSelectedNode();
                  if(e.getSource()==remove) 
                     while(((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getParent()!=root)deleteSelectedNode();
               }
            };
         multiply.addActionListener(contextMenuListener);
         duplicate.addActionListener(contextMenuListener);
         rename.addActionListener(contextMenuListener);
         delete.addActionListener(contextMenuListener);
         
         DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
         renderer.setLeafIcon(shirt);
         renderer.setClosedIcon(shirt);
         renderer.setOpenIcon(shirt);
         
         remove.setMnemonic('r');
         remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
         quantityPopup.add(remove);
         remove.addActionListener(contextMenuListener);
         
         add.setMnemonic('a');
         add.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,KeyEvent.SHIFT_MASK));
         addPopup.add(add);
         add.addActionListener(
               new ActionListener()
               {
                  public void actionPerformed(ActionEvent e)
                  {
                     addClothing();
                  }
               });
         
         //custom sizing of components
         detailsHolder.setPreferredSize(new Dimension((self.getContentPane().getWidth()-detailsSummaryTabs.getPreferredSize().width)/3*2,detailsHolder.getPreferredSize().height));
         summaryHolder.setPreferredSize(new Dimension((self.getContentPane().getWidth()-detailsSummaryTabs.getPreferredSize().width)/3*2,summaryHolder.getPreferredSize().height));
         
         //creating dialog
         quantityDialog.setLayout(new GridBagLayout());
         quantityDialog.setSize(300,150);
         WindowPositioner.setLocation(quantityDialog,WindowPositioner.CENTER,true);
         GridBagConstraints gbc = new GridBagConstraints();
         
         gbc.weightx = 1;
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.gridwidth = 2;
         gbc.insets = new Insets(20,20,20,20);
         quantityDialog.add(quantityLbl,gbc);
         
         gbc.gridy = 1;
         gbc.insets = new Insets(0,0,20,0);
         quantityDialog.add(quantitySpinner,gbc);
         
         gbc.gridx = 0;
         gbc.gridy = 2;
         gbc.gridwidth = 1;
         gbc.anchor = gbc.LINE_END;
         gbc.insets = new Insets(0,0,10,0);
         quantityDialog.add(okayBtn,gbc);
         
         gbc.gridx = 1;
         gbc.anchor = gbc.LINE_START;
         quantityDialog.add(cancelBtn,gbc);
         quantityDialog.pack();
         
         okayBtn.addActionListener(
               new ActionListener()
               {
                  public void actionPerformed(ActionEvent e)
                  {
                     DefaultMutableTreeNode clothing = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                     if(clothing!=null)
                     {
                        if(clothing.getChildCount()>0)
                        {
                           ((DefaultMutableTreeNode)clothing.getFirstChild()).setUserObject("Qty "+quantitySpinner.getValue().toString());
                        }
                        else
                        {
                           DefaultMutableTreeNode quantityNode = new DefaultMutableTreeNode("Qty "+quantitySpinner.getValue().toString());
                           clothing.add(quantityNode);
                           quantityNodes.add(quantityNode);
                        }
                        model.reload();
                        tree.setSelectionPath(new TreePath(clothing.getPath()));
                        for(int i = 0; i < tree.getRowCount(); i++)tree.expandRow(i);
                     }
                     quantitySpinner.setValue(2);
                     quantityDialog.setVisible(false);
                  }
               });
         cancelBtn.addActionListener(
               new ActionListener()
               {
                  public void actionPerformed(ActionEvent e)
                  {
                     quantitySpinner.setValue(2);
                     quantityDialog.setVisible(false);
                  }
               });
         priceAdjFld.addKeyListener(
               new KeyAdapter()
               {
                  public void keyReleased(KeyEvent e)
                  {
                     refreshSummary();
                  }
               });
         detailsSummaryTabs.addChangeListener(
               new ChangeListener()
               {
                  public void stateChanged(ChangeEvent e)
                  {
                     refreshSummary();
                  }
               });
         payOnPickup.addMouseListener(
               new MouseAdapter()
               {
                  public void mouseClicked(MouseEvent e)
                  {
                     refreshSummary();
                  }
               });
      }
      
      private boolean hasOrderChanged(Work w)
      {
         return (w.hasLogoTabChanged()||hasTextTabChanged()||hasAdvancedTabChanged()||hasEmbroideryTabChanged()||hasRhinestonesTabChanged());
      }
      
      private boolean hasTextTabChanged(){
         return false;}
      private boolean hasAdvancedTabChanged(){
         return false;}
      private boolean hasEmbroideryTabChanged(){
         return false;}
      private boolean hasRhinestonesTabChanged(){
         return false;}
      
      private void refreshSummary()
      {
         int increment = summary.getFontMetrics(summary.getFont()).charWidth(' ');
         double price = 0.;
         summary.setText("");
         summary.append("Sales Order Summary\n\n");
         if(!customerFld.getText().equals(""))
         {
            summary.append("Customer\n");
            summary.append(customerFld.getText()+"\n");
         }
         if(!contactFld.getText().equals(""))
         {
            summary.append(contactFld.getText()+"\n");
         }
         if(!customerFld.getText().equals("")||!contactFld.getText().equals(""))
         {
            summary.append("\n");
         }
         if(!salesFld.getText().equals(""))
         {
            summary.append("Salesperson\n");
            summary.append(salesFld.getText()+"\n\n");
         }
      //          for(DefaultMutableTreeNode node:items)
      //          {
         for(Component c: detailsHolder.getComponents())
            if(c instanceof Work)
               if(((Work)c).hasLogoTabChanged())
               {
                  Work w = (Work)c;
                  FontMetrics fm = summary.getFontMetrics(summary.getFont());
                  int hyphenWidth = fm.charWidth('-');
                  String verticalLine = "";
                  StringBuilder builder = new StringBuilder();
                  int constraints = summary.getWidth() - summary.getInsets().left - summary.getInsets().right - summaryPane.getVerticalScrollBar().getPreferredSize().width;
                  for(int i = hyphenWidth; i < constraints; i+= hyphenWidth) builder.append("-");
                  verticalLine = builder.toString();
                  summary.append(verticalLine+"\n"+w.associatedNode.getUserObject().toString()+"\n"+verticalLine+"\n");
                  summary.append("Custom Logos\n");
                  if(!w.logoTab.designDescription.getText().equals(""))
                  {
                     summary.append("Details:  "+w.logoTab.designDescription.getText()+"\n");
                  }
                  if(w.logoTab.file != null)
                  {
                     summary.append("Refer to file named "+w.logoTab.file.getName()+"\n");
                  }
                  for(Work.LogoTab.LogoEntry entry: w.logoTab.entries)
                  {
                     if(!entry.filmType.getSelectedItem().toString().equals("--"))
                     {
                        double subprice = 0.;
                        switch(entry.filmType.getSelectedItem().toString())
                        {
                           case "TF": subprice = 1.5;
                              break;
                           case "FF": subprice = 1.5;
                              break;
                           case "GF": subprice = 2.;
                              break;
                           case "FL": subprice = 2.;
                              break;
                           case "SP": subprice = 2.5;
                              break;
                           case "SV": subprice = 1.;
                              break;
                        }
                        subprice*=(Integer.parseInt(entry.verticalInchesSpinner.getValue().toString())+2);
                        price+=(subprice*(Integer.parseInt((w.associatedNode.getChildCount()>0?((DefaultMutableTreeNode)w.associatedNode.getFirstChild()).getUserObject().toString().split(" ")[1]:"1"))));
                        builder = new StringBuilder();
                        builder.append((entry.logoTextDescriptionFld.getText().equals("")?"":entry.logoTextDescriptionFld.getText()+" - "));
                        int inches = Integer.parseInt(entry.verticalInchesSpinner.getValue().toString());
                        builder.append(inches+" inch"+(inches>1?"es":"")+" of ");
                        builder.append(entry.colorBox.getColorName()+" ");
                        builder.append(entry.filmOptions[entry.filmType.getSelectedIndex()]+" ");
                        if(!entry.filmType.getSelectedItem().toString().equals("SV"))builder.append(entry.location.getSelectedItem().toString());
                        String entryStr = builder.toString();
                        summary.append(entryStr);
                        String priceStr = String.format((w.associatedNode.getChildCount()>0?((DefaultMutableTreeNode)w.associatedNode.getFirstChild()).getUserObject().toString().split(" ")[1]+"@":"")+"$%.2f",subprice);
                        constraints = summary.getWidth()-(summary.getFontMetrics(summary.getFont()).stringWidth(entryStr)+summary.getFontMetrics(summary.getFont()).stringWidth(priceStr)+summary.getInsets().left+summary.getInsets().right + summaryPane.getVerticalScrollBar().getPreferredSize().width);
                        for(int i = increment; i < constraints; i+=increment)
                        {
                           summary.append(" ");
                        }
                        summary.append(priceStr+"\n");
                     }
                     else summary.append("No film type is selected. Go back to Order Details to update the order.\n");
                  }
               //                   summary.append("\n");
                  double timeEstimatePrice = 50.;
                  List l = ((SpinnerListModel)w.logoTab.timeEstimateSpinner.getModel()).getList();
                  if(w.logoTab.drawInHouse.isSelected())
                  {
                     for(int i = 0; i < l.size(); i++)
                     {
                        if(l.get(i).equals(w.logoTab.timeEstimateSpinner.getValue()))
                        {
                           timeEstimatePrice*=i;
                           if(i>0)timeEstimatePrice-=25;
                        }
                     }
                  }
                  else timeEstimatePrice -= 10;
                  price += timeEstimatePrice;
                  String timeEstimateStr = "";
                  if(w.logoTab.drawInHouse.isSelected())timeEstimateStr = "Art Fee for "+w.logoTab.timeEstimateSpinner.getValue().toString();
                  else timeEstimateStr = "Vectoring Service Fee";
               
                  String timeEstimatePriceStr = String.format("$%.2f",timeEstimatePrice);
                  constraints = summary.getWidth()-(summary.getFontMetrics(summary.getFont()).stringWidth(timeEstimateStr+timeEstimatePriceStr+summary.getInsets().left+summary.getInsets().right + summaryPane.getVerticalScrollBar().getPreferredSize().width));
                  summary.append(timeEstimateStr);
               
                  for(int i = increment; i < constraints; i+=increment)
                  {
                     summary.append(" ");
                  }
                  summary.append(timeEstimatePriceStr+"\n");
               
               }
         summary.append("\n");
         if(!priceAdjFld.getText().equals(""))
         {
            String adjStr = priceAdjFld.getText();
            StringBuilder bldr = new StringBuilder();
            for(String str: adjStr.split("\\$"))
            {
               bldr.append(str);
            }
            adjStr = bldr.toString();
            if(!adjStr.equals(""))
            {
               if(adjStr.matches("[0-9]{1,2}%"))
               {
                  adjStr = adjStr.split("%")[0];
                  double adjPrice = Double.parseDouble(adjStr)/100.*price*-1;
                  if(price+adjPrice<0||Double.parseDouble(adjStr)>25)
                  {
                     priceAdjFld.setBorder(new LineBorder(Color.RED,2));
                  }
                  else
                  {
                     priceAdjFld.setBorder(new JTextField().getBorder());
                     summary.append("Adjustment");
                     price+= adjPrice;
                     String dollarSign = "-$";
                     String adjFormattedStr = String.format(dollarSign+"%.2f",Math.abs(adjPrice));
                     int constraints = summary.getWidth()-(summary.getFontMetrics(summary.getFont()).stringWidth("Adjustment"+adjFormattedStr)+summary.getInsets().left+summary.getInsets().right+summaryPane.getVerticalScrollBar().getPreferredSize().width);
                     for(int i = increment; i < constraints; i+= increment)
                     {
                        summary.append(" ");
                     }
                     summary.append(adjFormattedStr+"\n");
                  }
               }
               else if(!adjStr.matches("-{0,1}[0-9]*[.]{0,1}[0-9]{0,2}")||adjStr.matches("-")||adjStr.matches("-{0,1}[0-9]*[.]"))
               {
                  priceAdjFld.setBorder(new LineBorder(Color.RED,2));
               }
               else if(Double.parseDouble(adjStr)<(-.25*price))
               {
                  priceAdjFld.setBorder(new LineBorder(Color.RED,2));
               }
               else
               {
                  double adjPrice = Double.parseDouble(adjStr);
                  if(price+adjPrice<0)
                  {
                     priceAdjFld.setBorder(new LineBorder(Color.RED,2));
                  }
                  else
                  {
                     priceAdjFld.setBorder(new JTextField().getBorder());
                     summary.append("Adjustment");
                     price+= adjPrice;
                     String dollarSign = (adjStr.contains("-")?"-$":"$");
                     String adjFormattedStr = String.format(dollarSign+"%.2f",Math.abs(adjPrice));
                     int constraints = summary.getWidth()-(summary.getFontMetrics(summary.getFont()).stringWidth("Adjustment"+adjFormattedStr)+summary.getInsets().left+summary.getInsets().right+summaryPane.getVerticalScrollBar().getPreferredSize().width);
                     for(int i = increment; i < constraints; i+= increment)
                     {
                        summary.append(" ");
                     }
                     summary.append(adjFormattedStr+"\n");
                  }
               }
            }
         }
         else priceAdjFld.setBorder(new JTextField().getBorder());
         
         String priceString = String.format("$%.2f",price);
         summary.append("Total");
         int constraints = summary.getWidth()-(summary.getFontMetrics(summary.getFont()).stringWidth("Total"+priceString)+summary.getInsets().left+summary.getInsets().right+summaryPane.getVerticalScrollBar().getPreferredSize().width);
         for(int i = increment; i < constraints; i+=increment)
         {
            summary.append(" ");
         }
         summary.append(priceString+"\n");
         String paidString = "PAID";
         if(payOnPickup.isSelected()) paidString = "NOT PAID";
         constraints = summary.getWidth()-(summary.getFontMetrics(summary.getFont()).stringWidth(paidString)+summary.getInsets().left+summary.getInsets().right+summaryPane.getVerticalScrollBar().getPreferredSize().width);
         for(int i = increment; i < constraints; i+=increment)
         {
            summary.append(" ");
         }
         summary.append(paidString);
      }
      
      private void goToTab(JPanel pnl)
      {
         detailsSummaryTabs.setSelectedComponent(pnl);
         tree.requestFocus();
      }
      
      private void renameNode()
      {
         if(tree.getLastSelectedPathComponent()!=null&&((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getParent()==root)
         {
            valueChanged = false;
            tree.setEditable(true);
            tree.startEditingAtPath(new TreePath(model.getPathToRoot((DefaultMutableTreeNode)tree.getLastSelectedPathComponent())));
         }
      }
      
      private boolean textAreaHasFocus()
      {
         return (getFocusOwner() instanceof JTextArea && ((JTextArea)getFocusOwner()).isEditable())
            ||(getFocusOwner() instanceof JComboBox)
            ||
            (
               (getFocusOwner() instanceof JTextField)
               &&!(getFocusOwner() instanceof JFormattedTextField)
            );
      }
      
      private void deleteSelectedNode()
      {
         DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
         if(selectedNode.getParent().equals(root))
         {
            if(selectedNode!=null)
            {
               int option = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete "+selectedNode.toString()+" from the order? You will lose all data associated with this item.","Delete Item?",JOptionPane.YES_NO_OPTION);
               if(option==JOptionPane.YES_OPTION)
               {
                  DefaultMutableTreeNode previousNode = (DefaultMutableTreeNode)root.getChildBefore(selectedNode);
                  DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode)root.getChildAfter(selectedNode);
                  selectedNode.removeFromParent();
                  model.reload();
                  Component compToRemove = null;
                  for(Component c: detailsHolder.getComponents())
                     if(c instanceof Work)
                        if(((Work)c).associatedNode == selectedNode)compToRemove = c;
                  if(compToRemove!=null)detailsHolder.remove(compToRemove);
                  if(previousNode!=null)tree.setSelectionPath(new TreePath(previousNode.getPath()));
                  else if(nextNode!=null)tree.setSelectionPath(new TreePath(nextNode.getPath()));
                  tree.requestFocus();
                  valueChanged = false;
                  //
               }
            }
         }
         else
         {
            Object userObject = selectedNode.getUserObject();
            if(!userObject.toString().split(" ")[1].toString().equals("2"))
            {
               selectedNode.setUserObject("Qty "+(new Integer(Integer.parseInt(userObject.toString().split(" ")[1])-1)).toString());
            }
            else 
            {
               DefaultMutableTreeNode nodeToSelect = (DefaultMutableTreeNode)selectedNode.getPreviousNode();
               ((DefaultMutableTreeNode)selectedNode.getParent()).remove(selectedNode);
               quantityNodes.remove(selectedNode);
               selectedNode = nodeToSelect;
            }
            model.reload();
            tree.setSelectionPath(new TreePath(selectedNode.getPath()));
         }
         for(int i = 0; i < tree.getRowCount(); i++)tree.expandRow(i);
      }
      
      private void addClothing()
      {
         DefaultMutableTreeNode clothing = new DefaultMutableTreeNode("Item#"+itemCount);
      //          LogoTab logoTab = new LogoTab(clothing,itemCount);
      //          associatedTabIndex.add(new Integer(orderDetails.indexOfComponent(cuttingHolder)));
      //          cuttingHolder.add(logoTab,""+itemCount);
      //          visiblePanels.add(cuttingHolder); //BOOKMARK
         items.add(clothing);
         Work work = new Work(clothing, itemCount);
         detailsHolder.add(work,""+itemCount);
         itemCount++;
         root.add(clothing);
         model.reload();
         tree.setSelectionPath(new TreePath(clothing.getPath()));
         tree.requestFocus();
         valueChanged = false;
         for(int i = 0; i < tree.getRowCount(); i++)tree.expandRow(i);
      }
      
      private void duplicateNode()
      {
         DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
         if(selectedNode!=null)
         {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Copy of "+selectedNode.getUserObject());
            items.add(newNode);
            Work work = new Work(newNode, itemCount);
            detailsHolder.add(work,""+itemCount);
            itemCount++;
            root.add(newNode);
            model.reload();
            tree.setSelectionPath(new TreePath(newNode.getPath()));
            tree.requestFocus();
            valueChanged = false;
            for(int i = 0; i < tree.getRowCount(); i++)tree.expandRow(i);
            work.logoTab.designDescription.setText("*TO-DO: copy components from work class in duplicated node");
         }
      }
      
      private void multiplyNode()
      {
         DefaultMutableTreeNode clothing = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
         if(clothing!=null)
         {
            if(!clothing.getParent().equals(root))clothing = (DefaultMutableTreeNode)clothing.getParent();
            if(clothing.getChildCount()==0)
            {
               DefaultMutableTreeNode quantityNode = new DefaultMutableTreeNode("Qty 2");
               clothing.add(quantityNode);
               quantityNodes.add(quantityNode);
            }
            else
            {
               Object userObject = ((DefaultMutableTreeNode)clothing.getFirstChild()).getUserObject();
               ((DefaultMutableTreeNode)((DefaultMutableTreeNode)clothing).getFirstChild()).setUserObject("Qty "+(new Integer(Integer.parseInt(userObject.toString().split(" ")[1])+1)).toString());
            }
            model.reload();
            tree.setSelectionPath(new TreePath(clothing.getPath()));
            for(int i = 0; i < tree.getRowCount(); i++)tree.expandRow(i);
         }
      }
      
      public void actionPerformed(ActionEvent e)
      {
         if(e.getSource()==addItem)
         {
            addClothing();
         }
         
         if(e.getSource()==deleteItem)
         {
            deleteSelectedNode();
         }
      }
      
      public void valueChanged(TreeSelectionEvent e)
      {
         valueChanged = true;
         DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
         LABEL: 
         if(selectedNode!=null)
            for(Component c: detailsHolder.getComponents())
            {
               if(c instanceof Work)
               {
                  if(((Work)c).associatedNode == selectedNode)
                  {
                     clay.show(detailsHolder,((Work)c).itemNumber);
                     break LABEL;
                  }
                  else if(((Work)c).associatedNode == (DefaultMutableTreeNode)selectedNode.getParent())
                  {
                     clay.show(detailsHolder,((Work)c).itemNumber);
                     break LABEL;
                  }
               }
            }
         
         detailsHolder.setPreferredSize(new Dimension((self.getContentPane().getWidth()-detailsSummaryTabs.getPreferredSize().width)/3*2,detailsHolder.getPreferredSize().height));
         summaryHolder.setPreferredSize(new Dimension((self.getContentPane().getWidth()-detailsSummaryTabs.getPreferredSize().width)/3*2,summaryHolder.getPreferredSize().height));
         // for(DefaultMutableTreeNode i:items)
            // if(i==tree.getLastSelectedPathComponent())orderDetails.setSelectedIndex(associatedTabIndex.get(items.indexOf(i)));
      }
      
      public void keyPressed(KeyEvent e)
      {
         if(e.getKeyCode()==e.VK_DELETE&&e.getModifiers()==0)
         {
            deleteSelectedNode();
         }
         
         if(e.getKeyCode()==e.VK_F2&&e.getModifiers()==0)
         {
            renameNode();
         }
         
         if(e.getKeyCode()==e.VK_M&&e.getModifiers()==e.CTRL_MASK)
         {
            multiplyNode();
         }
         
         if(e.getKeyCode()==e.VK_D&&e.getModifiers()==e.CTRL_MASK)
         {
            duplicateNode();
         }
      }
      
      public void keyReleased(KeyEvent e)
      {
         if(e.getKeyCode()==e.VK_DOWN||e.getKeyCode()==e.VK_UP)valueChanged = false;
      }
      
      public void keyTyped(KeyEvent e)
      {
         
      }
      
      public void mousePressed(MouseEvent e)
      {
         DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
         int rowNumber = 0;
         if(selectedNode!=null)
         {
            rowNumber = tree.getRowForPath(new TreePath(selectedNode.getPath()));
         
            if(e.getButton()==1)
            {
               if(selectedNode!=null&&(selectedNode.getParent().equals(root))&&e.getSource()==tree&&e.getY()<tree.getRowBounds(0).height*(rowNumber+1)&&e.getY()>tree.getRowBounds(0).height*(rowNumber))
               {
                  Object[] pathToRoot = model.getPathToRoot((DefaultMutableTreeNode)tree.getLastSelectedPathComponent());
                  TreePath path = null;
                  if(pathToRoot!=null)path = new TreePath(pathToRoot);
                  
                  if(!valueChanged&&path!=null)
                  {
                     valueChanged = true;
                     tree.setEditable(true);
                     tree.startEditingAtPath(path);
                  }
                  else 
                  {
                     tree.stopEditing();
                  }
               }
               else if(!tree.isEditing())tree.setEditable(false);
            }
         }
         valueChanged = false;
         for(DefaultMutableTreeNode n: items) 
         {
            if(n!=null)
            {
               rowNumber = tree.getRowForPath(new TreePath(n.getPath()));
               if(e.getButton()==3&&e.getSource()==tree&&e.getY()<tree.getRowBounds(0).height*(rowNumber+1)&&e.getY()>tree.getRowBounds(0).height*(rowNumber))
               {
                  tree.setSelectionPath(new TreePath(n.getPath()));
                  nodeContextMenu.show(this,e.getX(),e.getY());
               }
            }
         }
         for(DefaultMutableTreeNode n: quantityNodes) 
         {
            if(n!=null)
            {
               rowNumber = tree.getRowForPath(new TreePath(n.getPath()));
               if(e.getButton()==3&&e.getSource()==tree&&e.getY()<tree.getRowBounds(0).height*(rowNumber+1)&&e.getY()>tree.getRowBounds(0).height*(rowNumber))
               {
                  tree.setSelectionPath(new TreePath(n.getPath()));
                  quantityPopup.show(this,e.getX(),e.getY());
               }
            }
         }
         if(e.getButton()==3&&!quantityPopup.isVisible()&&!nodeContextMenu.isVisible())
         {
            addPopup.show(this,e.getX(),e.getY());
         }
         
         if(e.getSource() instanceof JPanel) tree.requestFocus();
      }
      
      public void mouseEntered(MouseEvent e){}
      public void mouseExited(MouseEvent e){}
      public void mouseClicked(MouseEvent e){}
      public void mouseReleased(MouseEvent e){}
      // 
      // public void stateChanged(ChangeEvent e)
      // {
         // if(e.getSource()==orderDetails)
         // {
            // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            // int itemIndex = -1;
            // if(selectedNode!=null)itemIndex = items.indexOf(selectedNode);
            // if(itemIndex>=0)associatedTabIndex.set(itemIndex, orderDetails.getSelectedIndex());
         // }
      // }
      class Work extends JPanel
      {
         JTabbedPane orderDetails = new JTabbedPane(JTabbedPane.BOTTOM);
         String itemNumber;
         DefaultMutableTreeNode associatedNode;
         JPanel embroideryHolder = new JPanel();
         JPanel rhinestonesHolder = new JPanel();
         JPanel advancedImagingHolder = new JPanel();
         LogoTab logoTab = new LogoTab();
         TextTab textTab = new TextTab();
            
         public Work(DefaultMutableTreeNode clothing, int itemCount)
         {
            setLayout(new BorderLayout());
            itemNumber = ""+itemCount;
            associatedNode = clothing;
            add(orderDetails,BorderLayout.CENTER);
            orderDetails.add("Custom Logos",logoTab);
            orderDetails.add("Text And Numbers",textTab);
            orderDetails.add("Advanced Imaging",advancedImagingHolder);
            orderDetails.add("Embroidery",embroideryHolder);
            orderDetails.add("Rhinestones",rhinestonesHolder);
               
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C,0),"logotab");
            getActionMap().put("logotab",
                  new AbstractAction(){
                     public void actionPerformed(ActionEvent e){
                        if(!textAreaHasFocus())goToTab(logoTab);}});
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T,0),"texttab");
            getActionMap().put("texttab",
                  new AbstractAction(){
                     public void actionPerformed(ActionEvent e){
                        if(!textAreaHasFocus())goToTab(textTab);}});
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E,0),"embroiderytab");
            getActionMap().put("embroiderytab",
                  new AbstractAction(){
                     public void actionPerformed(ActionEvent e){
                        if(!textAreaHasFocus())goToTab(embroideryHolder);}});
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R,0),"rhinestonetab");
            getActionMap().put("rhinestonetab",
                  new AbstractAction(){
                     public void actionPerformed(ActionEvent e){
                        if(!textAreaHasFocus())goToTab(rhinestonesHolder);}});
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0),"advancedtab");
            getActionMap().put("advancedtab",
                  new AbstractAction(){
                     public void actionPerformed(ActionEvent e){
                        if(!textAreaHasFocus())goToTab(advancedImagingHolder);}});
         }
         
         private boolean hasLogoTabChanged()
         {
            if(!logoTab.designDescription.getText().equals("")) 
               return true;
            if(logoTab.file!=null) 
               return true;
            if(!logoTab.timeEstimateSpinner.getValue().toString().equals("<30min")) 
               return true;
            if(logoTab.sendForVectoring.isSelected()) 
               return true;
            for(LogoTab.LogoEntry entry: logoTab.entries)
            {
               if(!entry.logoTextDescriptionFld.getText().equals("")) 
                  return true;
               if(!entry.location.getSelectedItem().toString().equals("Center Chest")) 
                  return true;
               if(!entry.filmType.getSelectedItem().toString().equals("--")) 
                  return true;
               if(Integer.parseInt(entry.verticalInchesSpinner.getValue().toString())!=1) 
                  return true;
               if(entry.colorBox.getColor()!=Color.WHITE) 
                  return true;
            }
            return false;
         }
      
         private void goToTab(JPanel pnl)
         {
            orderDetails.setSelectedComponent(pnl);
            tree.requestFocus();
         
         }
      
         class LogoTab extends JPanel implements ActionListener
         {
            JLabel designDescriptionLbl = new JLabel("Description: ");
            JTextArea designDescription = new JTextArea();
            JScrollPane descriptionScroll = new JScrollPane(designDescription);
            JButton addLink = new JButton("Link file");
            JFileChooser fileChooser = new JFileChooser();
            File file = null;
            JLabel linkName = new JLabel("No file selected.");
         //      JButton deleteLink = new JButton("X");
            JLabel logoTextDescriptionLbl = new JLabel("<html><center>Logo<br>Summary</center></html>");
            JLabel locationLbl = new JLabel("<html><center>Artwork<br>Location</center></html>");
            JLabel filmTypeLbl = new JLabel("<html><center>Film<br>Type</center></html>");
            JLabel logoColor = new JLabel("<html><center>Film<br>Color</center></html>");
            JLabel verticalInches = new JLabel("<html><center>Vertical<br>Inches</center></html>");
            JButton addLogoEntryBtn = new JButton("+");
            JPanel artworkPnl = new JPanel();
            JScrollPane artwork = new JScrollPane(artworkPnl,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            JLabel timeEstimateLbl = new JLabel("Time estimate: ");
            SpinnerListModel listModel = new SpinnerListModel(new String[]{"<30min","1 hour","2 hours","3 hours","4 hours","5 hours","6 hours"});
            JSpinner timeEstimateSpinner = new JSpinner(listModel);
            JRadioButton drawInHouse = new JRadioButton("Draw in-house",true);
            JRadioButton sendForVectoring = new JRadioButton("Send for vectoring",false);
            ButtonGroup group = new ButtonGroup();
            DefaultMutableTreeNode associatedNode;
            String itemNumber;
            LogoTab self_logoTab = this;
            int textLogoEntries = 0;
            GridBagConstraints artworkPnlConstraints = new GridBagConstraints();
            ArrayList<LogoEntry> entries = new ArrayList<LogoEntry>();
            
            public LogoTab()
            {
               designDescription.setLineWrap(true);
               designDescription.setWrapStyleWord(true);
               group.add(drawInHouse);
               group.add(sendForVectoring);
               ((JSpinner.ListEditor)timeEstimateSpinner.getEditor()).getTextField().setEditable(false);
               artwork.setBorder(new SoftBevelBorder(BevelBorder.LOWERED,new Color(255,255,255,125),new Color(125,125,125,125)));
               
               GridBagLayout cuttingGLay = new GridBagLayout();
               setLayout(cuttingGLay);
               cuttingGLay.rowHeights = new int[]{0,80,0,0,0,75,0};
               cuttingGLay.columnWidths = new int[]{detailsSummaryTabs.getWidth()/6+30,detailsSummaryTabs.getWidth()/6-10,detailsSummaryTabs.getWidth()/6-5,detailsSummaryTabs.getWidth()/6-5,detailsSummaryTabs.getWidth()/6-5,detailsSummaryTabs.getWidth()/6-5};
               GridBagConstraints gbc = new GridBagConstraints();
               
               GridBagLayout glay = new GridBagLayout();
               artworkPnl.setLayout(glay);
               glay.columnWidths = new int[]{0,(detailsSummaryTabs.getWidth())/5,0,0,0,addLogoEntryBtn.getPreferredSize().width+10};
               addEntry();
               
               gbc.weightx = 1;
               gbc.weighty = 1;
               
               gbc.gridx = 0;
               gbc.gridy = 0;
               gbc.insets = new Insets(0,15,0,0);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.LINE_START;
               add(designDescriptionLbl,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 1;
               gbc.insets = new Insets(0,10,0,10);
               gbc.gridwidth = 6;
               gbc.fill = gbc.BOTH;
               gbc.anchor = gbc.CENTER;
               add(descriptionScroll,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 2;
               gbc.insets = new Insets(0,10,0,0);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.LINE_START;
               add(addLink,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 2;
               gbc.insets = new Insets(0,addLink.getPreferredSize().width+15,0,0);
               gbc.gridwidth = 3;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.LINE_START;
               add(linkName,gbc);
               // 
            //                //
            //                JComboBox<String> combo = new JComboBox<String>(new String[]{"Shirt","Pants","Hat"});
            //                combo.setPreferredSize(timeEstimateSpinner.getPreferredSize());
            //                //
            //                
            //                gbc.gridx = 3;
            //                gbc.gridy = 3;
            //                gbc.insets = new Insets(0,0,0,10+combo.getPreferredSize().width+5);
            //                gbc.gridwidth = 3;
            //                gbc.fill = gbc.NONE;
            //                gbc.anchor = gbc.LINE_END;
            //                add(new JLabel("Apparel Type: "),gbc);
            //                
            //                gbc.gridx = 4;
            //                gbc.gridy = 3;
            //                gbc.insets = new Insets(0,0,0,10);
            //                gbc.gridwidth = 2;
            //                gbc.fill = gbc.NONE;
            //                gbc.anchor = gbc.LINE_END;
            //                add(combo,gbc);
               gbc.gridx = 0;
               gbc.gridy = 3;
               gbc.insets = new Insets(0,0,0,0);
               gbc.gridwidth = 6;
               gbc.fill = gbc.HORIZONTAL;
               gbc.anchor = gbc.CENTER;
               add(new JSeparator(),gbc);
               
               gbc.gridx = 3;
               gbc.gridy = 2;
               gbc.insets = new Insets(0,0,0,10+timeEstimateSpinner.getPreferredSize().width+5);
               gbc.gridwidth = 3;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.LINE_END;
               add(timeEstimateLbl,gbc);
               
               gbc.gridx = 4;
               gbc.gridy = 2;
               gbc.insets = new Insets(0,0,0,10);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.LINE_END;
               add(timeEstimateSpinner,gbc);
               
               // gbc.gridx = 0;
            //                gbc.gridy = 4;
            //                gbc.insets = new Insets(10,17,5,0);
            //                gbc.gridwidth = 1;
            //                gbc.fill = gbc.NONE;
            //                gbc.anchor = gbc.SOUTHWEST;
            //                add(logoColor,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 4;
               gbc.insets = new Insets(10,42,5,0);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.SOUTHWEST;
               add(logoTextDescriptionLbl,gbc);
               
               gbc.gridx = 1;
               gbc.gridy = 4;
               gbc.insets = new Insets(10,logoTextDescriptionLbl.getWidth()+42,5,0);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.SOUTHWEST;
               add(locationLbl,gbc);
               
               gbc.gridx = 3;
               gbc.gridy = 4;
               gbc.insets = new Insets(10,17,5,0);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.SOUTHWEST;
               add(filmTypeLbl,gbc);
               
               gbc.gridx = 3;
               gbc.gridy = 4;
               gbc.insets = new Insets(10,filmTypeLbl.getPreferredSize().width+37,5,0);
               gbc.gridwidth = 2;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.SOUTH;
               add(verticalInches,gbc);
               
               gbc.gridx = 5;
               gbc.gridy = 4;
               gbc.insets = new Insets(10,0,5,10);
               gbc.gridwidth = 1;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.SOUTH;
               add(addLogoEntryBtn,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 5;
               gbc.insets = new Insets(0,10,0,10);
               gbc.gridwidth = 6;
               gbc.fill = gbc.BOTH;
               gbc.anchor = gbc.CENTER;
               add(artwork,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 6;
               gbc.insets = new Insets(0,10,0,0);
               gbc.gridwidth = 3;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.CENTER;
               add(drawInHouse,gbc);
               
               gbc.gridx = 3;
               gbc.gridy = 6;
               gbc.insets = new Insets(0,0,0,10);
               gbc.gridwidth = 3;
               gbc.fill = gbc.NONE;
               gbc.anchor = gbc.CENTER;
               add(sendForVectoring,gbc);
               
               addLogoEntryBtn.setToolTipText("Add color or logo.");
               addLogoEntryBtn.addActionListener(this);
               linkName.addMouseListener(
                     new MouseAdapter()
                     {
                        public void mouseEntered(MouseEvent e)
                        {
                           if(file!=null)linkName.setForeground(new Color(100,100,255));
                        }
                     
                        public void mouseExited(MouseEvent e)
                        {
                           if(file!=null)linkName.setForeground(Color.BLUE);
                        }
                     
                        public void mousePressed(MouseEvent e)
                        {
                           if(file!=null)linkName.setForeground(Color.BLUE.darker().darker());
                        }
                     
                        public void mouseReleased(MouseEvent e)
                        {
                           if(file!=null)linkName.setForeground(Color.BLUE);
                        }
                     
                        public void mouseClicked(MouseEvent e)
                        {
                           if(file!=null)
                           {
                              if(e.getButton()==1)
                              {
                                 try{Desktop.getDesktop().open(file);}
                                 catch(IOException ex){ex.printStackTrace();}
                              }
                              else if(e.getButton()==3)
                              {
                                 JPopupMenu popup = new JPopupMenu();
                                 JMenuItem openFile = new JMenuItem("Open");
                                 openFile.setMnemonic('o');
                                 JMenuItem openFileLocation = new JMenuItem("Open file location");
                                 openFileLocation.setMnemonic('l');
                                 popup.add(openFile);
                                 popup.add(openFileLocation);
                                 popup.show(linkName,e.getX(),e.getY());
                                 openFile.addActionListener(
                                       new ActionListener()
                                       {
                                          public void actionPerformed(ActionEvent e)
                                          {
                                             try{Desktop.getDesktop().open(file);}
                                             catch(IOException ex){ex.printStackTrace();}
                                          }
                                       });
                                 openFileLocation.addActionListener(
                                       new ActionListener()
                                       {
                                          public void actionPerformed(ActionEvent e)
                                          {
                                             try{Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());}
                                             catch(IOException ex){ex.printStackTrace();}
                                          }
                                       });
                              }
                           }
                        }
                     });
               addLink.addActionListener(
                     new ActionListener()
                     {
                        public void actionPerformed(ActionEvent e)
                        {
                           if(fileChooser.showOpenDialog(self)==JFileChooser.APPROVE_OPTION)
                           {
                              file = fileChooser.getSelectedFile();
                              linkName.setText(file.getName());
                              linkName.setForeground(Color.BLUE);
                           }
                        }
                     });
               
               artwork.getVerticalScrollBar().setUnitIncrement(28);
               timeEstimateSpinner.setPreferredSize(timeEstimateSpinner.getSize());
            }
            
            public void actionPerformed(ActionEvent e)
            {
               if(e.getSource()==addLogoEntryBtn)
               {
                  addEntry();
               }
            }
            
            public void addEntry()
            {
               entries.add(new LogoEntry());
               SwingUtilities.invokeLater(
                     new Runnable(){
                        public void run(){artwork.getVerticalScrollBar().setValue(artwork.getVerticalScrollBar().getMaximum());}});               
            }
            
            class LogoEntry implements ActionListener
            {
               JTextField logoTextDescriptionFld = new JTextField();
               String[] locOptions = {"Center Chest","Left Chest","Right Chest","Nameplate","Center Back","Shirt Tail","Left Shoulder","Right Shoulder","Left Sleeve","Right Sleeve"};
               JComboBox<String> location = new JComboBox<String>(locOptions);
               String[] filmOptions = {"-Select One-","Thermo Film","Fashion Film","Glitter Flake","Sign Vinyl","Flock","Specialty"};
               String[] filmAbbr = {"--","TF","FF","GF","SV","FL","SP"};
               AbbreviatedComboBox<String> filmType = new AbbreviatedComboBox<String>(filmOptions,filmAbbr);
               SpinnerNumberModel numberModel = new SpinnerNumberModel(1,1,99,1);
               JSpinner verticalInchesSpinner = new JSpinner(numberModel);
               Color[] colors = new Color[]{Color.WHITE,Color.GRAY,Color.BLACK,new Color(255,0,0),new Color(255,158,0),new Color(255,255,0),new Color(158,255,0),new Color(0,255,0),new Color(0,255,158),new Color(0,255,255),new Color(0,158,255),new Color(0,0,255),new Color(158,0,255),new Color(255,0,255),new Color(255,0,158)};
               String[] colorNames = new String[]{"White","Gray","Black","Red","Orange","Yellow","Citrine","Green","Aqua","Cyan","Columbia","Blue","Purple","Magenta","Rose"};
               ColorButton colorBox = new ColorButton(colors,colorNames,Color.WHITE);
               JButton deleteEntry = new JButton("X");
               GridBagConstraints gbc = new GridBagConstraints();
               GridBagLayout glay = new GridBagLayout();
               
               public LogoEntry()
               {
                  artworkPnlConstraints.gridy = textLogoEntries;
                  artworkPnlConstraints.weightx = 1;
                  artworkPnlConstraints.weighty = 1;
                  
                  artworkPnlConstraints.insets = new Insets(0,10,0,0);
                  artworkPnlConstraints.fill = artworkPnlConstraints.HORIZONTAL;
                  artworkPnlConstraints.anchor = artworkPnlConstraints.LINE_START;
                  artworkPnlConstraints.gridx=0;
                  artworkPnl.add(logoTextDescriptionFld,artworkPnlConstraints);
                  
                  artworkPnlConstraints.insets = new Insets(0,0,0,0);
                  artworkPnlConstraints.fill = artworkPnlConstraints.NONE;
                  artworkPnlConstraints.anchor = artworkPnlConstraints.CENTER;
                  artworkPnlConstraints.gridx++;
                  artworkPnl.add(location,artworkPnlConstraints);
                  
                  artworkPnlConstraints.anchor = artworkPnlConstraints.LINE_START;
                  artworkPnlConstraints.gridx++;
                  artworkPnl.add(filmType,artworkPnlConstraints);
                  
                  artworkPnlConstraints.insets = new Insets(0,0,0,5);
                  artworkPnlConstraints.anchor = artworkPnlConstraints.CENTER;
                  artworkPnlConstraints.gridx++;
                  artworkPnl.add(colorBox,artworkPnlConstraints);
                  
                  artworkPnlConstraints.gridx++;
                  artworkPnl.add(verticalInchesSpinner,artworkPnlConstraints);
                  
                  artworkPnlConstraints.gridx++;
                  artworkPnlConstraints.insets = new Insets(0,0,0,20+artwork.getVerticalScrollBar().getPreferredSize().width);
                  artworkPnl.add(deleteEntry,artworkPnlConstraints);
                  
                  deleteEntry.addActionListener(this);
                  logoTextDescriptionFld.requestFocus();
                  textLogoEntries++;
                  logoTextDescriptionFld.setPreferredSize(new Dimension(detailsSummaryTabs.getWidth()/5,logoTextDescriptionFld.getPreferredSize().height));
                  filmType.addItemListener(
                        new ItemListener()
                        {
                           public void itemStateChanged(ItemEvent e)
                           {
                              if(filmType.getSelectedItem().toString().equals("SV")) location.setEnabled(false);
                              else location.setEnabled(true);
                           }
                        });
               }
               
               public void actionPerformed(ActionEvent e)
               {
                  if(e.getSource()==deleteEntry)
                  {
                     artworkPnl.remove(colorBox);
                     artworkPnl.remove(logoTextDescriptionFld);
                     artworkPnl.remove(location);
                     artworkPnl.remove(filmType);
                     artworkPnl.remove(verticalInchesSpinner);
                     artworkPnl.remove(deleteEntry);
                     artworkPnl.repaint();
                     artworkPnl.revalidate();
                     colorBox = null;
                     logoTextDescriptionFld = null;
                     location = null;
                     filmType = null;
                     verticalInchesSpinner = null;
                     deleteEntry = null;
                     entries.remove(this);
                     if(entries.size()>0)entries.get(entries.size()-1).logoTextDescriptionFld.requestFocus();
                     else addLogoEntryBtn.requestFocus();
                  }
               }
            }
         }
         
         class TextTab extends JPanel
         {
            String[] textOptions = new String[]{"1 line of text","2 lines of text","3 lines of text","Chest Name","Script with Tail (1 Color)","Script with Tail (2 Color)"};
            String[] numberOptions = new String[]{"1\"","2\"","3\"","4\"","6\"","8\"","10\""};
            CustomJList<String> textList = new CustomJList<String>(textOptions);
            CustomJList<String> numberList = new CustomJList<String>(numberOptions);
            GridBagLayout ttGlay = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            NimbusCellRenderer cr = new NimbusCellRenderer();
            JPanel pnl = new JPanel(new GridBagLayout());
            JScrollPane textNumOptionsPane = new JScrollPane(pnl);
            
            public TextTab()
            {
               ttGlay.rowHeights = new int[]{0,137};
               
               textList.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED),"Standard Text"));
               numberList.setBorder(new TitledBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED),"Pre-Cut Numbers"));
               textList.setOpaque(false);
               textList.setCellRenderer(cr);
               numberList.setOpaque(false);
               numberList.setCellRenderer(cr);
               textList.setBackground(Color.WHITE);
               numberList.setBackground(Color.WHITE);
               textList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               numberList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               
               setLayout(ttGlay);
               gbc.weightx = 1;
               gbc.weighty = 1;
               
               gbc.gridx = 0;
               gbc.gridy = 0;
               gbc.anchor = gbc.NORTH;
               gbc.fill = gbc.BOTH;
               gbc.gridwidth = 1;
               gbc.gridheight = 1;
               gbc.insets = new Insets(10,10,0,0);
               add(textList,gbc);
               
               gbc.gridx = 1;
               gbc.gridy = 0;
               gbc.anchor = gbc.NORTH;
               gbc.fill = gbc.BOTH;
               gbc.gridwidth = 1;
               gbc.gridheight = 1;
               gbc.insets = new Insets(10,0,0,10);
               add(numberList,gbc);
               
               gbc.gridx = 0;
               gbc.gridy = 1;
               gbc.anchor = gbc.NORTH;
               gbc.fill = gbc.BOTH;
               gbc.gridwidth = 2;
               gbc.gridheight = 1;
               gbc.insets = new Insets(0,10,10,10);
               add(textNumOptionsPane,gbc);
            }
            
            class CustomJList<E> extends JList<E>
            {
               public CustomJList(){super();}
               public CustomJList(E[] listData){super(listData);}
               public CustomJList(ListModel<E> dataModel){super(dataModel);}
               public CustomJList(Vector<? extends E> listData){super(listData);}
               
               @Override
               public void paint(Graphics g)
               {
                  Graphics2D g2 = (Graphics2D)g.create();
                  g2.setColor(getBackground());
                  if(!isOpaque())g2.fillRect(getInsets().left-4,getInsets().top-4,getWidth()-getInsets().left-getInsets().right+8,getHeight()-getInsets().top-getInsets().bottom+8);
                  super.paint(g);
               }
            }
            
            class NimbusCellRenderer extends JLabel implements ListCellRenderer<Object>
            {
               public NimbusCellRenderer()
               {
                  setOpaque(true);
               }

               public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus)
               {
                  setBorder(new EmptyBorder(0,5,0,0));
                  setText(value.toString());
                  Color background;
                  Color foreground;
         
                  if (isSelected&&list.hasFocus())
                  {
                     background = new Color(57,105,138);
                     foreground = Color.WHITE;
                  }
                  else
                  {
                     background = Color.WHITE;
                     foreground = Color.BLACK;
                  }
         
                  if(list.hasFocus()&&isSelected)
                  {
                     setBorder(new CompoundBorder(new CompoundBorder(new LineBorder(new Color(115,164,209),1),new LineBorder(new Color(72,120,155),1)),new EmptyBorder(0,3,0,0)));
                  }
         
                  setBackground(background);
                  setForeground(foreground);
                  return this;
               }
            }
         }
      }
   }
}