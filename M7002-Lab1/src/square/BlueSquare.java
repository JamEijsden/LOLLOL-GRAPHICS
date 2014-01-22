package square;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.util.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;

public class BlueSquare implements GLEventListener  {
    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(400, 400);
        frame.add(canvas);
        frame.setVisible(true);
        
        canvas.addGLEventListener(new BlueSquare());
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
		 blueSquare(drawable,0.2, 15);
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
	
	

		
		public void blueSquare(GLAutoDrawable drawable, double size,int num){
			GL2 gl = drawable.getGL().getGL2();
			
			double startx = -1.0 + size/2;
			double starty = 1.0 - size/2;
			
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			while(num != 0){		
				gl.glRectd(startx,starty-size,startx+size,starty);
				
			
				startx += 2*size;
				if(startx >= 1){
					starty -= 2*size;
					startx = -1.0 + size/2;
				}
				
				num--;
			}
			gl.glEnd();
		}
		
	}
	

