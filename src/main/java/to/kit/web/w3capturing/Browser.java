package to.kit.web.w3capturing;

import java.io.File;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * @author H.Sasai
 */
public final class Browser {
	private static final int BROWSER_WIDTH = 900;
	/** Only one instance. */
	private static Browser me = new Browser();
	/** Browser path. */
	private String browserPath;
	/** Selenium WebDriver. */
	private WebDriver driver;

	@SuppressWarnings("unused")
	private WebDriver useChrome() {
		System.setProperty("webdriver.chrome.driver", "C:/application/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.setBinary("C:/Program Files/Google/Chrome/Application/chrome.exe");
		WebDriver result = new ChromeDriver(options);
		WebDriver.Window window = result.manage().window();
		Dimension dimension = window.getSize();

		window.setPosition(new Point(0, 0));
		window.setSize(new Dimension(BROWSER_WIDTH, dimension.getHeight())); // SXGA
		return result;
	}

	private WebDriver useFirefox() {
		File file = new File(this.browserPath);
		FirefoxBinary bin = new FirefoxBinary(file);
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.popups.showPopupBlocker", "true");
		profile.setPreference("browser.sessionhistory.max_entries", 0);
		profile.setPreference("browser.sessionhistory.max_total_viewers", 0);
		profile.setPreference("places.history.enabled", "false");
		profile.setPreference("browser.privatebrowsing.autostart", true);
		WebDriver result = new FirefoxDriver(bin, profile);
		WebDriver.Window window = result.manage().window();
		Dimension dimension = window.getSize();

		window.setPosition(new Point(0, 0));
		window.setSize(new Dimension(BROWSER_WIDTH, dimension.getHeight())); // SXGA
		return result;
	}

	protected WebDriver getDriver() {
		if (this.driver == null) {
			this.driver = useFirefox();
		}
		return this.driver;
	}

	public void get(String url) {
		getDriver().get(url);
	}

	public File getScreenshot() {
		TakesScreenshot takesScreenshot = ((TakesScreenshot) getDriver());
		return takesScreenshot.getScreenshotAs(OutputType.FILE);
	}

	public void destroy() {
		if (this.driver != null) {
			this.driver.close();
		}
	}

	public void setBrowserPath(String browserPath) {
		this.browserPath = browserPath;
	}

	public static Browser getInstance() {
		return me;
	}
}
