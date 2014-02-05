package lab2;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.IntBuffer;
import java.util.ArrayList;













import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class Shape implements GLEventListener, MouseListener,  KeyListener, MouseMotionListener  {

	ArrayList<Shapes> shapeList = new ArrayList<Shapes>();
	
	GLU glu = new GLU();
	static GLCanvas canvas;

	final int NULL = -1, SELECT = 1, UPDATE = 2, MOVE = 3, RESIZE = 4, DELETE = 5, LIGHT = 6;
	final int NOTHING = 0, SPHERE = 1, CUBE = 2, TORUS = 3, TEAPOT = 4;
	int chgMode = -1;
	int cmd = 1; 
	int selShapeID;
	int selectedObj;
	double mouse_x, mouse_y;
	double posX, posY, posZ;  
	
    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);
        Shape s = new Shape();
        
       
        Frame frame = new Frame("M7002E: Lab2");
        frame.setSize(400, 400);
        
        frame.add(canvas);
        frame.setVisible(true);
        canvas.addMouseListener(s);
        canvas.addKeyListener(s);
        canvas.addMouseMotionListener(s);
        
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
    	double x,y;
    	float[] ambientClr;
		float[] lightClr;
		float[] diffuse;
		float shiny;
    	float size;
    	
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
		
		void changeLightning(float[] ambient, float[] light, float[] diff, float shine){
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diff, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, light, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shine);
	        gl.glPopAttrib();
		}
		
    }
    
    class Cube extends Shapes {
    	
    	
    	GLUT glut = new GLUT();
    	
    	
    	Cube(double x, double y, float sz, GL2 gl, GLU glu) {
    		super(glu, gl);
    		this.x = x;
    		this.y= y;
    		size = 0.3f;
    		id = rnd();
    	}
    	
    	
    	

		@Override
		public void draw() {
			// TODO Auto-generated method stub
			//GL2 gl = drawable.getGL().getGL2();
			gl.glPushName(id);
			gl.glPushMatrix();
			gl.glRotatef(30.0f, -1.0f, -1.0f, 1.0f);
		      gl.glTranslatef((float)x, (float)y, 0f); // translate right and into the screen
		      
		      // of the color cube
		      gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		      float[] ambientClr = {0.5f, 0.5f, 0f, 1f};
			float[] lightClr = {1.0f, 1.0f, 1.0f, 1f};
			float[] diffuse = {0f, 0f, 1f, 1f};
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 2f);
		      // Top-face
		     
		     
		
			glut.glutSolidCube(size);
	       
			gl.glPopAttrib();
			gl.glPopMatrix();

		}
		
    }
    
    
    class Sphere extends Shapes{
    	
    	float[] ambientClr = {0f, 0f, 0f, 1f};
		float[] lightClr = {1.0f, 1.0f, 1f ,1f};
		float[] diffuse = {0f, 1f, 0f, 1f};
		float shiny = 2f;
	
		
		GLUT glut = new GLUT();
		
		
		Sphere(GL2 gl, GLU glu,double x,double y){
			super(glu, gl);
			this.id = rnd();
			this.x = x;
			this.y = y;
			System.out.println("Sphere: "+ this.x + ", " + this.y );
			size = 0.2f;
			
		}

		@Override
		public void draw() {
			gl.glPushName(id);
			// TODO Auto-generated method stub
			//GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0f);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);
		
			glut.glutSolidSphere(size,20,20);
	       
			gl.glPopAttrib();
			gl.glPopMatrix();
			
		}
	}
    



    

    	
    class teaPot extends Shapes{
    	GLUT glut = new GLUT();
    	
    	float[] ambientClr = {0f, 0f, 0f, 1f};
		float[] lightClr = {1.0f, 1.0f, 1f ,1f};
		float[] diffuse = {1f, 1f, 0f, 1f};
		float shiny = 60f;
    
    	teaPot(GL2 gl, GLU glu,double x,double y){
    		super(glu, gl);
    		this.x = x;
    		this.y = y;
    		this.id = rnd();
    		size = 0.2f;
    	}
    	
	    public void draw(){
	    	gl.glPushName(id);
			// TODO Auto-generated method stub
			//GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0f);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);
		
		
	        glut.glutSolidTeapot((double)size);
			gl.glPopAttrib();
			gl.glPopMatrix();
			
	    	
	    }
    }

    class Torus extends Shapes{
    	GLUT glut = new GLUT();
    	
    	float[] ambientClr = {0f, 0f, 0f, 1f};
		float[] lightClr = {1.0f, 1.0f, 1f ,1f};
		float[] diffuse = {1f, 0f, 0f, 1f};
		float shiny = 60f;
    	
    	Torus(GL2 gl, GLU glu,double x,double y){
    		super(glu, gl);
    		this.x = x;
    		this.y = y;
    		this.id = rnd();
    		size = 0.1f;
    	}
    	
	    public void draw(){
	    	gl.glPushName(id);
			// TODO Auto-generated method stub
			//GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0f);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
	        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
	        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);
		
		
	        glut.glutSolidTorus(size, size+(size/2), 10, 50);
			gl.glPopAttrib();
			gl.glPopMatrix();
			
	    	
	    }
    }
    
    void addShapes(GL2 gl, GLU glu, double x, double y){
    	System.out.println("addShapes: "+ x + ", " + y );
    	
    	switch(selectedObj){
    	case SPHERE:
    		shapeList.add(new Sphere(gl, glu,x,y));
    		break;
    	case CUBE:
    		shapeList.add(new Cube(x, y, 0.3f, gl, glu));
    		break;
    		
    	case TORUS:
    		shapeList.add(new Torus(gl, glu,x,y));
    		break;
    		
    	case TEAPOT:
    		shapeList.add(new teaPot(gl, glu,x, y));
    		break;
    	}
    	selectedObj = NOTHING;
    	
		
	}
    
	public void render(GL2 gl, GLUT glut){
		
		 
		
	}

