package utility;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageManip
{
	public static void tintTransparency(BufferedImage img, int c)
	{
//		int c2;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				
				int i = img.getRGB(x, y);

				if (i != 0x00000000)
				{
//					c2 = (i & 0xff000000) | (c & 0x00ffffff);
					img.setRGB(x, y, c);
				}
			}
		}
	}
	
	public static void tint(BufferedImage img, int c)
	{
		int c2;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				
				int i = img.getRGB(x, y);

				if (i != 0x00000000)
				{
					c2 = (i & 0xff000000) | (c & 0x00ffffff);
					img.setRGB(x, y, c2);
				}
			}
		}
	}
	
	public static void tintWhite(BufferedImage img, int c)
	{
		int c2;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {

				int i = img.getRGB(x, y);

				// If color is white or grey
				if (isGray(i))
				{
					c2 = (i & 0xff000000) | (c & 0x00ffffff);
					img.setRGB(x, y, c2);
				}
			}
		}
	}
	
	public static boolean isGray(int c)
	{
		int r = c | 0x00ff0000;
		int g = c | 0x0000ff00;
		int b = c | 0x000000ff;
		
		return (r == g && r == b);
	}

	public static BufferedImage copy(BufferedImage i)
	{
		 ColorModel cm = i.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = i.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
