package framework;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import org.jbox2d.common.Vec2;

import utility.PMath;

/**
 * The purpose of this class is to facillitate drawing basic graphics, such as drawing rectangles, polygons, and images.
 * @author Paul Hazelton
 */
public class Artist
{
	private Model model;
	
	private Graphics2D graphics;
	private AffineTransform normalTransform;
	private AlphaComposite ac;
	
	private RenderingHints rh;
	public static final RenderingHints rhPolygon
									= new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	public static final RenderingHints rhImage
									= new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	public static final RenderingHints rhText
									= new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	private DrawMode drawMode = DrawMode.NORMAL;
	private Stroke normalStroke;
	private Stroke stroke = new BasicStroke(0.1f);
	private Composite defaultAC;
	
	private final Font defaultFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	private Font font = defaultFont;
	private DecimalFormat df = new DecimalFormat();
	
	private LocalSpace ls;
	
	/**
	 * The draw mode determines what "plane" the graphics are drawn to.
	 * @DrawMode.CENTERED_TRANSFORMED: The coordinate (0, 0) is usually at the center of the screen.
	 * The graphics device is transformed relative to the camera, and transformed again to draw the appropriate object.
	 * @DrawMode.NORMAL: This draws things to the screen based on screen coordinates,
	 * and is NOT transformed relative to the camera.
	 * @author Paul Hazelton
	 */
	public enum DrawMode
	{
		CENTERED_TRANSFORMED, NORMAL
	}
	
	/**
	 * The Artist should only be instantiated once in the main mehtod.
	 * @param model - The game model
	 */
	public Artist(Model model)
	{
		this.model = model;
		this.ls = new LocalSpace();
		
		this.rh = Artist.rhImage;
		
		this.df.setMaximumFractionDigits(2);
	}
	
	/**
	 * This is called once at the beginning of every render sesion. It is called from the game class.
	 */
	void beginDraw()
	{
		this.normalTransform = this.graphics.getTransform();
		this.normalStroke = this.graphics.getStroke();
		this.defaultAC = this.graphics.getComposite();
		
		this.stroke = this.normalStroke;
		this.ac = null;
		
		this.graphics.setRenderingHints(this.rh);
		this.graphics.setFont(this.font);
	}
	/**
	 * This is called once at the end of every render sesion. It is called from the game class.
	 */
	void endDraw()
	{
		this.graphics.setTransform(this.normalTransform);
		this.graphics.setStroke(this.normalStroke);
	}
	
