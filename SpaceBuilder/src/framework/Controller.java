package framework;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The Controller handles the timing of the game loop. The input gathering,
 * the render, initialize, and update methods are called here.
 * @author Paul Hazelton
 */
public class Controller implements Runnable
{	
	//Framework communication
	private Model model;
	
	//Game loop stuff
	ScheduledExecutorService executor;
	private long initialTime;
	private long currentTime;
	private double timePassed;
	private double accumulatedTime = 0;
	private int frameCount = 0;
	
	//Graphics
	Graphics2D g2 = null;
	//Buffer thing used for performance and eliminate flickering
	BufferStrategy bs = null;
	
	
	//Set the model
	Controller(Model model)
	{
		this.model = model;
		executor = Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * Begin the game loop.
	 * Controller.start() will handle the initialization as well as beginning the game loop.
	 * This is called exactly once at the start of the game
	 */
	protected void start()
	{
		//Initialize the actual game
		this.model.gm.initialize();
		//Dimentions may change during initialization, update them
		this.model.updateDimentions();
		//Request focus so the events work
		this.model.gp.requestFocus();
	
		//Initialize render thing
		this.model.gp.createBufferStrategy(2);
//		this.model.frame.createBufferStrategy(2);
		this.bs = this.model.gp.getBufferStrategy();
//		this.bs = this.model.frame.getBufferStrategy();

		//Start the game loop
		this.initialTime = System.nanoTime();
		executor.scheduleAtFixedRate(this, 0, 1000000000l/this.model.getFPS(), TimeUnit.NANOSECONDS);
	}

	//Game loop
	/**
	 * This is called periodically by the ScheduledExecutorService.
	 * @Warning This should not be called by anything except for the executor.
	 */
	@Override
	public void run()
	{
		//Should the game be closed?
		if (!this.model.getRunning())
		{
			int terminationArg = this.model.gm.terminate(); 
			if (terminationArg != -1)
				System.exit(terminationArg);
			else
				this.model.setRunning(true);
		}
		
		//Measure time passed
		currentTime = System.nanoTime();
		timePassed = (currentTime - initialTime)/1000000000.0d;
		initialTime = currentTime;
		
		//Execute game loop
		getInput();
		update(timePassed);
		render();
		
		//Calculate FPS
		accumulatedTime += timePassed;
		frameCount++;
		
		if (accumulatedTime >= 1)
		{
			this.model.setFPS_actual(frameCount);
			if (model.displayFPS)
				System.out.println(String.format("FPS: " + frameCount));
			//Reset counters
			accumulatedTime--;
			frameCount = 0;
		}
	}
	
	
	//Basic game loop methods
	private void getInput()
	{
		model.gm.handleInput();
	}
	private void update(double timePassed)
	{
		model.gm.update(timePassed);
	}
	private void render()
	{
		//Get graphics from buffer
		this.g2 = (Graphics2D)this.bs.getDrawGraphics();
		
		//Clear the screan
		this.g2.clearRect(0, 0, this.model.getWidth(), this.model.getHeight());
		
		//Pass graphics to artist and debugDraw
		model.ga.setGraphcis(g2);
		model.debugDraw.setGraphics(g2);
		//Begin render process (called at the beginning of every render cycle)
		model.ga.beginDraw();
		model.debugDraw.beginDraw();
		//Tell the game to render
		model.gm.render(this.model.ga);
		//End render process
		model.ga.endDraw();
		model.debugDraw.endDraw();
		
		//Show the buffer after drawing to it
		this.bs.show();
		
		//Release the g2 after using it
		g2.dispose();
	}
}
