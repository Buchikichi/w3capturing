package to.kit.web;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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

	protected void saveFile(File srcFile) {
		JFileChooser chooser = new JFileChooser();
		String filename = "capture" + this.format.format(new Date()) + ".png";

		chooser.setCurrentDirectory(null);
		chooser.setSelectedFile(new File(filename));
		if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		try {
			Files.move(srcFile.toPath(), chooser.getSelectedFile().toPath());
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
			public void actionPerformed(ActionEvent e) {
				String url = textField.getText();

				browser.get(url);
				saveFile(browser.getScreenshot());
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

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		new Capturing();
	}
}
