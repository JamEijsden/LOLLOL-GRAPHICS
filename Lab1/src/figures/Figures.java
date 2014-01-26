package figures;


import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;


import com.jogamp.opengl.util.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;


public class Figures implements GLEventListener  {
	 /*
     * List of Figures that holds pyramid, squares and stars
     */
	ArrayList<Figure> figureList = new ArrayList<Figure>();
	
    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

       
        
        Frame frame = new Frame("M7002E: Lab1");
        frame.setSize(400, 400);
        frame.add(canvas);
        frame.setVisible(true);
        
        canvas.addGLEventListener(new Figures());
        Animator animator = new Animator(canvas);
        
        animator.start();
       
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    
    /*
     * abstract class that is extended by all figures for draw method
     * x position for all figures are random
     */
    abstract class Figure{
    	public abstract void draw(GLAutoDrawable drawable);
    	public double rnd(){
    		double value = Math.random(); 
    		// value = rand.nextGaussian();
    		value = (value*2)-1;
    		return value;
    	}
    }
   
    /*
     * Creates star
     */
    class Star extends Figure{
    	 double size;
    	 double startx;
    	Star(double sz){
    		this.size = sz;
    		this.startx = rnd();
    	}
    	
		@Override
		public void draw(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();
			
			gl.glPushMatrix();
			gl.glBegin(GL2.GL_POLYGON);
			
			gl.glPushAttrib(GL2.GL_CURRENT_BIT);
			gl.glColor3f(0, 1, 0);
			gl.glPopAttrib();
			
			gl.glVertex2d((-0.1+startx)*size, 0.1*size);
			gl.glVertex2d((0.0+startx)*size, 0.4*size);
			gl.glVertex2d((0.1+startx)*size, 0.1*size);
			gl.glVertex2d((0.4+startx)*size, 0.0*size);
			gl.glVertex2d((0.1+startx)*size, -0.1*size);
			gl.glVertex2d((0.0+startx)*size, -0.4*size);
			gl.glVertex2d((-0.1+startx)*size, -0.1*size);
			gl.glVertex2d((-0.4+startx)*size, 0.0*size);

			
			
			gl.glEnd();
			gl.glPopMatrix();
			
		}
    	
    }
    /*
     * Creates square
     */
    class Square extends Figure{
    	double size;
    	double startx;
    	Square(double sz){
    		this.size = sz*-1;
    		startx = rnd();
    		System.out.println("Square " + startx);
    	}
    	
    	public void draw(GLAutoDrawable drawable){
    		GL2 gl = drawable.getGL().getGL2();
    		
			gl.glPushMatrix();
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glPushAttrib(GL2.GL_CURRENT_BIT);
			gl.glColor3f(0, 0, 1);
			gl.glPopAttrib();
			
			gl.glVertex2d(startx, 0.9);
			gl.glVertex2d(startx, 0.8+size);
			gl.glVertex2d((startx+0.1)-size, 0.8+size);
			gl.glVertex2d((startx+0.1)-size, 0.9);
			
			
			
			gl.glEnd();
			gl.glPopMatrix();
    	}
				
    }
    
    /*
     * Creates pyramid
     */
    class Pyramid extends Figure{
    	int size;
    	double x = rnd();
    	double y = -.950;
        double chg= 0.1;
        double tempX=x;
    	
    	Pyramid(int sz){
    		this.size = sz;
    		
    		System.out.println("p= "+x);
    	}
    	
        public void draw(GLAutoDrawable drawable){
            GL2 gl = drawable.getGL().getGL2();
   
            gl.glPushMatrix();
            gl.glBegin(GL.GL_LINE_LOOP);
            
            gl.glPushAttrib(GL2.GL_CURRENT_BIT);
            gl.glColor3f(1f, 1f, 1f);
            gl.glPopAttrib();
            
            int pyr = 2*size;
           
            
            while(pyr > 0){
                    
                if(pyr > size){        //Step up
	                gl.glVertex2d(tempX, y);
	                y=y+chg;
	                gl.glVertex2d(tempX, y);
                }
                else{					//Step down
	                gl.glVertex2d(tempX, y);
	                y=y-chg;
	                gl.glVertex2d(tempX, y);
                }
                
                pyr--;
                if(pyr != 0){        
                gl.glVertex2d(tempX, y);
                tempX=tempX+chg;
                gl.glVertex2d(tempX, y);
                } 
            }
            tempX=x;
            gl.glEnd();
            gl.glPopMatrix();
        }
    }
    
    
	public void render(GLAutoDrawable drawable){
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		for(int i = 0; i < figureList.size(); i++){
			figureList.get(i).draw(drawable);
			
		
		}
		gl.glFlush();
		
	}


/*Add figures Star, square or Pyramid here
 * Takes size as constructor parameter
 * Pyramid(Steps), Star/Square(Scale)
 */
    public void addFigures(){
    	figureList.add(new Pyramid(4));
    	figureList.add(new Star(0.4));
    	figureList.add(new Square(.05));
    	figureList.add(new Square(0.1));
    	figureList.add(new Pyramid(2));
    	figureList.add(new Star(0.3));
    	
    }
	@Override
	public void display(GLAutoDrawable drawable) {
		
		 update();
		 render(drawable);
	}
	
	public void update(){

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		
		
	}
	 /*
     * Init list!
     */
	@Override
	public void init(GLAutoDrawable drawable) {
		
		addFigures();
		drawable.getGL().setSwapInterval(1);
		System.out.println(figureList.size());
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
	
		
	}
	
}