/*Add shapes Star, square or Torus here
 * Takes size as constructor parameter
 * Torus(Steps), Star/Square(Scale)
 */
	
	@Override
	public void display(GLAutoDrawable drawable) {
		
		
		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); 
		double[] res = getOBJPos((int)mouse_x, (int)mouse_y, gl);
        double x_s = res[0], y_s = res[1];
	 
		
		 // clear color and depth buffers
		// ----- Render the Color Cube -----

		
		    switch(cmd)
		      {
		      case UPDATE:
		    	  for(int i=0;i<shapeList.size();i++){
		    		
		  			shapeList.get(i).draw();
		  			}
		    	  render(gl, glut);
		    	
		        break;
		      case SELECT:
		        int buffsize = 512;
		        int[] viewPort = new int[4];
		        //double[] res = getOBJPos((int)mouse_x, (int)mouse_y, gl);
		        //double x = res[0], y = res[1];
		        double x = mouse_x;
		        double y = mouse_y;
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
		       
		        glu.gluOrtho2D(10.0f,10.0f,10f,10.0f);
		        //glu.gluPerspective(60.0f, (float)canvas.getWidth()/(float)canvas.getHeight(),0.1f, 100);
		        System.out.println("before: "+x+", "+y);
		        
		        addShapes(gl, glu, x_s, y_s);
		        for(int i=0;i<shapeList.size();i++){
		  			shapeList.get(i).draw();
		  		}
		        render(gl, glut);
		        gl.glMatrixMode(GL2.GL_PROJECTION);
		        gl.glPopMatrix();
		        gl.glFlush();
		        hits = gl.glRenderMode(GL2.GL_RENDER);
		        processHits(hits, selectBuffer);
		        
		        //System.out.println(mouse_x+" = " + x +", " +mouse_y+ " = "+ y);
		        cmd = UPDATE;
		       
		        break;
		      case MOVE:
		    	  moveShape(x_s, y_s);
		    	  for(int i=0;i<shapeList.size();i++){
			  			shapeList.get(i).draw();
			  		}
		    	  cmd = UPDATE;
		    	  break;
		    	 
		    	 
		      case RESIZE:
		    	  chgSize(chgMode);
		    	  for(int i=0;i<shapeList.size();i++){
			  			shapeList.get(i).draw();
			  		}
		    	  cmd = UPDATE;
		    	  break;
		    	  
		      case DELETE:
		    	  deleteObj();
		    	  cmd = UPDATE;
		    	  break;
		    	  
		      case LIGHT:
		    	  customObj();
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
	            if (j==(names-1)){
	              System.out.println("<-");
	              selShapeID =buffer.get(offset);
	              System.out.println("Selected Shape: " + selShapeID);
	            }
	            else
	              System.out.println();
	            offset++;
	          }
	        System.out.println("- - - - - - - - - - - -");
	      }
	    if(hits == 0){
	    	selShapeID = NULL;
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
		//glu.gluPerspective(60.0f, (float)canvas.getWidth()/(float)canvas.getHeight(),0.1f, 100);
		glu.gluOrtho2D(10.0f,10.0f,10f,10.0f);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
	    gl.glClearDepth(1.0f);      // set clear depth value to farthest
	    gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
	    gl.glDepthFunc(GL2.GL_DEPTH_TEST);
	   // gl.glDepthFunc(GL2.GL_LEQUAL);  // the type of depth test to do
	    gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best perspective correction
	    gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	    
	    float[] lightPos = { 100, 50,-200,0 };        // light position
	    float[] noAmbient = { 0.2f, 0.2f, 0.2f, 1f };     // low ambient light
	    float[] diffuse = { 0.25f, .25f, 0.25f, 0.25f };  // full diffuse color
	    float[] spec = { 1.0f, 1.0f, 1.0f, 1.0f };
	    
	    shapeList.add(new Sphere(gl, glu, 0.0, 0.0));
	    
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
		    // *((float)canvas.getWidth()/(float)canvas.getHeight())
		   glu.gluOrtho2D(10.0f,10.0f,10f,10.0f);
		   // glu.gluPerspective(60.0f, (float)canvas.getWidth()/(float)canvas.getHeight(),0.1f, 100);
	}

    class ligthSource{
    	void lightSoruce(){
    		
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
        
//        	for(int i =0; i< wcoord.length;i++){
//        		System.out.println(wcoord[i] + ", ");
//        	}
        return wcoord;
    }

    void moveShape(double t_x, double t_y){
//    	GL2 gl = drawable.getGL().getGL2();
//    	getOBJPos(x ,y, );
    	if(selShapeID != NULL){
	    	for(int i = 0; i < shapeList.size(); i++){
	    		
		    	if(selShapeID == shapeList.get(i).id){
		    		System.out.println("Selected Shape: " + shapeList.get(i).id);
		    		shapeList.get(i).x = t_x;
		    		shapeList.get(i).y = t_y;
		    		shapeList.get(i).draw();
		    		return;
		    		
		    	}
		    	else{
		    		System.out.println("Searching Shape ID: " + shapeList.get(i).id);
		    		shapeList.get(i).draw();
	    		}
	    	}
    	}
    }
    
    void chgSize(int opt){
    	if(selShapeID != NULL){
    		for(int i = 0; i < shapeList.size(); i++){
        		
    	    	if(selShapeID == shapeList.get(i).id){
    	    		System.out.println("Selected Shape with ID: " + shapeList.get(i).id + "resized");
    	    		if(opt < 0 && shapeList.get(i).size > 0.0f) shapeList.get(i).size -= 0.01f;
    	    		else shapeList.get(i).size += 0.01f;
    	    		
    	    		shapeList.get(i).draw();
    	    		
    	    		
    	    	}
    	    	else{
    	    		System.out.println("Searching Shape ID: " + shapeList.get(i).id);
    	    		shapeList.get(i).draw();
    	    		
        		}
        	}
    	}
    }
    
    
    void deleteObj(){
    	if(selShapeID != NULL){
    		for(int i = 0; i < shapeList.size(); i++){
        		
    	    	if(selShapeID == shapeList.get(i).id){
    	    		shapeList.remove(i);
//    	    		float[] ambient = {0f, 0f, 0f, 1f};
//    	    		float[] light = {1.0f, 1.0f, 1f ,1f};
//    	    		float[] diff = {1f, 0f, 1f, 1f};
//    	    		float shine = 60f;
    	    		//shapeList.get(i).changeLightning(ambient, light, diff, shine);
    	    		   	    		
    	    		
    	    	}
    	    	else{
    	    		System.out.println("Searching Shape ID: " + shapeList.get(i).id);
    	    		shapeList.get(i).draw();
    	    		
        		}
        	}
    	}
    	
    }
    
	public void keyPressed(KeyEvent key)
	  {
	    
	  }



	public void mouseEntered(MouseEvent arg0) {
		
		
	}






	
	public void mouseExited(MouseEvent arg0) {
		
		
	}






	@Override
	public void mousePressed(MouseEvent e) {
		
		cmd = SELECT;
	    mouse_x = (double) e.getX();
	    mouse_y = (double) e.getY();
	   
		
	}







	public void mouseReleased(MouseEvent arg0) {
		cmd = UPDATE;
		
	}

	
	public void mouseClicked(MouseEvent e) {
		
		

	    
	}

	
	public void keyReleased(KeyEvent e) {
		
		
	}

	
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()){
		case KeyEvent.VK_1:
			System.out.println("SPHERE");
			selectedObj = SPHERE;
			break;
		case KeyEvent.VK_2:
			System.out.println("CUBE");
			selectedObj = CUBE;
			break;
		case KeyEvent.VK_3:
			System.out.println("TORUS");
			selectedObj = TORUS;
			break;
			
		case KeyEvent.VK_4:
			System.out.println("TEAPOT");
			selectedObj = TEAPOT;
			break;
			
		case KeyEvent.VK_0:
			System.out.println("NOTHING");
			selectedObj = NOTHING;
			break;
			
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
			
		case KeyEvent.VK_8:
			cmd = RESIZE;
			chgMode = -1;
			break;
			
		case KeyEvent.VK_9:
			cmd = RESIZE;
			chgMode = 1;
			break;
			
		case KeyEvent.VK_DELETE:
			cmd = DELETE;
			break;
		case KeyEvent.VK_ENTER:
			cmd = LIGHT;
			break;
		}
		
		
		

	}

	@Override
	public void mouseDragged(MouseEvent e) {
	mouse_x = (double)e.getX();
	mouse_y = (double)e.getY();
	cmd=MOVE;
	
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}
	
	void customObj(){
		
		
		JTextField rField = new JTextField(5);
	    JTextField gField = new JTextField(5);
	    JTextField bField = new JTextField(5);
	    JTextField aField = new JTextField(5);


	      JPanel myPanel = new JPanel();
	      myPanel.add(new JLabel("r:"));
	      myPanel.add(rField);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("g:"));
	      myPanel.add(gField);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("b:"));
	      myPanel.add(bField);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("a:"));
	      myPanel.add(aField);

	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
	               "Please enter values for lightning", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	         System.out.println("r value: " + rField.getText());
	         System.out.println("g value: " + gField.getText());
	         System.out.println("b value: " + bField.getText());
	         System.out.println("a value: " + aField.getText());
	      }

	   }
//		ArrayList<Double> input = new ArrayList<Double>();
//		input.add(Double.parseDouble(JOptionPane.showInputDialog(this 
//			       ,"Size")));
//		
//		input.add(Double.parseDouble(JOptionPane.showInputDialog(this 
//			       ,"Ambient")));
//		
//		for(int i = 0; i<input.size(); i++){
//			System.out.println(input.get(i));
//		}
	
}
	
