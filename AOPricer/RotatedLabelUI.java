import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JLabel;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class RotatedLabelUI extends BasicLabelUI {
 
    static {
        labelUI = new RotatedLabelUI(false);
    }
 
    protected boolean clockwise;
 
    public RotatedLabelUI(boolean clockwise) {
        super();
        this.clockwise = clockwise;
    }
 
    @Override
    public Dimension getPreferredSize(JComponent c) {
    	Dimension dim = super.getPreferredSize(c);
    	return new Dimension( dim.height-5, dim.width + 10);
    }
 
    private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);
 
    @Override
    public void paint(Graphics g, JComponent c) {
    
    	Graphics2D g2 = (Graphics2D)g.create();
      RenderingHints rendHint = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      rendHint.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHints(rendHint);
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
 
        if ((icon == null) && (text == null)) {
            return;
        }
 
        FontMetrics fm = g2.getFontMetrics();
        paintViewInsets = c.getInsets(paintViewInsets);
 
        paintViewR.x = paintViewInsets.left;
        paintViewR.y = paintViewInsets.top;
 
    	// Use inverted height &amp; width
        paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
 
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
 
        String clippedText = layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);
 
    	AffineTransform tr = g2.getTransform();
    	if (clockwise) {
            g2.rotate( Math.PI / 2 );
            g2.translate( 0, - c.getWidth() );
    	} else {
            g2.rotate( - Math.PI / 2 );
            g2.translate( - c.getHeight(), 0 );
    	}
 
    	if (icon != null) {
            icon.paintIcon(c, g2, paintIconR.x, paintIconR.y);
        }
 
        if (text != null) {
            int textX = paintTextR.x+5;
            int textY = paintTextR.y + fm.getAscent();
 
            if (label.isEnabled()) {
                paintEnabledText(label, g2, clippedText, textX, textY);
            } else {
                paintDisabledText(label, g2, clippedText, textX, textY);
            }
        }
	g2.setTransform( tr );
    }
 
}