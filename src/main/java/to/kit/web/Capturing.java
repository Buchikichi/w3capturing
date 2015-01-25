package to.kit.web;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;

import to.kit.web.w3capturing.Browser;

/**
 * @author H.Sasai
 */
public final class Capturing extends JFrame {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 7476429533398820128L;
	/** DateFormat. */
	private Format format = new SimpleDateFormat("yyyyMMdd-HHmmss");
	/** Browser path. */
	private static final String BROWSER_PATH = "C:/Program Files/Mozilla Firefox/firefox.exe";
	/** CurrentDirectory for JFileChooser. */
	private File defaultDir = null;

	private byte[] effect(byte[] bytes, String url) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
		Graphics2D g = (Graphics2D) image.getGraphics();
		FontMetrics metrics = g.getFontMetrics();
		int height = metrics.getHeight() / 2;

		g.setColor(Color.LIGHT_GRAY);
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				g.drawString(url, x, height + y);
			}
		}
		g.setColor(Color.BLACK);
		g.drawString(url, 1, height + 1);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", out);
		return out.toByteArray();
	}

	protected void saveFile(byte[] bytes, String url) {
		JFileChooser chooser = new JFileChooser();
		String filename = "capture" + this.format.format(new Date()) + ".png";

		chooser.setCurrentDirectory(this.defaultDir);
		chooser.setSelectedFile(new File(filename));
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = chooser.getSelectedFile();
		this.defaultDir = chooser.getCurrentDirectory();
		try {
			byte[] effected = effect(bytes, url);
			FileUtils.writeByteArrayToFile(file, effected);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Capturing() {
		final Browser browser = Browser.getInstance();
		setTitle("Capture browsing image.");
		setBounds(10, 10, 640, 120);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
		JPanel panel = new JPanel();
		final JTextField textField = new JTextField(40);
		JButton button = new JButton("Capture");

		panel.add(textField);
		panel.add(button);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = textField.getText();

				browser.get(url);
				saveFile(browser.getScreenshot(), url);
			}
		});
		add(panel);
		browser.setBrowserPath(BROWSER_PATH);
	}

	@Override
	public void dispose() {
		Browser browser = Browser.getInstance();

		browser.destroy();
		super.dispose();
	}

	public static void main(String[] args) {
		new Capturing().validate();
	}
}
