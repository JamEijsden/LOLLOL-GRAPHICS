package lab2;


import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;



import java.util.Random;
import java.util.Vector;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.glu.GLU;

import lab2.Picking.Renderer.GLEntity;


public class Shape implements GLEventListener, MouseListener,  KeyListener  {

	ArrayList<Shapes> shapeList = new ArrayList<Shapes>();
	GLU glu = new GLU();
	private float angleCube = 0;  
	private float speedCube= -1.5f;
	final int SELECT = 1, UPDATE = 2;
	final int NOTHING = 0, SPHERE = 1, CUBE = 2, PYRAMID = 3;
	int cmd = 1; 
	int selectedObj;
	double mouse_x, mouse_y;
	double posX, posY, posZ;  
	
    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        Shape s = new Shape();
       
        
        Frame frame = new Frame("M7002E: Lab2");
        frame.setSize(400, 400);
        
        frame.add(canvas);
        frame.setVisible(true);
        canvas.addMouseListener(s);
        canvas.addKeyListener(s);
        
        
        canvas.addGLEventListener(s);
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
    abstract class Shapes{
    	int id;
    	GLU glu;
    	GL2 gl;
    	float[] c= {1.0f,0,0};
        float x, y, z;
        float w = 0.1f;
        float h = 0.1f;
    	Shapes(GLU glu, GL2 gl){
    		this.gl = gl;
    		this.glu = glu;
    	}
    	public abstract void draw();
    	public int rnd(){
    		double value = Math.random(); 
    		// value = rand.nextGaussian();
    		value = value*100000.0;
    		return (int)value;
    	}
		public void resize() {
			// TODO Auto-generated method stub
			
		}
		public void pushName(){
    		gl.glPushName(id);
    		draw();
    	}
    }
    
    class Cube extends Shapes {
    	float placement = 0.5f;
    	float size;
    	int id;
    	
    	Cube(float plc, float sz, GL2 gl, GLU glu) {
    		super(glu, gl);
    		placement = plc;
    		size = sz;
    		id = rnd();
    	}
    	
    	public void resize(){
    		size += 0.1;
    	}
    	
    	

		@Override
		public void draw() {
			// TODO Auto-generated method stub
			//GL2 gl = drawable.getGL().getGL2();
			//gl.glPushName(id);
			gl.glPushMatrix();
			gl.glLoadIdentity();                // reset the current model-view matrix
		      gl.glTranslatef(1.6f, 0.0f, -7.0f); // translate right and into the screen
		      gl.glRotatef(angleCube, 1.0f, 1.0f, 1.0f); // rotate about the x, y and z-axes
		      
		      gl.glBegin(GL2.GL_QUADS); // of the color cube
		      gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		      float[] ambientClr = {0f, 0f, 0f, 0f};
			float[] lightClr = {1.0f, 1.0f, 1.0f};
			float[] diffuse = {0.3f, 0.6f, 1f, 0f};
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 60f);
		      // Top-face
		     
		      
		      //gl.glColor3f(0.0f+placement, 0f, 1.0f); // green
		      gl.glVertex3f(size+placement, size, -size);
		      gl.glVertex3f(-size+placement, size, -size);
		      gl.glVertex3f(-size+placement, size, size);
		      gl.glVertex3f(size+placement, size, size);
		   
		      // Bottom-face
		    
//		      gl.glColor3f(0f+placement, 0f, 1.0f); // orange
		      gl.glVertex3f(size+placement, -size, size);
		      gl.glVertex3f(-size+placement, -size, size);
		      gl.glVertex3f(-size+placement, -size, -size);
		      gl.glVertex3f(size+placement, -size, -size);
		 
		      // Front-face
		    
//		      gl.glColor3f(0f+placement, 0.0f, 1.0f); // red
		      gl.glVertex3f(size+placement, size, size);
		      gl.glVertex3f(-size+placement, size, size);
		      gl.glVertex3f(-size+placement, -size, size);
		      gl.glVertex3f(size+placement, -size, size);
		   
		      // Back-face
		  
//		      gl.glColor3f(0f+placement, 0f, 1.0f); // yellow
		      gl.glVertex3f(size+placement, -size, -size);
		      gl.glVertex3f(-size+placement, -size, -size);
		      gl.glVertex3f(-size+placement, size, -size);
		      gl.glVertex3f(size+placement, size, -size);
		     
		      // Left-face
		     
//		      gl.glColor3f(0.0f+placement, 0.0f, 1.0f); // blue
		      gl.glVertex3f(-size+placement, size, size);
		      gl.glVertex3f(-size+placement, size, -size);
		      gl.glVertex3f(-size+placement, -size, -size);
		      gl.glVertex3f(-size+placement, -size, size);
		
