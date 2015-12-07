import javax.swing.JScrollPane;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTree;
import javax.swing.JFrame;
import java.util.Vector;
import java.awt.Container;
import java.awt.Component;

public abstract class GUIUtilities
{
   private static JFrame fr;
   private static DefaultMutableTreeNode root;
   private static JTree tree;
   private static DefaultTreeModel model;
   private static JScrollPane scroll;
   
   public static void viewSubcomponentTree(Container c)
   {
      fr = new JFrame("Subcomponent Tree");
      root = new DefaultMutableTreeNode("Your container");
      tree = new JTree(root);
      model = (DefaultTreeModel)tree.getModel();
      scroll = new JScrollPane(tree);
      
      
      getAllSubcomponentsOf(c);
      fr.add(scroll);
      fr.setDefaultCloseOperation(fr.DISPOSE_ON_CLOSE);
      for(int i = 0;i<tree.getRowCount();i++)tree.expandRow(i);
      fr.pack();
      for(int i = tree.getRowCount()-1;i>=0;i--)tree.collapseRow(i);
      WindowPositioner.setLocation(fr,WindowPositioner.CENTER,true);
      fr.setVisible(true);
   }
   
   public static Component[] getAllSubcomponentsOf(Container c)
   {
      Vector<Component> components = new Vector<Component>();
      for(Component t: c.getComponents())
      {
         DefaultMutableTreeNode newNode = null;
         DefaultMutableTreeNode selectedNode = null;
         
         if(tree!=null)
         {
            selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            if(selectedNode==null)selectedNode = root;
            newNode = new DefaultMutableTreeNode(t.getClass().getSimpleName().equals("")?"Annonymous Component":t.getClass().getSimpleName());
            selectedNode.add(newNode);
         }
         
         
         components.add(t);
         
         if(t instanceof Container)
         {
            if(tree!=null)tree.setSelectionPath(new TreePath(newNode));
            for(Component p: getAllSubcomponentsOf((Container)t)) components.add(p);
            if(tree!=null)tree.setSelectionPath(new TreePath(selectedNode));
         }
      }
      Object[] objArr = components.toArray();
      Component[] compArr = new Component[objArr.length];
      for(int i = 0; i<objArr.length; i++)compArr[i]=(Component)objArr[i];
      return compArr;
   }
}