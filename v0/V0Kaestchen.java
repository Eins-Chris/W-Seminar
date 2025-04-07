import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Kästchen V2 (mit Umlauten) (c) A. Eckert eckert@gmg.amberg.de
 * 
 * @version 2.0 - 01. Januar 2020
 * 
 **/
public abstract class V0Kaestchen extends JFrame {
	private int randX = 10;
	private int randY = 10;
	private int kaestchenBreite = 20;
	private int kaestchenHoehe = 20;
	private int kaestchenAnzX = 20;
	private int kaestchenAnzY = 20;
	private int nichtfuellbreite = 1;
	private Color strichfarbe = Color.LIGHT_GRAY;
	private JPanel jPanel1;
	private int fensterhoehe;
	private int fensterbreite;
	private int spielfeldhoehe;
	private int spielfeldbreite;
	private Color[][] matrix;
	private String[][] text;
	private Image[][] bild;
	private Hashtable<String, Image> bilder = new Hashtable<String, Image>();
	private Thread[] ticker = new Thread[100];
	private double[] stepDauer = new double[100];
	private Color farbeDurchsichtig = new Color(0, 0, 0, 0);
	private BufferedImage hintergrundbild;
	private boolean autopaint = true;
	private int startY = 0;
	private int startX = 0;

	/**
	 * Standardkonstruktor, erzeugt 20x20 Kästchen der Breite 20 und Höhe 20
	 */
	public V0Kaestchen() {
		this(20, 20, 20, 20);
	}

	/**
	 * Konstruktor
	 * 
	 * @param kaeBreite
	 *            - Breite eines Kästchens
	 * @param kaeHoehe
	 *            - Höhe eines Kästchens
	 * @param kaeAnzX
	 *            - Anzahl der Kästchen horizontal
	 * @param kaeAnzY
	 *            - Anzahl der Kästchen vertikal
	 */
	public V0Kaestchen(int kaeBreite, int kaeHoehe, int kaeAnzX, int kaeAnzY) {
		this(kaeBreite, kaeHoehe, kaeAnzX, kaeAnzY, false);
	}

