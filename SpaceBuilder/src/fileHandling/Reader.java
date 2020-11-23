package fileHandling;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public final class Reader
{
	private static BufferedImage noTexture;
	
	//File name directories
	public static String imgroot = "resource/images/";
	public static String txtroot = "resource/text/";
	
//Initialization
	public static void initialize(String imgroot, String txtroot)
	{
		Reader.imgroot = imgroot;
		Reader.txtroot = txtroot;
		
		Reader.noTexture = loadImage("misc/duck.png");
	}
	public static void initialize()
	{
		Reader.noTexture = loadImage("misc/duck.png");
	}
	
	//Load image
	public static BufferedImage loadImage(String fileName)
	{
		File f = new File(imgroot + fileName);
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(f);
		}
		catch (Exception e)
		{
			System.out.println("image loading error. File name: " + imgroot + fileName);
			image = noTexture;
		}
		
		//TODO EXPERIMENTAL!!!!!!!!!!!!!!!!!!!
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
//	    BufferedImage buffy = config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
	    BufferedImage buffy = config.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
//	    buffy = image;
	    Graphics g = buffy.getGraphics();
	    g.drawImage(image, 0, 0, null);
		
	    return buffy;
		
	    //TODO CLASIC!!!!!!
//		return image;
	}
	
	/**
	 * Takes a text file and parses each line into an element of an array of Strings.
	 * the file name is combined with {@link Reader.txtroot}
	 * @param fileName: the file name excluding the directory.
	 * @return An array of each line in the text document
	 */
	public static String[] readTextLines(String fileName)
	{
		//First array list, then array
		ArrayList<String> lineList = new ArrayList<String>();
		String[] lines = null;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(txtroot + fileName));
			String currLine = br.readLine();
			
			while(currLine != null)
			{
				lineList.add(currLine);
				currLine = br.readLine();
			}
			
			lines = new String[lineList.size()];
			lineList.toArray(lines);
			
			br.close();
		}
		catch (IOException e)
		{
			System.out.println("Text file not found, file name: " + txtroot + fileName);
		}
		catch(Exception e)
		{
			System.out.println("Reader error @readTextLines");
			System.out.println(e.toString());
		}
		
		return lines;
	}
}
