package lab2;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Shape implements GLEventListener, MouseListener, KeyListener,
		MouseMotionListener {

	ArrayList<Shapes> shapeList = new ArrayList<Shapes>();

	GLU glu = new GLU();
	static GLCanvas canvas;

	final int NULL = -1, SELECT = 1, UPDATE = 2, MOVE = 3, RESIZE = 4,
			DELETE = 5, LIGHT = 6, PRINT = 7, DELETEALL= 8;
	final int NOTHING = 0, SPHERE = 1, CUBE = 2, TORUS = 3, TEAPOT = 4;
	int chgMode = 0;
	int cmd = 1;
	int prevShapeID = NULL;
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
	 * abstract class that is extended by all figures for draw method x position
	 * for all figures are random
	 */
	abstract class Shapes {
		int id;
		GLU glu;
		GL2 gl;
		GLUT glut = new GLUT();
		String shape;
		double x, y, z = 0;
		
		float[] ambientClr = {0,0,0,1};
		float[] lightClr = {1,1,1,1};
		float[] diffuse = {0,1,0,1};
		float shiny = 50;
		float size = 0.2f;

		Shapes(GLU glu, GL2 gl) {
			this.gl = gl;
			this.glu = glu;
		}

		public abstract void draw();

		public int rnd() {
			double value = Math.random();
			// value = rand.nextGaussian();
			value = value * 100000.0;
			return (int) value;
		}

		public void shapesToString() {
			System.out.println("Shape: " + shape);
			System.out.println("x, y , z: " + x + ", " + y + ", " + z);
			System.out.println("Size: " + size);
			System.out.println("Ambient(RGB): {"+ambientClr[0]+", "+ambientClr[1]+", " + ambientClr[2]+"}");
			System.out.println("Specular(RGB): {"+lightClr[0]+", "+lightClr[1]+", " + lightClr[2]+"}");
			System.out.println("Diffuse(RGB): {"+diffuse[0]+", "+diffuse[1]+", " + diffuse[2]+"}\n");
		}

		void changeLightning(float[] ambient, float[] light, float[] diff,
				float shine) {
			
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diff, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, light, 0);
			gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shine);
			gl.glPopAttrib();
		}

	}

	class Cube extends Shapes {



		Cube(double x, double y, float sz, GL2 gl, GLU glu) {
			super(glu, gl);
			this.x = x;
			this.y = y;
			this.size = sz;
			this.id = rnd();
			this.shape = "Cube";
		}

		@Override
		public void draw() {

			// GL2 gl = drawable.getGL().getGL2();
			gl.glPushName(id);
			gl.glPushMatrix();
			gl.glRotatef(30.0f, -1.0f, -1.0f, 1.0f);
			gl.glTranslatef((float) x, (float) y, (float)z); // translate right and
														// into the screen

			// of the color cube
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
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

	class Sphere extends Shapes {




		Sphere(GL2 gl, GLU glu,float sz, double x, double y) {
			super(glu, gl);
			this.shape = "Sphere";
			this.id = rnd();
			this.x = x;
			this.y = y;
			this.shiny = 2f;
			this.size = sz;

		}

		@Override
		public void draw() {
			gl.glPushName(id);

			// GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef((float) x, (float) y, (float)z);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
			gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);

			glut.glutSolidSphere(size, 50, 50);

			gl.glPopAttrib();
			gl.glPopMatrix();

		}
	}

	class teaPot extends Shapes {
		

		

		teaPot(GL2 gl, GLU glu,float sz, double x, double y) {
			super(glu, gl);
			this.x = x;
			this.y = y;
			this.id = rnd();
			size = sz;
			shape = "Teapot";

		}

		public void draw() {
			gl.glPushName(id);

			// GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef((float) x, (float) y, (float)z);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
			gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);

			glut.glutSolidTeapot((double) size);
			gl.glPopAttrib();
			gl.glPopMatrix();

		}
	}

	class Torus extends Shapes {
		


		Torus(GL2 gl, GLU glu, float sz, double x, double y) {
			super(glu, gl);
			this.x = x;
			this.y = y;
			this.id = rnd();
			this.size = sz;
			this.shape = "Torus";
		}

		public void draw() {
			gl.glPushName(id);
		
			// GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef((float) x, (float) y, (float)z);
			gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);

			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, ambientClr, 0);
			gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, lightClr, 0);
			gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, shiny);

			glut.glutSolidTorus(size, size + (size / 2), 10, 50);
			gl.glPopAttrib();
			gl.glPopMatrix();

		}
	}
	void selShape(){
		for (int i = 0; i < shapeList.size(); i++) {
			
			if (selShapeID == shapeList.get(i).id) {
				System.out.println("IIID");
				for(int h = 0; h < shapeList.get(i).ambientClr.length-2;h++){
					shapeList.get(i).ambientClr[h] = 0.5f;	
				}
				shapeList.get(i).draw();
				if(prevShapeID != NULL && selShapeID != prevShapeID)deselShape();
			} else {
				
				shapeList.get(i).draw();

			}
		}
	}
	
	void deselShape(){
		for (int i = 0; i < shapeList.size(); i++) {
					
			if (prevShapeID == shapeList.get(i).id) {
				System.out.println("PREV");
				for(int h = 0; h < shapeList.get(i).ambientClr.length-2;h++){
					shapeList.get(i).ambientClr[h] = 0f;	
				}
				shapeList.get(i).draw();
			} else {
				
				shapeList.get(i).draw();

			}
		}
	}


	
	void addDefShapes(GL2 gl, GLU glu, double x, double y) {
		

		switch (selectedObj) {
		case SPHERE:
			shapeList.add(new Sphere(gl, glu,0.2f, x, y));
			break;
		case CUBE:
			shapeList.add(new Cube(x, y, 0.3f, gl, glu));
			break;

		case TORUS:
			shapeList.add(new Torus(gl, glu, 0.2f, x, y));
			break;

		case TEAPOT:
			shapeList.add(new teaPot(gl, glu, 0.2f, x, y));
			break;
		}
		selectedObj = NOTHING;

	}

	public void render(GL2 gl, GLUT glut) {

	}

	/*
	 * Add shapes Star, square or Torus here Takes size as constructor parameter
	 * Torus(Steps), Star/Square(Scale)
	 */

	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		GLUT glut = new GLUT();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		double[] res = getOBJPos((int) mouse_x, (int) mouse_y, gl);
		double x_s = res[0], y_s = res[1];

		// clear color and depth buffers
		// ----- Render the Color Cube -----

		switch (cmd) {
		case UPDATE:
			for (int i = 0; i < shapeList.size(); i++) {

				shapeList.get(i).draw();
			}
			render(gl, glut);

			break;
		case SELECT:
			int buffsize = 512;
			int[] viewPort = new int[4];
			// double[] res = getOBJPos((int)mouse_x, (int)mouse_y, gl);
			// double x = res[0], y = res[1];
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
			glu.gluOrtho2D(10.0f, 10.0f, 10f, 10.0f);
			// glu.gluPerspective(60.0f,
			// (float)canvas.getWidth()/(float)canvas.getHeight(),0.1f, 100);
			

			if (selectedObj != NOTHING && chgMode == 1) addShapes(gl, glu, x_s, y_s);
			else addDefShapes(gl, glu, x_s, y_s);
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.get(i).draw();
			}
			render(gl, glut);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glFlush();
			hits = gl.glRenderMode(GL2.GL_RENDER);
			prevShapeID = selShapeID;
			processHits(hits, selectBuffer);
			if(selShapeID != NULL) selShape();
			else deselShape();

			cmd = UPDATE;

			break;
		case MOVE:
			moveShape(x_s, y_s);
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.get(i).draw();
			}
			cmd = UPDATE;
			break;
		
		case RESIZE:
			chgSize(chgMode);
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.get(i).draw();
			}
			cmd = UPDATE;
			break;
		case DELETEALL:
			deleteObj();
		case DELETE:
			deleteObj();
			cmd = UPDATE;
			break;

		case LIGHT:
			changeLight(gl);

			cmd = UPDATE;
			break;
		case PRINT:
			for (int i = 0; i < shapeList.size(); i++) {
				shapeList.get(i).shapesToString();
			}
			cmd = UPDATE;
			break;
		}

	}

	public void processHits(int hits, IntBuffer buffer) {

		int offset = 0;
		int names;
		float z1, z2;
		for (int i = 0; i < hits; i++) {
			
			names = buffer.get(offset);
			offset++;
			z1 = (float) (buffer.get(offset) & 0xffffffffL) / 0x7fffffff;
			offset++;
			z2 = (float) (buffer.get(offset) & 0xffffffffL) / 0x7fffffff;
			offset++;

			for (int j = 0; j < names; j++) {
				if (j == (names - 1)) {
					
					selShapeID = buffer.get(offset);
					if(prevShapeID == selShapeID)prevShapeID = NULL;
				} else
				
				offset++;
			}		
		}
		if (hits == 0)
			
			selShapeID = NULL;
		
	}

	public void update() {

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {

	}
	public void initLight(GL2 gl, float[] ambient, float[] spec, float[] diffuse, float[] lightPos){
		gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes
		// out lighting

		
		
		//shapeList.add(new Sphere(gl, glu, 0.0, 0.0));
		
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spec, 0);
		gl.glLightf( GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 60.0F );
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
	}
	/*
	 * Init list!
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.getGL().setSwapInterval(1);
		// glu.gluPerspective(60.0f,
		// (float)canvas.getWidth()/(float)canvas.getHeight(),0.1f, 100);
		glu.gluOrtho2D(10.0f, 10.0f, 10f, 10.0f);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f); // set clear depth value to farthest
		gl.glEnable(GL2.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL2.GL_DEPTH_TEST);
		// gl.glDepthFunc(GL2.GL_LEQUAL); // the type of depth test to do
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST); // best
				
		
		float[] noAmbient = { 0.2f, 0.2f, 0.2f, 1f }; // low ambient light
		float[] spec = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] diffuse = { 0.25f, .25f, 0.25f, 0.25f }; // full diffuse color
		float[] lightPos = { 100, 50, -200, 0 }; // light position
																		
		initLight(gl, noAmbient, spec, diffuse, lightPos);
		  
		  

		// addShapes(gl, glu);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// GL2 gl = drawable.getGL().getGL2();
		// GLU glu = new GLU();
		//
		// if (height == 0) height = 1; // prevent divide by zero
		// float aspect = (float)width / height;
		//
		// // Set the view port (display area) to cover the entire window
		// gl.glViewport(0, 0, width, height);
		//
		// // Setup perspective projection, with aspect ratio matches viewport
		// gl.glMatrixMode(GL2.GL_PROJECTION); // choose projection matrix
		// gl.glLoadIdentity(); // reset projection matrix
		// glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear,
		// zFar
		//
		// // Enable the model-view transform
		// gl.glMatrixMode(GL2.GL_MODELVIEW);
		// gl.glLoadIdentity(); // reset
		//
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// *((float)canvas.getWidth()/(float)canvas.getHeight())
		glu.gluOrtho2D(10.0f, 10.0f, 10f, 10.0f);
		// glu.gluPerspective(60.0f,
		// (float)canvas.getWidth()/(float)canvas.getHeight(),0.1f, 100);
	}

	class ligthSource {
		void lightSoruce() {

		}
	}

	double[] getOBJPos(int x, int y, GL2 gl) {
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
				mvmatrix, 0, projmatrix, 0, viewport, 0, wcoord, 0);

		// for(int i =0; i< wcoord.length;i++){
		// System.out.println(wcoord[i] + ", ");
		// }
		return wcoord;
	}

	void moveShape(double t_x, double t_y) {
		// GL2 gl = drawable.getGL().getGL2();
		// getOBJPos(x ,y, );
		if (selShapeID != NULL) {
			for (int i = 0; i < shapeList.size(); i++) {

				if (selShapeID == shapeList.get(i).id) {
		
					shapeList.get(i).x = t_x;
					shapeList.get(i).y = t_y;
					shapeList.get(i).draw();
					return;	
			
					}else {
					shapeList.get(i).draw();
				}
			}
		}
	}

	void chgSize(int opt) {
		if (selShapeID != NULL) {
			for (int i = 0; i < shapeList.size(); i++) {

				if (selShapeID == shapeList.get(i).id) {
				
					if (opt < 0 && shapeList.get(i).size > 0.0f)
						shapeList.get(i).size -= 0.01f;	
					
					else shapeList.get(i).size += 0.01f;
					shapeList.get(i).draw();
					

				} else {
					
					shapeList.get(i).draw();

				}
			}
		}
		chgMode = 0;
	}

	void deleteObj() {
		if (selShapeID != NULL) {
			for (int i = 0; i < shapeList.size(); i++) {

				if (selShapeID == shapeList.get(i).id) {
					shapeList.remove(i);
					// float[] ambient = {0f, 0f, 0f, 1f};
					// float[] light = {1.0f, 1.0f, 1f ,1f};
					// float[] diff = {1f, 0f, 1f, 1f};
					// float shine = 60f;
					// shapeList.get(i).changeLightning(ambient, light, diff,
					// shine);
				}else {
					
					shapeList.get(i).draw();

				}
			}
		}
		if(cmd == DELETEALL)shapeList.clear();
}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
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
			
		case KeyEvent.VK_BACK_SPACE:
			cmd=DELETEALL;
			break;

		case KeyEvent.VK_NUMPAD7:
			cmd = LIGHT;
			break;

		case KeyEvent.VK_SPACE:
			cmd = PRINT;
			break;
		}
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.getModifiers()  == MouseEvent.BUTTON3_MASK){
			cmd = SELECT;
			chgMode = 1;
			mouse_x = (double) e.getX();
			mouse_y = (double) e.getY();
		}
		else{
			cmd = SELECT;
			mouse_x = (double) e.getX();
			mouse_y = (double) e.getY();
		}
		

	}

	public void mouseReleased(MouseEvent arg0) {
//		cmd = UPDATE;

	}

	public void mouseClicked(MouseEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {
		

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse_x = (double) e.getX();
		mouse_y = (double) e.getY();
		cmd = MOVE;

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	// CHANGES LIGHTNING ATTRIBS
	void changeLight(GL2 gl){
		
		JTextField pos0 = new JTextField(5);
		JTextField pos1 = new JTextField(5);
		JTextField pos2 = new JTextField(5);
		JTextField pos3 = new JTextField(5);
		
		float[] ambient = { 0.2f, 0.2f, 0.2f, 1f }; // low ambient light
		float[] spec = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] diffuse = { 0.25f, .25f, 0.25f, 1f }; // full diffuse color
		float[] lightPos = { 100, 50, -200, 0 }; // light position
		JPanel myPanel = new JPanel();

		
		myPanel.add(new JLabel("1:"));
		myPanel.add(pos0);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("2:"));
		myPanel.add(pos1);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("3:"));
		myPanel.add(pos2);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	
		
		
		
		int res = JOptionPane.showConfirmDialog(null, myPanel,
		"Please enter values for new ambient lightning(0.0 - 1.0, decimal)", JOptionPane.OK_CANCEL_OPTION);
		if(isNumeric(pos0.getText()) && isNumeric(pos1.getText()) && isNumeric(pos2.getText())){
			float[] temp = {Float.parseFloat(pos0.getText()), Float.parseFloat(pos1.getText()), Float.parseFloat(pos2.getText()), 1f};
			for(int i = 0; i < ambient.length;i++){
				ambient[i] = temp[i];	
			}
		}else if(JOptionPane.CANCEL_OPTION == res){
			
		}else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. ambientlight options set to default");
		
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
		"Please enter values for specular lightning(0.0 - 1.0, decimal)", JOptionPane.OK_CANCEL_OPTION);
		if(isNumeric(pos0.getText()) && isNumeric(pos1.getText()) && isNumeric(pos2.getText())){
			float[] temp = {Float.parseFloat(pos0.getText()), Float.parseFloat(pos1.getText()), Float.parseFloat(pos2.getText()), 1f};
			for(int i = 0; i < spec.length;i++){
				spec[i] = temp[i];	
			}
		}else if(JOptionPane.CANCEL_OPTION == res){
			
		}else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. Light options set to default");
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
		"Please enter values for diffuse lightning(0.0 - 1.0, decimal)", JOptionPane.OK_CANCEL_OPTION);
		if(isNumeric(pos0.getText()) && isNumeric(pos1.getText()) && isNumeric(pos2.getText())){
			float[] temp = {Float.parseFloat(pos0.getText()), Float.parseFloat(pos1.getText()), Float.parseFloat(pos2.getText()), 1f};
			for(int i = 0; i < diffuse.length;i++){
				diffuse[i] = temp[i];	
			}
		}else if(JOptionPane.CANCEL_OPTION == res){
			
		}else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. Light options set to default");
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
				"Please enter values for new light position(-1.0 - 1.0, decimal)", JOptionPane.OK_CANCEL_OPTION);
				if(isNumeric(pos0.getText()) && isNumeric(pos1.getText()) && isNumeric(pos2.getText())){
					float[] temp = {Float.parseFloat(pos0.getText()), Float.parseFloat(pos1.getText()), Float.parseFloat(pos2.getText()), 1f};
					for(int i = 0; i < lightPos.length;i++){
						lightPos[i] = temp[i];	
					}
					
				}else if(JOptionPane.CANCEL_OPTION == res){
					
				}else JOptionPane.showMessageDialog(null, "Not a valid input,numbers only. ambientlight options set to default");
				
				initLight(gl, ambient, spec, diffuse, lightPos);
	}

	
	//CREATES CUSTOMIZED SHAPES
	void addShapes(GL2 gl, GLU glu, double x, double y){

		
		JTextField size = new JTextField(5);
		JTextField rField = new JTextField(5);
		JTextField gField = new JTextField(5);
		JTextField bField = new JTextField(5);
		
		float objSize = 0.2f;
		float[] ambient = {0,0,0,1};
		float[] specular = {1,1,1,1};
		float[] diffuse = {0,1,0,1};
		double z = 0;
		JPanel myPanel = new JPanel();
		
		myPanel.add(new JLabel("Size(float):"));
		myPanel.add(size);
		int res = JOptionPane.showConfirmDialog(null, myPanel,
				"Choose size for the object(>0.0)", JOptionPane.OK_CANCEL_OPTION);
				if(isNumeric(size.getText())) objSize = Float.parseFloat(size.getText());
				else if(JOptionPane.CANCEL_OPTION == res);
				else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. Size set to default 0.2");
		myPanel.removeAll();
		
		
		
		myPanel.add(new JLabel("1:"));
		myPanel.add(rField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("2:"));
		myPanel.add(gField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("3:"));
		myPanel.add(bField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
				"Enter Coordinates for the object between(-1,1)xyz", JOptionPane.OK_CANCEL_OPTION);
				if(isNumeric(rField.getText()) && isNumeric(gField.getText()) && isNumeric(bField.getText())){
					x= Double.parseDouble(rField.getText());
					y = Double.parseDouble(gField.getText());
					z = Double.parseDouble(bField.getText());
				}else if(JOptionPane.CANCEL_OPTION == res);
					else JOptionPane.showMessageDialog(null, "Not a valid input, -1.0, 1.0 interval. Position set to mouseclick pos");
				
		
		
		
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
		"Please enter values for ambient lightning(0.0 - 1.0, decimal, RGB)", JOptionPane.OK_CANCEL_OPTION);
		if(isNumeric(rField.getText()) && isNumeric(gField.getText()) && isNumeric(bField.getText())){
			float[] temp = {Float.parseFloat(rField.getText()), Float.parseFloat(gField.getText()), Float.parseFloat(bField.getText()), 1f};
			for(int i = 0; i < ambient.length;i++){
				ambient[i] = temp[i];	
			}
		}else if(JOptionPane.CANCEL_OPTION == res);
			
		else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. Light options set to default");
		
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
		"Please enter values for specular lightning(0.0 - 1.0, decimal, RGB)", JOptionPane.OK_CANCEL_OPTION);
		if(isNumeric(rField.getText()) && isNumeric(gField.getText()) && isNumeric(bField.getText())){
			float[] temp = {Float.parseFloat(rField.getText()), Float.parseFloat(gField.getText()), Float.parseFloat(bField.getText()), 1f};
			for(int i = 0; i < specular.length;i++){
				specular[i] = temp[i];	
			}
		}else if(JOptionPane.CANCEL_OPTION == res);
			
		else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. Light options set to default");
		
		res = JOptionPane.showConfirmDialog(null, myPanel,
		"Please enter values for diffuse lightning(0.0 - 1.0, decimal, RGB)", JOptionPane.OK_CANCEL_OPTION);
		if(isNumeric(rField.getText()) && isNumeric(gField.getText()) && isNumeric(bField.getText())){
			float[] temp = {Float.parseFloat(rField.getText()), Float.parseFloat(gField.getText()), Float.parseFloat(bField.getText()), 1f};
			for(int i = 0; i < diffuse.length;i++){
				diffuse[i] = temp[i];	
			}
		}else if(JOptionPane.CANCEL_OPTION == res);
			
		else JOptionPane.showMessageDialog(null, "Not a valid input, 0.0 - 1.0 format. Light options set to default");


		switch (selectedObj) {
		case SPHERE:
			Sphere s = new Sphere(gl, glu,objSize, x, y);
			shapeList.add(s);
			s.z=z;
			for(int i = 0; i < diffuse.length;i++){
				s.diffuse[i] = diffuse[i];
				s.ambientClr[i] = ambient[i];
				s.lightClr[i] = specular[i];
			}
			break;
			
		case CUBE:
			Cube c = new Cube(x, y, objSize, gl, glu);
			shapeList.add(c);
			c.z = z;
			for(int i = 0; i < diffuse.length;i++){
				c.diffuse[i] = diffuse[i];
				c.ambientClr[i] = ambient[i];
				c.lightClr[i] = specular[i];
			}
			break;

		case TORUS:
			Torus t = new Torus(gl, glu, objSize, x, y);
			shapeList.add(t);
			t.z = z;
			for(int i = 0; i < diffuse.length;i++){
				t.diffuse[i] = diffuse[i];
				t.ambientClr[i] = ambient[i];
				t.lightClr[i] = specular[i];
			}
			break;

		case TEAPOT:
			teaPot p = new teaPot(gl, glu, objSize, x, y);
			shapeList.add(p);
			p.z=z;
			for(int i = 0; i < diffuse.length;i++){
				p.diffuse[i] = diffuse[i];
				p.ambientClr[i] = ambient[i];
				p.lightClr[i] = specular[i];
			}
			break;
		}
		selectedObj = NOTHING;
		
	}


			

}
