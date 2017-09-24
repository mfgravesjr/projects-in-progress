/*Author: Mark Fredrick Graves, Jr.
	Last Updated: 
	About: 
	
	Table of Contents
	i.)   Imports
	ii.)  Class
		1.) Constants
		2.) Static Variables
		3.) Variables
		4.) Annonymous Inner Class Variables
		5.) Constructors
		6.) Methods
		7.) Main method
		8.) Inner Classes
	iii.) Private Classes
	*/
	
//---------------------------------------------------------------------------------------------------------------------//
//i.IMPORTS
//---------------------------------------------------------------------------------------------------------------------//
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.awt.Point;
import java.io.Serializable;
import javax.swing.ImageIcon;
import java.awt.Image;

//---------------------------------------------------------------------------------------------------------------------//
//ii.CLASS
//---------------------------------------------------------------------------------------------------------------------//
public class RiskMap implements Serializable
{
   //---------------------------------------------------------------------------------------------------------------------//
   //1.CONSTANTS//
   //---------------------------------------------------------------------------------------------------------------------//
   
   //---------------------------------------------------------------------------------------------------------------------//
   //2.STATIC VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
   static BufferedImage mapImage;
   
   static Vector<Territory> territories = new Vector<Territory>();
   
   static Vector<Continent> continents = new Vector<Continent>();
   
   //---------------------------------------------------------------------------------------------------------------------//
   //3.VARIABLES//
   //---------------------------------------------------------------------------------------------------------------------//
      
   //---------------------------------------------------------------------------------------------------------------------//
   //4.ANNONYMOUS INNER CLASS VARIABLES
   //---------------------------------------------------------------------------------------------------------------------//
      
   //---------------------------------------------------------------------------------------------------------------------//
   //5.CONSTRUCTORS//
   //---------------------------------------------------------------------------------------------------------------------//
     
   //---------------------------------------------------------------------------------------------------------------------//
   //6.METHODS//
   //---------------------------------------------------------------------------------------------------------------------//
   public void addTerritory(String name, Point point, ImageIcon icon)
   {
      territories.add(new Territory());
      territories.get(territories.size()-1).setName(name);
      territories.get(territories.size()-1).setPoint(point);
      territories.get(territories.size()-1).setIcon(icon);
   }
   
   public void addContinent()
   {
      continents.add(new Continent());
   }
   //---------------------------------------------------------------------------------------------------------------------//
   //7.MAIN//
   //---------------------------------------------------------------------------------------------------------------------//
   
   //---------------------------------------------------------------------------------------------------------------------//
   //8.INNER CLASSES//
   //---------------------------------------------------------------------------------------------------------------------//
   class Territory
   {
      Point territoryPoint;
      String territoryName;
      ImageIcon imgIcon;
      Vector<Integer> indexOfBorderingTerr = new Vector<Integer>();
   
      public ImageIcon getIcon()
      {
         return imgIcon;
      }
      
      public void setIcon(ImageIcon icon)
      {
         imgIcon = icon;
      }
      
      public void setPoint(Point point)
      {
         territoryPoint = point;
      }
   
      public void setName(String name)
      {
         territoryName = name;
      }
   
      public void addBorder(int border)
      {
         indexOfBorderingTerr.add(new Integer(border));
      }
      
      public void clearBorders()
      {
         indexOfBorderingTerr.clear();
      }
      
      public void removeBorder(int border)
      {
         indexOfBorderingTerr.remove(new Integer(border));
      }
      
      public Point getPoint()
      {
         return territoryPoint;
      }
   
      public String getName()
      {
         return territoryName;
      }
   
      public int[] getBorders() throws Exception
      {
         int[] borders = new int[indexOfBorderingTerr.size()];
         int index = 0;
         for(int i:indexOfBorderingTerr)
         {
            borders[index] = i;
            index++;
         }
         return borders;
      }
   }
   
   class Continent
   {
      String continentName;
      int continentValue;
      Vector<Integer> indexOfContainingTerr = new Vector<Integer>();
   
      public void setName(String name)
      {
         continentName = name;
      }
   
      public void setValue(int value)
      {
         continentValue = value;
      }
   
      public void addTerritory(int index)
      {
         indexOfContainingTerr.add(new Integer(index));
      }
   
      public String getName()
      {
         return continentName;
      }
   
      public int getValue()
      {
         return continentValue;
      }
   
      public int[] getTerritories()
      {
         int[] territoryArr = new int[indexOfContainingTerr.size()];
         int index = 0;
         for(int i: indexOfContainingTerr)
         {
            territoryArr[index] = i;
            index++;
         }
         return territoryArr;
      }
      
      public void removeTerritory(Object o)
      {
         indexOfContainingTerr.remove(o);
      }
   }
}
//---------------------------------------------------------------------------------------------------------------------//
//iii.PRIVATE CLASSES//
//---------------------------------------------------------------------------------------------------------------------//