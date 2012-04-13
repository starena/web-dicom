package dicomcl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DCMImagePanel extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	BufferedImage image = null;
	
	public DCMImagePanel (BufferedImage image) 
	{
		this.image = image;
		this.setPreferredSize (new Dimension(image.getWidth(),image.getHeight()));
		//this.setPreferredSize(new Dimension(2000,3000));
	}
	
	public void paint (Graphics g)
	{
		g.clearRect(0,0,this.getWidth(),this.getHeight());
		g.setColor(Color.BLACK);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		if (image != null) g.drawImage(image,0,0,this);
	}
}
