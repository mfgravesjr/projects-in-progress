      import javax.swing.border.LineBorder;
      import java.awt.Dimension;
      import java.awt.event.ItemEvent;
      import java.awt.event.ItemListener;
      import javax.swing.JComboBox;
      import java.awt.event.MouseEvent;
      import java.awt.event.MouseListener;
      import java.awt.Component;
      import javax.swing.JButton;
      import javax.swing.plaf.basic.BasicComboPopup;
      
      class AbbreviatedComboBox<E> extends JComboBox<E> implements MouseListener, ItemListener
      {
         AbbreviatedComboBox<E> self = this;
         E[] items = null;
         E[] abbreviations = null;
         JComboBox<E> abstractBox = null;
         BasicComboPopup popup = null;
         JButton comboBoxButton = null;
         int maximumRowCount = 0;
         
         public AbbreviatedComboBox(E[] items, E[] abbreviations)
         {
            super(abbreviations);
            this.items = items;
            this.abbreviations = abbreviations;
            abstractBox = new JComboBox<E>(items);
            new JComboBoxRowCountRetreival();
            popup = new BasicComboPopup(abstractBox);
            for(Component c: getComponents())if(c instanceof JButton)comboBoxButton = (JButton)c;
            for(MouseListener l: getMouseListeners())removeMouseListener(l);
            for(MouseListener l: comboBoxButton.getMouseListeners())comboBoxButton.removeMouseListener(l);
            addMouseListener(this);
            comboBoxButton.addMouseListener(this);
            abstractBox.addItemListener(this);
            popup.setPreferredSize(new Dimension(popup.getPreferredSize().width,popup.getPreferredSize().height/maximumRowCount*items.length+popup.getMargin().top+popup.getMargin().bottom+(((LineBorder)popup.getBorder()).getThickness()*2)));
            popup.addMouseListener(this);
         }
         
         public void itemStateChanged(ItemEvent e)
         {
            for(int i = 0; i < items.length; i++)if(items[i].equals(e.getItem()))setSelectedIndex(i);
            popup.hide();
         }
               
         public void mousePressed(MouseEvent e)
         {
            requestFocusInWindow();
            if(!popup.isVisible())
            {
               popup.show(this,0,getHeight());
            }
            else 
            {
               popup.hide();
            }
         }
         
         public void mouseReleased(MouseEvent e){}
         public void mouseClicked(MouseEvent e){}
         public void mouseEntered(MouseEvent e){}
         public void mouseExited(MouseEvent e){}
         
         class JComboBoxRowCountRetreival extends JComboBox
         {
            public JComboBoxRowCountRetreival()
            {
               super();
               self.maximumRowCount = super.maximumRowCount;
            }
         }
      }