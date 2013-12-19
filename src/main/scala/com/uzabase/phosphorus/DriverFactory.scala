package com.uzabase.phosphorus

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL
import org.openqa.selenium.ie.InternetExplorerDriver

class DriverFactory {
	def create: WebDriver = {
		if (Config().isChrome) {
			System.setProperty("webdriver.chrome.driver", Config().chromeDriverUrl)
			val options = new ChromeOptions
			if (Config().isLang)
				options.addArguments("--lang=" + Config().lang)
			if (Config().isRemote)
				return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeCapabilities(options));
			return new ChromeDriver(options)
		} else if (Config().isIE) {
			val cap = new DesiredCapabilities();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			System.setProperty("webdriver.ie.driver", Config().ieDriverUrl);
			cap.setJavascriptEnabled(true)
			return new InternetExplorerDriver(cap)
		} else {
			if (Config().isRemote) {
				val capability = DesiredCapabilities.firefox();
				new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
			} else {
				new FirefoxDriver
			}
		}
	}

	private def chromeCapabilities(options: ChromeOptions): DesiredCapabilities = {
		val capability = DesiredCapabilities.chrome()
		capability.setCapability(ChromeOptions.CAPABILITY, options)
		capability.setCapability("webdriver.chrome.driver", Config().chromeDriverUrl)
		capability.setCapability("chrome.binary", Config().chromeBinary)
		capability
	}
}
object DriverFactory {
	def apply() = new DriverFactory
}