	/**
	 * The draw mode determines what "plane" the graphics are drawn to.
	 * @DrawMode.CENTERED_TRANSFORMED: The coordinate (0, 0) is usually at the center of the screen.
	 * The graphics device is transformed relative to the camera, and transformed again to draw the appropriate object.
	 * @DrawMode.NORMAL: This draws things to the screen based on screen coordinates,
	 * and is NOT transformed relative to the camera.
	 * @param drawMode
	 */
	public void setDrawMode(DrawMode drawMode)
	{ this.drawMode = drawMode; }
	public void setOpacity(float opacity)
	{
		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		this.graphics.setComposite(ac);
	}
	public void resetOpacity()
	{
		this.graphics.setComposite(defaultAC);
	}
	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
		this.graphics.setStroke(stroke);
	}
	public void setStroke(float stroke)
	{
		this.setStroke(new BasicStroke(stroke));
	}
	public void setRenderingHints(RenderingHints rh)	{ this.rh = rh; }
	public void setFont(Font font)
	{
		this.font = font;
		this.graphics.setFont(font);
	}
	public void resetFont()	{ this.font = this.defaultFont; }
	
	//Quick drawing
	public void fillPolygon(Path2D.Float path, float x, float y, float angle, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		
		this.graphics.setColor(color);
		this.graphics.fill(path);
		
		this.returnGraphics();
	}
	public void fillPolygon(Path2D.Float path, Vec2 position, float angle, Color color)
	{
		this.fillPolygon(path, position.x, position.y, angle, color);
	}
	public void fillPolygon(Path2D.Float path, float angle, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.rotate(angle);
		
		this.graphics.setColor(color);
		this.graphics.fill(path);
		
		this.returnGraphics();
	}
	public void fillPolygon(Path2D.Float path, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.setColor(color);
		this.graphics.fill(path);
		
		this.returnGraphics();
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, Color color)
	{
		this.drawLine(new Vec2(x1, y1), new Vec2(x2, y2), color);
	}
	public void drawLine(Vec2 start, Vec2 end, Color color)
	{
		this.prepareGraphics();
		
		Vec2 mid = PMath.midpoint(start, end);
		Vec2 pol = PMath.cartesianToPolar(start, end);
		
		this.graphics.translate(mid.x, mid.y);
		this.graphics.rotate(pol.y);
		this.graphics.scale(pol.x/2f, 1);
		
		this.graphics.setColor(color);
		
		this.graphics.drawLine(-1, 0, 1, 0);
		
		this.returnGraphics();
	}
	
	/**
	 * Fills a rectangle by transforming the graphics.
	 * @param x - the x coordinate of the center of the rectangle
	 * @param y - the y coordinate of the center of the rectangle
	 * @param w - the width of the rectangle
	 * @param h - the height of the rectangle
	 * @param angle - the angle offset. positive is clockwise
	 * @param color - the color.
	 */
	public void fillRec(float x, float y, float w, float h, float angle, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		this.graphics.scale((float)w/2f, (float)h/2f);
		
		this.graphics.setColor(color);
		this.graphics.fillRect(-1, -1, 2, 2);
		
		this.returnGraphics();
	}
	public void fillRec(Vec2 position, Vec2 dimentions, float angle, Color color)
	{
		this.fillRec(position.x, position.y, dimentions.x, dimentions.y, angle, color);
	}
	/**
	 * Draws the outline of a rectangle by transforming the graphics.
	 * @param x - the x coordinate of the center of the rectangle
	 * @param y - the y coordinate of the center of the rectangle
	 * @param w - the width of the rectangle
	 * @param h - the height of the rectangle
	 * @param angle - the angle offset. positive is clockwise
	 * @param color - the color.
	 */
	public void drawRec(float x, float y, float w, float h, float angle, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		this.graphics.scale((float)w/2f, (float)h/2f);
		
		this.graphics.setColor(color);
		this.graphics.setStroke(this.stroke);
		this.graphics.drawRect(-1, -1, 2, 2);
		
		this.returnGraphics();
	}
	public void drawRec(Vec2 pos, Vec2 dim, float angle, Color color)
	{
		this.drawRec(pos.x, pos.y, dim.x, dim.y, angle, color);
	}

	/**
	 * Fills an oval by transforming the graphics.
	 * @param x - the x coordinate of the center of the oval
	 * @param y - the y coordinate of the center of the oval
	 * @param w - the width of the oval
	 * @param h - the height of the oval
	 * @param angle - the angle offset. positive is clockwise
	 * @param color - the color.
	 */
	public void fillOval(float x, float y, float w, float h, float angle, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		this.graphics.scale(w/2f, h/2f);
		
		this.graphics.setColor(color);
		this.graphics.fillOval(-1, -1, 2, 2);
		
		this.returnGraphics();
	}
	public void fillOval(Vec2 pos, Vec2 dim, float angle, Color color)
	{
		this.fillOval(pos.x, pos.y, dim.x, dim.y, angle, color);
	}
	public void fillCircle(float x, float y, float r, Color color)
	{
		this.fillOval(x, y, r*2, r*2, 0, color);
	}
	public void fillCircle(Vec2 pos, float r, Color color)
	{
		this.fillCircle(pos.x, pos.y, r, color);
	}
	/**
	 * Draws the outline of an oval by transforming the graphics.
	 * @param x - the x coordinate of the center of the oval
	 * @param y - the y coordinate of the center of the oval
	 * @param w - the width of the oval
	 * @param h - the height of the oval
	 * @param angle - the angle offset. positive is clockwise
	 * @param color - the color.
	 */
	public void drawOval(float x, float y, float w, float h, float angle, Color color)
	{
		this.prepareGraphics();
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		this.graphics.scale(w/2f, h/2f);
		
		this.graphics.setColor(color);
		this.graphics.setStroke(this.stroke);
		this.graphics.drawOval(-1, -1, 2, 2);
		
		this.returnGraphics();
	}
	public void drawOval(Vec2 pos, Vec2 dim, float angle, Color color)
	{
		this.drawOval(pos.x, pos.y, dim.x, dim.y, angle, color);
	}
	public void drawCircle(float x, float y, float r, Color color)
	{
		this.drawOval(x, y, r*2, r*2, 0, color);
	}
	public void drawCircle(Vec2 pos, float r, Color color)
	{
		this.drawOval(pos.x, pos.y, r*2, r*2, 0, color);
	}
	
	/**
	 * Draws a buffered image with the center at the specified coordinates, and scaled to the specified dimentions
	 * @param image		- the image to be drawn.
	 * @param x			- the x coodinate of the center of the image.
	 * @param y			- the y coodinate of the center of the image.
	 * @param w			- the desired width  of the image.
	 * @param h			- the desired height of the image.
	 * @param angle		- the angle that the image will be rotated by.
	 * @param backColor	- All transparent sections of the image will be filled with this color. "null" will resault in not filling
	 * the transparent sections
	 * @param mirrored	- If "true" the image will be mirrored horizontally
	 */
	public void drawImage(BufferedImage image, float x, float y, float w, float h, float angle, Color backColor, boolean mirrored)
	{
		this.prepareGraphics();
		
		this.graphics.setRenderingHints(this.model.gp.getRHimage());
		
		this.graphics.translate(x, y);
		this.graphics.rotate(angle);
		
		if (mirrored)
			this.graphics.scale((float)-w/2f, (float)h/2f);
		else
			this.graphics.scale((float)w/2f, (float)h/2f);
		
		this.graphics.drawImage(image, -1, -1, 2, 2, backColor, null);
		
		this.returnGraphics();
	}
	public void drawImage(BufferedImage image, float x, float y, float w, float h, float angle, Color backColor)
	{
		this.drawImage(image, x, y, w, h, angle, backColor, false);
	}
	public void drawImage(BufferedImage image, float x, float y, float w, float h, float angle)
	{
		this.drawImage(image, x, y, w, h, angle, null, false);
	}
	public void drawImage(BufferedImage image, Vec2 position, Vec2 dimentions, float angle, Color backColor)
	{
		this.drawImage(image, position.x, position.y, dimentions.x, dimentions.y, angle, backColor, false);
	}
	public void drawImage(BufferedImage image, Vec2 position, Vec2 dimentions, float angle, boolean mirrored)
	{
		this.drawImage(image, position.x, position.y, dimentions.x, dimentions.y, angle, null, mirrored);
	}
	public void drawImage(BufferedImage image, Vec2 position, Vec2 dimentions, float angle)
	{
		this.drawImage(image, position.x, position.y, dimentions.x, dimentions.y, angle, null, false);
	}

	public void drawText(String text, Vec2 position, float angle, float height, Color color)
	{
		this.prepareGraphics();
		
		int heightFont = this.graphics.getFontMetrics().getHeight();
		
//		text += ". HF = " + heightFont;
		
		int width = this.graphics.getFontMetrics().stringWidth(text);
		
		float scale = height/(heightFont*0.6f);
		
		
		this.graphics.translate(position.x, position.y);
		this.graphics.rotate(angle);
		this.graphics.scale(scale, scale);
//		this.graphics.translate(-width/2f, height/2f);
		this.graphics.translate(-width/2f, (heightFont*0.6f)/2f);
		
		this.graphics.setColor(color);
		this.graphics.drawString(text, 0, 0);
		
		this.returnGraphics();
	}
	public void drawText(String text, Vec2 position, float angle, Color color)
	{
		this.prepareGraphics();
		
		int height = this.graphics.getFontMetrics().getHeight();
		
		this.graphics.translate(position.x, position.y);
		this.graphics.rotate(angle);
		this.graphics.translate(0, height/2f);
		
		this.graphics.setColor(color);
		this.graphics.drawString(text, 0, 0);
		
		this.returnGraphics();
	}
	public void drawTextCentered(String text, Vec2 position, float angle, Color color)
	{
		this.prepareGraphics();
		
		int height = (int)(this.graphics.getFontMetrics().getHeight()*0.58f);
		int width = this.graphics.getFontMetrics().stringWidth(text);
		
		this.graphics.translate(position.x, position.y);
		this.graphics.rotate(angle);
		this.graphics.translate(-width/2f, height/2f);
		
		this.graphics.setColor(color);
		this.graphics.drawString(text, 0, 0);
		
//		int h = (int)(this.graphics.getFontMetrics().getHeight()*0.58f);
		
//		this.graphics.drawRect(0, -h, width, h);
//		this.graphics.draw(this.graphics.getFontMetrics().getStringBounds(text, this.graphics));
		
		this.returnGraphics();
	}
	public void drawNumber(float number, Vec2 position, float angle, Color color)
	{
		String text = String.format("%3.2f", number);
		
		this.drawTextCentered(text, position, angle, color);
	}
	
	private void prepareGraphics()
	{
		if (this.drawMode == DrawMode.CENTERED_TRANSFORMED)
		{
			Camera cam = this.model.cam;
			
			this.graphics.translate	( cam.getCenter().x,		 cam.getCenter().y);
			this.graphics.rotate	(-cam.getRotate());
//			this.graphics.shear		( cam.getShear().x,			 cam.getShear().y);
			this.graphics.scale		( cam.getScale().x,			 cam.getScale().y);
			this.graphics.translate	(-cam.getTranslation().x,	-cam.getTranslation().y);
		}
		else if (this.drawMode == DrawMode.NORMAL)
		{
			this.graphics.setTransform(this.normalTransform);
		}
		
		this.graphics.setFont(this.font);
		
		this.transformLocalSpace();
	}
	private void returnGraphics()
	{
		this.graphics.setTransform(this.normalTransform);
		this.graphics.setRenderingHints(this.model.gp.getRHpolygon());
	}
	
	private void transformLocalSpace()
	{
		this.graphics.translate	(ls.position.x, ls.position.y);
		this.graphics.rotate	(ls.rotation);
	}
	
	public void setLocalSpace(Vec2 pos, float angle)
	{
		this.ls.position = pos;
		this.ls.rotation = angle;
	}
	public void resetLocalSpace()
	{
		this.ls = new LocalSpace();
	}
	
	public Graphics2D	getGraphics()
	{
		return this.graphics;
	}
	public void			setGraphcis(Graphics2D g)
	{
		this.graphics = g;
		this.normalTransform = g.getTransform();
	}

	public DrawMode getDrawMode() { return this.drawMode; }
}

class LocalSpace
{
	Vec2 position;
	float rotation;
	
	public LocalSpace()
	{
		position = new Vec2(0, 0);
		rotation = 0;
	}
}