package net.latin.server.security.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class VerificationCodeGenerator {
	
	private static final Random RANDOM = new Random();
	private static final String[] chars = { "A","B","C","D","E","F","G","H","I","J","K","L",
											"N","M","O","P","Q","R","S","T","U","V","W","X",
											"Y","Z","a","b","c","d","e","f","g","h","i","j","k","l",
											"n","m","o","p","q","r","s","t","u","v","w","x",
											"y","z","1","2","3","4","5","6","7","8","9","0","1","2","3","4","5","6","7","8","9","0"};
	
	private static final int WIDTH = 35;
	private static final int HEIGHT = 64;
	
	private static final int TRANSLATE_X = 6;
	private static final int SHADOW_TRANSLATE_X = 5;
	private static final int SHADOW_TRANSLATE_Y = 5;
	private static final int FONT_SIZE = 42;
	private static final int MIN_TRANSLATE_CHAR_Y = 29; 
	private static final int MAX_TRANSLATE_CHAR_Y = 35;
	private String verificationCode;
	
	public VerificationCodeGenerator(OutputStream out) {
		writeOutputStream(make(),out);
	}
	public BufferedImage make() {
		int minLength = 4;//TODO param
		int maxLengh = 5;//TODO param
		String code = getStringRandom(minLength, maxLengh);
		BufferedImage img = createImage(code);  
		this.verificationCode = code;
		return img;
	}
	
	/**
	 * crea un camptcha a partir de un string
	 * @param code
	 * @return
	 */
	private static final BufferedImage createImage(String code) {
		BufferedImage img = new BufferedImage(WIDTH * code.length() + TRANSLATE_X, HEIGHT, BufferedImage.TYPE_INT_RGB);  
		Graphics2D g = (Graphics2D)img.getGraphics();
		
		setRenderingHint(g);
		setColorBackground(g,code.length());
	    drawCrossLines(code, g);
	    drawCode(code, g);
	    return img;
	}

	/**
	 * Dibuja un string en el gráfico
	 * @param code
	 * @param g
	 */
	private static void drawCode(String code, Graphics2D g) {
		for (int i = 0; i < code.length(); i++) {
			setFont(g);
	    	g.setColor(Color.BLACK);
	    	int y = getIntRandom(MIN_TRANSLATE_CHAR_Y,MAX_TRANSLATE_CHAR_Y);
			g.drawString(String.valueOf(code.charAt(i)), TRANSLATE_X +(i*WIDTH) + SHADOW_TRANSLATE_X, y + SHADOW_TRANSLATE_Y);
	    	g.setColor(getRandomColor());
	    	g.drawString(String.valueOf(code.charAt(i)), TRANSLATE_X +(i*WIDTH), y);
		}
	}

	/**
	 * Dibuja lineas aleatorias en el backgruond del gráfico
	 * @param code
	 * @param g
	 */
	private static void drawCrossLines(String code, Graphics2D g) {
		for (int i = 0; i < code.length(); i++) {
	    	g.setColor(getRandomColor());
	    	g.drawLine((i*WIDTH), getIntRandom(HEIGHT), +((i+1)*WIDTH), getIntRandom(HEIGHT));
	    	g.setColor(getRandomColor());
	    	g.drawLine(getIntRandom((i*WIDTH),((i+1)*WIDTH)), 0, getIntRandom((i*WIDTH),((i+1)*WIDTH)), HEIGHT);
	    }
	}


	/**
	 * Setea una fuente para dibujar un string en un gráfico
	 * @param g
	 */
	private static void setFont(Graphics2D g) {
	    int rotate = getIntRandom(20);
        AffineTransform fontAT = new AffineTransform();
        fontAT.rotate(RANDOM.nextBoolean() ? Math.toRadians(rotate) : -Math.toRadians(rotate/2));
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE).deriveFont(fontAT);
        g.setFont(font);
	}

	/**
	 * setea las propiedades del renderizado del gráfico
	 * @param g
	 */
	private static void setRenderingHint(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	/**
	 * Setea un color de fondo
	 * @param g
	 * @param stringLength
	 */
	private static void setColorBackground(Graphics2D g, int stringLength) {
		g.setColor(new Color(252,250,255));//Color.WHITE
		g.fillRect(0, 0, WIDTH * stringLength +TRANSLATE_X, HEIGHT);
	}

	/**
	 * Obtiene un color a azar
	 * @return
	 */
	private static final Color getRandomColor(){
		return new Color(getIntRandom(255),getIntRandom(255),getIntRandom(255));
	}
	/**
	 * Genera un string dado la mínima y máxima longitud
	 * @param minLength
	 * @param maxLengh
	 * @return
	 */
	private static final String getStringRandom(int minLength,int maxLengh) {
		int length = getIntRandom(minLength,maxLengh);
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append( chars[getIntRandom(chars.length)]);
		}
		return sb.toString();
	}
	
	private static final int getIntRandom(int hasta){
		return RANDOM.nextInt(hasta);
	}
	private static final int getIntRandom(int desde, int hasta){
		return RANDOM.nextInt(hasta - desde) + desde;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	private static final void writeOutputStream(BufferedImage bimage, OutputStream out){
    	try {
			ImageIO.write(bimage, "png", out);
		} catch (IOException e) {
			throw new RuntimeException("Error", e);
		}
	 }
	/**
	 * Comprueba que dos códigos sean iguales
	 * (soportando por ejemplo que el 0 es igual que la o)
	 * @param userCode
	 * @param codeGenerated
	 * @return
	 */
	public static boolean compareCode(String userCode, String codeGenerated) {
		userCode = userCode.toUpperCase();
		codeGenerated = codeGenerated.toUpperCase();
		
		codeGenerated= codeGenerated.replace("L", "I");
		userCode= userCode.replace("L", "I");
		
		codeGenerated= codeGenerated.replace("1", "I");
		userCode = userCode.replace("1", "I");
		
		codeGenerated= codeGenerated.replace("7", "I");
		userCode = userCode.replace("7", "I");
		
		codeGenerated= codeGenerated.replace("Q", "O"); 
		userCode = userCode.replace("Q", "O");
		
		codeGenerated= codeGenerated.replace("Ñ", "N"); 
		userCode = userCode.replace("Ñ", "N");
		
		codeGenerated= codeGenerated.replace("M", "N"); 
		userCode = userCode.replace("M", "N");
		
		codeGenerated= codeGenerated.replace("0", "O"); 
		userCode = userCode.replace("0", "O");
		
		codeGenerated= codeGenerated.replace("6", "G"); 
		userCode = userCode.replace("6", "G");
		
		return  userCode.equalsIgnoreCase(codeGenerated);
		
	}
}
