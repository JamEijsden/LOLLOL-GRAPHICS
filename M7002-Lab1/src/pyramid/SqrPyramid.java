package pyramid;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.util.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

public class SqrPyramid implements GLEventListener  {
    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(400, 400);
        frame.add(canvas);
        frame.setVisible(true);
        
        canvas.addGLEventListener(new SqrPyramid());
        Animator animator = new Animator(canvas);
        
        animator.start();
        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    private double theta = 0;
    private double s = 0;
    private double c = 0;
    private double b = 0;
    
	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		 update();
		 pyramid(drawable,6);
	}
	
	public void update(){
		//Nothing to update atm
		 theta += 0.01; s = Math.sin(theta); c = Math.cos(theta); b = Math.tan(theta);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		 drawable.getGL().setSwapInterval(1);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		
	}
	
	public void pyramid(GLAutoDrawable drawable, int size){
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glBegin(GL.GL_LINES);
		double x = -1.0;
		double y = -1.0;
		double chg= 0.1;
		int pyr = 2*size;
		while(pyr > 0){
			
			if(pyr > size){	
				gl.glVertex2d(x, y);
				y=y+chg;
			    gl.glVertex2d(x, y);
			}
			else{
				gl.glVertex2d(x, y);
				y=y-chg;
			    gl.glVertex2d(x, y);
			}
			
			pyr--;
				if(pyr != 0){	
				gl.glVertex2d(x, y);
				x=x+chg;
			    gl.glVertex2d(x, y);
			}
			
		}
		gl.glVertex2d(-1, -1);
	    gl.glVertex2d(x, y);	
		gl.glEnd();
	}

}
