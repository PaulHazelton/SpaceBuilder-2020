package menus;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import fileHandling.Reader;
import framework.Artist;
import framework.InputEvent;
import framework.InputHandler;
import framework.InputEvent.EventCatagory;
import utility.ImageManip;

public class MouseButton extends HUDButton
{
	private static final String root = "buttons/";
	private static BufferedImage lImgOff;
	private static BufferedImage rImgOff;
	private static BufferedImage wImgOff;
	
	private static BufferedImage lImgOn;
	private static BufferedImage rImgOn;
	private static BufferedImage wImgOn;
	
	private BufferedImage butOff;
	private BufferedImage butOn;
	private BufferedImage butImg;
	
	private Vmouse vm;
	
	public static void fetchImages()
	{
		lImgOff = Reader.loadImage(root + "lClick" + ".png");
		rImgOff = Reader.loadImage(root + "rClick" + ".png");
		wImgOff = Reader.loadImage(root + "scroll" + ".png");

		ImageManip.tintTransparency(lImgOff, 0x80000000);
		ImageManip.tintTransparency(rImgOff, 0x80000000);
		ImageManip.tintTransparency(wImgOff, 0x80000000);
		
		lImgOn = ImageManip.copy(lImgOff);
		rImgOn = ImageManip.copy(rImgOff);
		wImgOn = ImageManip.copy(wImgOff);
		
		ImageManip.tintTransparency(lImgOn, 0x80666666);
		ImageManip.tintTransparency(rImgOn, 0x80666666);
		ImageManip.tintTransparency(wImgOn, 0x80666666);
	}
	
	public MouseButton(Vmouse ch, String lable, int index, int x, int y, int w, int h, Command com)
	{
		super(ch, lable, index, x, y, w, h, com);
		
		this.vm = ch;
		
		if (index == InputHandler.LEFT_CLICK_CODE)
		{
			this.butOff = lImgOff;
			this.butOn = lImgOn;
			this.butImg = this.butOff;
		}
		else if (index == InputHandler.RIGHT_CLICK_CODE)
		{
			this.butOff = rImgOff;
			this.butOn = rImgOn;
			this.butImg = this.butOff;
		}
		else if (index == InputHandler.MIDDLE_CLICK_CODE)
		{
			this.butOff = wImgOff;
			this.butOn = wImgOn;
			this.butImg = this.butOff;
		}
	}
	
	@Override
	public void inputEvent(InputEvent item)
	{
		if ((item.catagory != EventCatagory.MOUSE) && (item.catagory != EventCatagory.MOUSE_WHEEL))
			return;
		
		switch(item.type)
		{
		case MOUSE_MOVED:
			break;
		case MOUSE_PRESSED:
			if (item.button == this.index)
				this.press();
			break;
		case MOUSE_RELEASED:
			if (item.button == this.index)
				this.release();
			break;
		case MOUSE_WHEEL_MOVED:
			if (InputHandler.MIDDLE_CLICK_CODE == this.index)
				vm.scroll(item.wheelRotation);
			break;
		default:	break;
		}
	}
	
	@Override
	protected void press()
	{
		this.butImg = this.butOn;
		super.press();
	}
	@Override
	protected void release()
	{
		this.butImg = this.butOff;
		super.release();
	}
	
	void highlight()	{ this.butImg = this.butOn; }
	void unHighlight()
	{
		if (!this.pressed)
			this.butImg = this.butOff;
	}
	
	@Override
	public void render(Artist a)
	{
		Graphics2D g = a.getGraphics();
		
		g.drawImage(this.butImg, box.x, box.y, box.w, box.h, null);
		
		if (com.image != null)
			g.drawImage(com.image, iBox.x, iBox.y, iBox.w, iBox.h, null);
	}
}