	/**
	 * Konstruktor
	 * 
	 * @param kaeBreite
	 *            - Breite eines Kästchens
	 * @param kaeHoehe
	 *            - Höhe eines Kästchens
	 * @param kaeAnzX
	 *            - Anzahl der Kästchen horizontal
	 * @param kaeAnzY
	 *            - Anzahl der Kästchen vertikal
	 * @param ohneRand
	 *            - true oder false
	 */
	public V0Kaestchen(int kaeBreite, int kaeHoehe, int kaeAnzX, int kaeAnzY, boolean ohneRand) {
		if (ohneRand) {
			strichfarbe = farbeDurchsichtig;
			nichtfuellbreite = 0;
			randX = 0;
			randY = 0;
		}
		this.kaestchenAnzX = kaeAnzX;
		this.kaestchenAnzY = kaeAnzY;
		this.kaestchenBreite = kaeBreite;
		this.kaestchenHoehe = kaeHoehe;
		spielfeldhoehe = kaestchenAnzY * kaestchenHoehe + 2 * randY;
		spielfeldbreite = kaestchenAnzX * kaestchenBreite + 2 * randX;
		fensterhoehe = spielfeldhoehe;
		fensterbreite = spielfeldbreite;
		matrix = new Color[kaestchenAnzX][kaestchenAnzY];
		for (int x = 0; x < kaestchenAnzX; x++) {
			for (int y = 0; y < kaestchenAnzY; y++) {
				matrix[x][y] = farbeDurchsichtig;
			}
		}
		text = new String[kaestchenAnzX][kaestchenAnzY];
		bild = new Image[kaestchenAnzX][kaestchenAnzY];
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				String taste = KeyEvent.getKeyText(evt.getKeyCode());
				if (evt.isControlDown())
					tasteClickCtrl(taste);
				else
					tasteClick(taste);
			}
		});
		jPanel1 = new JPanel() {
			public void paint(Graphics g) {
				super.paint(g);
				BufferedImage buff = new BufferedImage(spielfeldbreite, spielfeldhoehe, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) g;
				Graphics2D temp = buff.createGraphics();
				temp.setColor(strichfarbe);
				for (int x = 0; x <= kaestchenAnzX; x++) {
					temp.drawLine(randX + x * kaestchenBreite, randY, randX + x * kaestchenBreite, spielfeldhoehe - randY);
				}
				for (int y = 0; y <= kaestchenAnzY; y++) {
					temp.drawLine(randX, randY + y * kaestchenHoehe, spielfeldbreite - randX, randY + y * kaestchenHoehe);
				}
				for (int x = 0; x < kaestchenAnzX; x++) {
					for (int y = 0; y < kaestchenAnzY; y++) {
						if (bild[x][y] != null)
							continue;
						temp.setColor(matrix[x][y]);
						temp.fillRect(randX + x * kaestchenBreite + nichtfuellbreite, randY + y * kaestchenHoehe + nichtfuellbreite,
								kaestchenBreite - nichtfuellbreite, kaestchenHoehe - nichtfuellbreite);
					}
				}
				temp.setColor(Color.black);
				for (int x = 0; x < kaestchenAnzX; x++) {
					for (int y = 0; y < kaestchenAnzY; y++) {
						if (text[x][y] != null) {
							int breite = temp.getFontMetrics().stringWidth(text[x][y]);
							int hoehe = temp.getFontMetrics().getAscent();
							temp.drawString(text[x][y], (int) (randX + (x + 0.5) * kaestchenBreite - 0.5 * breite),
									(int) (randY + (y + 0.5) * kaestchenHoehe + 0.5 * hoehe));
						}
						if (bild[x][y] != null) {
							temp.drawImage(bild[x][y], randX + x * kaestchenBreite, randY + y * kaestchenHoehe, kaestchenBreite, kaestchenHoehe, null);
						}
					}
				}
				if (hintergrundbild != null)
					g2d.drawImage(hintergrundbild, 0, 0, getWidth(), getHeight(), null);

				g2d.drawImage(buff, startX, startY, null);
			}
		};
		getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.setBackground(new java.awt.Color(255, 255, 250));
		jPanel1.setPreferredSize(new java.awt.Dimension(1044, 1042));
		jPanel1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				int x = (-startX + evt.getX() - randX + kaestchenBreite) / kaestchenBreite;
				int y = (-startY + evt.getY() - randY + kaestchenHoehe) / kaestchenHoehe;
				if (x < 1 || x > kaestchenAnzX)
					x = -1;
				if (y < 1 || y > kaestchenAnzY)
					y = -1;
				mausClick(x, y);
				if (evt.getButton() == MouseEvent.BUTTON1)
					mausLeftClick(x, y);
				if (evt.getButton() == MouseEvent.BUTTON3)
					mausRightClick(x, y);
			}
		});
		jPanel1.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent evt) {
				int x = (-startX + evt.getX() - randX + kaestchenBreite) / kaestchenBreite;
				int y = (-startY + evt.getY() - randY + kaestchenHoehe) / kaestchenHoehe;
				if (x < 1 || x > kaestchenAnzX)
					x = -1;
				if (y < 1 || y > kaestchenAnzY)
					y = -1;
				if (evt.isShiftDown())
					mausShiftMoved(x, y);
				else
					mausMoved(x, y);
			}

			@Override
			public void mouseDragged(MouseEvent evt) {
				int x = (-startX + evt.getX() - randX + kaestchenBreite) / kaestchenBreite;
				int y = (-startY + evt.getY() - randY + kaestchenHoehe) / kaestchenHoehe;
				if (x < 1 || x > kaestchenAnzX)
					x = -1;
				if (y < 1 || y > kaestchenAnzY)
					y = -1;
				if (evt.isShiftDown())
					mausShiftDragged(x, y);
				else
					mausDragged(x, y);
			}
		});
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setBackground(new java.awt.Color(255, 255, 255));
		setSize(fensterbreite + 16, fensterhoehe + 40);
		setTitle("KaestchenV2.0");
		setResizable(false);
		setVisible(true);
		repaint();
	}

	public void farbeSetzen(int x, int y, String farbe) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("farbeSetzen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		matrix[x - 1][y - 1] = gibFarbe(farbe);
		if (autopaint)
			repaint();
	}

	public void farbeSetzen(int x, int y, Color farbe) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("farbeSetzen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		matrix[x - 1][y - 1] = farbe;
	}

	public Color gibFarbe(String farbe) {
		if (farbe.equals("durchsichtig"))
			return farbeDurchsichtig;
		if (farbe.equals("weiß"))
			return Color.WHITE;
		else if (farbe.equals("grün"))
			return Color.GREEN;
		else if (farbe.equals("rot"))
			return Color.RED;
		else if (farbe.equals("blau"))
			return Color.BLUE;
		else if (farbe.equals("schwarz"))
			return Color.BLACK;
		else if (farbe.equals("grau"))
			return Color.GRAY;
		else if (farbe.equals("hellgrau"))
			return Color.LIGHT_GRAY;
		else if (farbe.equals("cyan"))
			return Color.CYAN;
		else if (farbe.equals("orange"))
			return Color.ORANGE;
		else if (farbe.equals("pink"))
			return Color.PINK;
		else if (farbe.equals("gelb"))
			return Color.YELLOW;
		else if (farbe.equals("magenta"))
			return Color.MAGENTA;
		else if (farbe.equals("braun"))
			return new Color(102, 51, 0);
		else if (farbe.equals("dunkelgrün"))
			return new Color(0, 102, 0);
		else
			return Color.BLACK;

	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 * @return
	 */
	public String farbeGeben(int x, int y) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("farbeSetzen(...) wurde außerhalb des Feldes aufgerufen!");
			return "";
		}
		if (matrix[x - 1][y - 1].equals(Color.WHITE))
			return "weiß";
		else if (matrix[x - 1][y - 1].equals(farbeDurchsichtig))
			return "durchsichtig";
		else if (matrix[x - 1][y - 1].equals(Color.GREEN))
			return "grün";
		else if (matrix[x - 1][y - 1].equals(Color.RED))
			return "rot";
		else if (matrix[x - 1][y - 1].equals(Color.BLUE))
			return "blau";
		else if (matrix[x - 1][y - 1].equals(Color.BLACK))
			return "schwarz";
		else if (matrix[x - 1][y - 1].equals(Color.GRAY))
			return "grau";
		else if (matrix[x - 1][y - 1].equals(Color.LIGHT_GRAY))
			return "hellgrau";
		else if (matrix[x - 1][y - 1].equals(Color.CYAN))
			return "cyan";
		else if (matrix[x - 1][y - 1].equals(Color.ORANGE))
			return "orange";
		else if (matrix[x - 1][y - 1].equals(Color.PINK))
			return "pink";
		else if (matrix[x - 1][y - 1].equals(Color.YELLOW))
			return "gelb";
		else if (matrix[x - 1][y - 1].equals(Color.MAGENTA))
			return "gelb";
		else if (matrix[x - 1][y - 1].equals(new Color(102, 51, 0)))
			return "braun";
		else if (matrix[x - 1][y - 1].equals(new Color(0, 102, 0)))
			return "dunkelgrün";
		else
			return "";
	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 */
	public void farbeLoeschen(int x, int y) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("farbeLoeschen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		matrix[x - 1][y - 1] = farbeDurchsichtig;
		if (autopaint)
			repaint();
	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 * @param s
	 *            - Text
	 */
	public void textSetzen(int x, int y, String s) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("textSetzen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		text[x - 1][y - 1] = s;
		if (autopaint)
			repaint();
	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 * @return
	 */
	public String textGeben(int x, int y) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("textGeben(...) wurde außerhalb des Feldes aufgerufen!");
			return "";
		}
		return text[x - 1][y - 1];
	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 */
	public void textLoeschen(int x, int y) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("textLoeschen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		text[x - 1][y - 1] = null;
		if (autopaint)
			repaint();
	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 * @param s
	 *            - dateiname
	 */
	public void bildSetzen(int x, int y, String s) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("bildSetzen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		if (bilder.containsKey(s))
			bild[x - 1][y - 1] = bilder.get(s);
		else
			try {
				Image b = ImageIO.read(new File(s));
				bild[x - 1][y - 1] = b;
				bilder.put(s, b);
			} catch (IOException e) {
				meldung("Bildname unbekannt: " + s);
				System.exit(0);
			}
		if (autopaint)
			repaint();
	}

	/**
	 * @param x
	 *            - von 1 bis Kästchenanzahl
	 * @param y
	 *            - von 1 bis Kästchenanzahl
	 */
	public void bildLoeschen(int x, int y) {
		if ((x < 1 || x > kaestchenAnzX) || (y < 1 || y > kaestchenAnzY)) {
			meldungSystem("bildLoeschen(...) wurde außerhalb des Feldes aufgerufen!");
			return;
		}
		bild[x - 1][y - 1] = null;
		if (autopaint)
			repaint();
	}

	/**
	 * @param text
	 *            - Auszugebender Text in einem eigenen Fenster
	 */
	public void meldung(String text) {
		JOptionPane.showMessageDialog(null, text);
	}

	/**
	 * @param text
	 *            - Auszugebender Text in der Konsole
	 */
	public void meldungSystem(String text) {
		System.out.println(text);
	}

	/**
	 * @param filename
	 *            - Dateiname einer .wav-Datei
	 */
	public void sound(String filename) {
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filename));
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param nr
	 * @param dauer
	 */
	public void tickerStart(int nr, double dauer) {
		if ((nr < 0) || (nr >= 100)) {
			meldung("Tickernummern müssen im Bereich 0 und 99 sein");
			System.exit(-1);
		}
		stepDauer[nr] = dauer;
		ticker[nr] = new Thread(new Runnable() {
			@Override
			public void run() {
				while (stepDauer[nr] != 0) {
					try {
						TimeUnit.MICROSECONDS.sleep((int) (stepDauer[nr] * 1000));
					} catch (InterruptedException e) {}
					if (stepDauer[nr] != 0)
						tick(nr);
				}
			}

		});
		ticker[nr].start();
	}

	/**
	 * @param nr
	 * @param dauer
	 */
	public void tickerDauer(int nr, double dauer) {
		stepDauer[nr] = dauer;
	}

	/**
	 * @param nr
	 */
	public void tickerStop(int nr) {
		stepDauer[nr] = 0;
	}

	/**
	 * speichert die Matrix
	 * 
	 * @param filename
	 */
	public void hintergrundbildSetzen(String filename) {
		try {
			hintergrundbild = ImageIO.read(new File(filename));
		} catch (IOException e) {
			meldung("Bildname unbekannt: " + filename);
			System.exit(0);
		}
		if (autopaint)
			repaint();
	}

	public void speichereMatrix(String filename) {
		speichereMatrix(filename, "alles");
	}

	/**
	 * speichert die Matrix
	 * 
	 * @param filename
	 * @param farbe
	 */
	public void speichereMatrix(String filename, String farbe) {
		try {
			String zeile = "";
			BufferedWriter b = new BufferedWriter(new FileWriter(filename));
			b.write(kaestchenAnzX + " " + kaestchenAnzY);
			b.newLine();
			for (int x = 0; x < kaestchenAnzX; x++) {
				zeile = "";
				for (int y = 0; y < kaestchenAnzY; y++)
					if (farbe.equals("alles"))
						zeile = zeile + matrix[x][y].getRGB() + " ";
					else if (gibFarbe(farbe).equals(matrix[x][y]))
						zeile = zeile + matrix[x][y].getRGB() + " ";
					else
						zeile = zeile + farbeDurchsichtig.getRGB() + " ";
				b.write(zeile);
				b.newLine();
			}
			b.close();
		} catch (IOException e) {
			meldung("Fehler beim Speichern der Matrix!");
		}
	}

	/**
	 * lädt die Matrix aus der angegebenen Datei
	 * 
	 * @param filename
	 */
	public void ladeMatrix(String filename) {
		ladeMatrix(filename, "alles");
	}

	/**
	 * lädt nur eine bestimmte Farbe der Matrix aus der angegebenen Datei
	 * 
	 * @param filename
	 * @param farbe
	 */
	public void ladeMatrix(String filename, String farbe) {
		try {
			BufferedReader b = new BufferedReader(new FileReader(filename));
			String[] a = b.readLine().split(" ");
			int anzX = Integer.parseInt(a[0]);
			int anzY = Integer.parseInt(a[1]);
			if ((anzX != kaestchenAnzX) && (anzY != kaestchenAnzY)) {
				meldung("Matrix wurde nicht geladen! Gespeicherte Matrix passt nicht zur derzeitigen Matrix, gespeicherte Dimensionen sind (" + anzX + "/"
						+ anzY + ")");
				b.close();
				return;
			}
			String[] werte = new String[kaestchenAnzY];
			for (int x = 0; x < kaestchenAnzX; x++) {
				werte = b.readLine().split(" ");
				for (int y = 0; y < kaestchenAnzY; y++) {
					Color c = new Color(Integer.parseInt(werte[y]), true);
					if (farbe.equals("alles"))
						matrix[x][y] = c;
					else if (gibFarbe(farbe).equals(c))
						matrix[x][y] = c;
					else
						matrix[x][y] = farbeDurchsichtig;
				}
			}
			b.close();
		} catch (IOException e) {
			meldung("Fehler beim Laden der Matrix !");
		}
	}

	/**
	 * gibt an, ob repaint() automatisch aufgerufen wird
	 * 
	 * @param autopaint
	 *            - default: true
	 */
	public void setAutopaint(boolean autopaint) {
		this.autopaint = autopaint;
	}

	public void kaestchenInMitteSetzen(int x, int y) {
		startX = fensterbreite / 2 - x * kaestchenBreite;
		startY = fensterhoehe / 2 - y * kaestchenHoehe;
		if (autopaint)
			repaint();
	}

	public void setzeFenstergröße(int breite, int hoehe) {
		fensterbreite = breite;
		fensterhoehe = hoehe;
		setSize(breite, hoehe);
	}

	/**
	 * Achtung: zu überschreibende Methoden, müssen exakt so abgeschrieben
	 * werden!
	 */
	public void mausClick(int x, int y) {}

	public void mausLeftClick(int x, int y) {}

	public void mausRightClick(int x, int y) {}

	public void mausMoved(int x, int y) {}

	public void mausDragged(int x, int y) {}

	public void mausShiftMoved(int x, int y) {}

	public void mausShiftDragged(int x, int y) {}

	public void tasteClick(String taste) {}

	public void tasteClickCtrl(String taste) {}

	public void tick(int nr) {}
}