		      // Right-face
//		      gl.glColor3f(0f+placement, 0.0f, 1.0f); // magenta
		      gl.glVertex3f(size+placement, size, -size);
		      gl.glVertex3f(size+placement, size, size);
		      gl.glVertex3f(size+placement, -size, size);
		      gl.glVertex3f(size+placement, -size, -size);
		     
		      gl.glEnd(); // of the color cube
		      gl.glPopAttrib();
		      gl.glPopMatrix();
		      angleCube += speedCube;

		}
		
    }
    
    
    class Sphere extends Shapes{
    	float size;
    	float[] ambientClr = {0f, 0f, 0f, 1f};
		float[] lightClr = {1.0f, 1.0f, 1f ,1f};
		float[] diffuse = {0f, 1f, 0f, 1f};
		float shiny = 60f;
		double x=0.5,y=0.5;
		
		GLUT glut = new GLUT();
		int id;
		
		Sphere(GL2 gl, GLU glu,double x,double y){
			super(glu, gl);
//			id = rnd();
			this.x = x;
			this.y = y;
			
		}

		@Override
		public void draw() {
			gl.glPushName(id);
			// TODO Auto-generated method stub
			//GL2 gl = drawable.getGL().getGL2();
			gl.glTranslatef((float)x, (float)y, 0);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0f);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);
		
			glut.glutSolidSphere(8.0f,20,20);
			gl.glPopAttrib();
			gl.glPopMatrix();
			
		}
	}
    
    class ligthSource{
    	void lightSoruce(){
    		
    	}
    }

    void addShapes(GL2 gl, GLU glu, double x, double y){
    	switch(selectedObj){
    	case SPHERE:
    		shapeList.add(new Sphere(gl, glu,mouse_x, mouse_y));
    		break;
    	case CUBE:
    		shapeList.add(new teaPot(gl, glu,mouse_x, mouse_y));
    	}
		
	}
    
    double[] getOBJPos(int x, int y, GL2 gl)
    {
    	int viewport[] = new int[4];
        double mvmatrix[] = new double[16];
        double projmatrix[] = new double[16];
        int realy = 0;// GL y coord pos
        double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords
        
     
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
     
        realy = viewport[3] - (int) y - 1;
   
        glu.gluUnProject((double) x, (double) realy, 0.0, //
            mvmatrix, 0,
            projmatrix, 0, 
            viewport, 0, 
            wcoord, 0);
        
     
        return wcoord;
    }
    	
    class teaPot extends Shapes{
    	GLUT glut = new GLUT();
    	teaPot(GL2 gl, GLU glu,double x,double y){
    		super(glu, gl);
    	}
    	
	    void draw(GL2 gl){
	    	gl.glPushName(1337);
	    	gl.glPushMatrix();
	    	gl.glTranslatef((float)x, (float)y, 0);
	    	glut.glutSolidTeapot(0.5);
	    	gl.glPopMatrix();
	    	
	    }

		@Override
		public void draw() {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
	public void render(GL2 gl, GLUT glut){
		gl.glPushName(1337);
    	gl.glPushMatrix();
    	gl.glTranslatef(0.5f, (float)0.5, 0);
    	glut.glutSolidTeapot(0.5);
    	gl.glPopMatrix();
		
	}

/*Add shapes Star, square or Pyramid here
 * Takes size as constructor parameter
 * Pyramid(Steps), Star/Square(Scale)
 */
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); 
		
	 
		
		 // clear color and depth buffers
		// ----- Render the Color Cube -----

		
		    switch(cmd)
		      {
		      case UPDATE:
		    	  for(int i=0;i<shapeList.size();i++){
		    		
		  			shapeList.get(i).draw();
		  			}
		    	  //render(gl, glut);
		    	
		        break;
		      case SELECT:
		        int buffsize = 512;
		        int[] viewPort = new int[4];
		        double[] res = getOBJPos((int)mouse_x, (int)mouse_y, gl);
		        double x = res[0], y = res[1];
		        IntBuffer selectBuffer = Buffers.newDirectIntBuffer(buffsize);
		        int hits = 0;
		        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPort, 0);
		        gl.glSelectBuffer(buffsize, selectBuffer);
		        gl.glRenderMode(GL2.GL_SELECT);
		        gl.glInitNames();
		        gl.glMatrixMode(GL2.GL_PROJECTION);
		        gl.glPushMatrix();
		        gl.glLoadIdentity();
		        glu.gluPickMatrix(x, (double) viewPort[3] - y, 5.0d, 5.0d, viewPort, 0);
		        glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
		        System.out.println("before: "+x+", "+y);
		        addShapes(gl, glu, x, y);
		        for(int i=0;i<shapeList.size();i++){
		  			shapeList.get(i).draw();
		  			System.out.println(shapeList.size());
		  			System.out.println(shapeList.get(i).x+ ", "+ shapeList.get(i).y);
		  		}
		        render(gl, glut);
		        gl.glMatrixMode(GL2.GL_PROJECTION);
		        gl.glPopMatrix();
		        gl.glFlush();
		        hits = gl.glRenderMode(GL2.GL_RENDER);
		        //processHits(hits, selectBuffer);
		        System.out.println(mouse_x+" = " + x +", " +mouse_y+ " = "+ y);
		        cmd = UPDATE;
		       
		        break;
		      }
		
	}
	
	public void processHits(int hits, IntBuffer buffer)
	  {
	    System.out.println("---------------------------------");
	    System.out.println(" HITS: " + hits);
	    int offset = 0;
	    int names;
	    float z1, z2;
	    for (int i=0;i<hits;i++)
	      {
	        System.out.println("- - - - - - - - - - - -");
	        System.out.println(" hit: " + (i + 1));
	        names = buffer.get(offset); offset++;
	        z1 = (float) (buffer.get(offset)& 0xffffffffL) / 0x7fffffff; offset++;
	        z2 = (float) (buffer.get(offset)& 0xffffffffL) / 0x7fffffff; offset++;
	        System.out.println(" number of names: " + names);
	        System.out.println(" z1: " + z1);
	        System.out.println(" z2: " + z2);
	        System.out.println(" names: ");

	        for (int j=0;j<names;j++)
	          {
	            System.out.print("       " + buffer.get(offset)); 
	            if (j==(names-1))
	              System.out.println("<-");
	            else
	              System.out.println();
	            offset++;
	          }
	        System.out.println("- - - - - - - - - - - -");
	      }
	    System.out.println("---------------------------------");
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
		GL2 gl = drawable.getGL().getGL2();
		gl.getGL().setSwapInterval(1);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
	    gl.glClearDepth(1.0f);      // set clear depth value to farthest
	    gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
	    gl.glDepthFunc(GL2.GL_DEPTH_TEST);
	   // gl.glDepthFunc(GL2.GL_LEQUAL);  // the type of depth test to do
	    gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
	    gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	    
	    float[] lightPos = { 10, 5,10,0 };        // light position
	    float[] noAmbient = { 0.2f, 0.2f, 0.2f, 1f };     // low ambient light
	    float[] diffuse = { 0.25f, .25f, 0.25f, 0.25f };  // full diffuse colour
	    float[] spec = { 1.0f, 1.0f, 1.0f, 1.0f };
	    
	    gl.glEnable(GL2.GL_LIGHTING);
	    gl.glEnable(GL2.GL_LIGHT0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, noAmbient, 0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spec, 0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,lightPos, 0);
	    
	    //addShapes(gl, glu);
		
	    
	
		
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
//		GL2 gl = drawable.getGL().getGL2();
//		GLU glu = new GLU();
//		
//	      if (height == 0) height = 1;   // prevent divide by zero
//	      float aspect = (float)width / height;
//	 
//	      // Set the view port (display area) to cover the entire window
//	      gl.glViewport(0, 0, width, height);
//	 
//	      // Setup perspective projection, with aspect ratio matches viewport
//	      gl.glMatrixMode(GL2.GL_PROJECTION);  // choose projection matrix
//	      gl.glLoadIdentity();             // reset projection matrix
//	      glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
//	 
//	      // Enable the model-view transform
//	      gl.glMatrixMode(GL2.GL_MODELVIEW);
//	      gl.glLoadIdentity(); // reset
//	      
			GL2 gl = drawable.getGL().getGL2();
		    gl.glViewport(0, 0, width, height);
		    gl.glMatrixMode(GL2.GL_PROJECTION);
		    gl.glLoadIdentity();
		    glu.gluOrtho2D(0.0f,1.0f,0.0f,1.0f);
	}



	public void keyPressed(KeyEvent key)
	  {
	    
	  }


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}






	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		cmd = SELECT;
	    mouse_x = (double) e.getX();
	    mouse_y = (double) e.getY();
	   

	    
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()){
		case KeyEvent.VK_1:
			System.out.print("1");
			selectedObj = SPHERE;
			break;
		case KeyEvent.VK_2:
			System.out.print("2");
			selectedObj = CUBE;
			break;
		case KeyEvent.VK_3:
			System.out.print("3");
			selectedObj = PYRAMID;
			break;
		}
		
		
	}
	
}