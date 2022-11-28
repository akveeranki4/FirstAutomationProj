package automationLib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.WebDriverWait;

import stepdefinition.stepdefinition;
import automationLib.Driver;
import utils.ErrorLogger;
import utils.SeleniumCustomServlets;
import utils.Utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

public class Driver {

	static Logger log = Logger.getLogger(Driver.class);

	public static WebDriver pgDriver;
	public static String drType;
	WebDriverWait wait;
	static ErrorLogger errLogger = new ErrorLogger();

	public static SeleniumCustomServlets customServlet = null;
	static Utilities comnutils;
	static int count=0;

	public static WebDriver getPgDriver() {

		return pgDriver;

	}

	public static void setPgDriver(String brsrType, String runtype,String ip ) throws Exception {

		if (runtype.equalsIgnoreCase("Local")) {

			if (brsrType.equalsIgnoreCase("chrome")) {

				File file = new File("webdrivers//chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				System.setProperty("webdriver.chrome.driver","webdrivers//chromedriver.exe" );

				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("useAutomationExtension", false);
				capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				log.info("Chrome browser cache cleared successfully");
				
				options.addArguments("--start-maximized");
				options.addArguments("--disable-web-security");
				options.addArguments("--no-proxy-server");

				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);

				options.setExperimentalOption("prefs", prefs);


				//ChromeOptions options = new ChromeOptions();
				//options.setExperimentalOption("useAutomationExtension", false);
				options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

				WebDriver chromeDriver = new ChromeDriver(options);
				pgDriver = chromeDriver;
				drType = "chrome";
				pgDriver.manage().window().maximize();

			} else if(brsrType.equalsIgnoreCase("firefox")) {

				WebDriver fireFoxDriver = new FirefoxDriver();
				pgDriver = fireFoxDriver;
				drType = "firefox";

			} else if (brsrType.equalsIgnoreCase("ie")) {

				File file = new File("webdrivers//IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

				try {

					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					@SuppressWarnings("deprecation")
					WebDriver ieDriver = new InternetExplorerDriver(capabilities);
					pgDriver = ieDriver;
					drType = "ie";

				} catch(Exception e) {

					count++;

					errLogger.logError("Exception occurred: " + e);

					try {
						//Runtime.getRuntime().exec("taskkill /im IEDriverServer.exe /f");
						//Runtime.getRuntime().exec("taskkill /im iexplore.exe /f");
					} catch(Exception e1) {
						log.info("Exception: " + e1);
					}

					drType = "ie";
					if (count<=3) setPgDriver(brsrType,  runtype, ip);

				}

			}

		} else if (runtype.equals("Grid")) {

			if (brsrType.equalsIgnoreCase("chrome")) {

				try {

					log.info("STARTING HUB...");

					log.info("ip = " + ip);

					DesiredCapabilities capability = DesiredCapabilities.chrome();
					ChromeOptions options = new ChromeOptions();
					options.setExperimentalOption("useAutomationExtension", false);
					capability.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
					capability.setCapability(ChromeOptions.CAPABILITY, options);

					log.info("Chrome browser cache cleared successfully");

					pgDriver = new RemoteWebDriver(new URL("http://" + ip + "/wd/hub"), capability);

					log.info("Remote WebDriver instance is created.");

					pgDriver.manage().window().maximize();

					log.info("Window is maximized");

					pgDriver.manage().deleteAllCookies();

					log.info("Deleted cookies from chrome browser");

					log.info("HUB STARTED.");

				} catch (TimeoutException e) {

					count++;

					log.info("Exception Cause = " + e.getMessage());
					log.info("Exception Thrown..." + e.toString());
					log.info("Timeout Exception is thrown...Re-Try Count = " + count);

					if (count <= 3) {
						quit();
						setPgDriver(brsrType, runtype,ip );
						stepdefinition.logger.info("Trying " + count + "time");

					} else {

						stepdefinition.isServicedown=true;
						extentmanager.ExtentManager.setTeststatus("Warning-Browser Issue");
						errLogger.logremoteNotconnectedError(e);
						quit();

					}

				} catch (NoSuchSessionException e) {

					count++;

					log.info("Exception Cause = " + e.getMessage());
					log.info("Exception Thrown..." + e.toString());
					log.info("No Such Session Exception is thrown...Re-Try Count = " + count);

					if (count <= 3) {
						quit();
						setPgDriver(brsrType, runtype,ip );
						stepdefinition.logger.info("Trying " + count + "time");

					} else {

						stepdefinition.isServicedown=true;
						extentmanager.ExtentManager.setTeststatus("Warning-Browser Issue");
						errLogger.logremoteNotconnectedError(e);
						quit();

					}

					e.printStackTrace();

				} catch (Exception e) {

					stepdefinition.isServicedown=true;
					extentmanager.ExtentManager.setTeststatus("Warning");
					errLogger.logremoteNotconnectedError(e);
					quit();

					e.printStackTrace();
				}
			}

			if (brsrType.equalsIgnoreCase("ie")) {

				try {

					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability("ie.ensureCleanSession", true);
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
					pgDriver = new RemoteWebDriver(new URL("http://" + ip + "/wd/hub"), capabilities);

				} catch (NoSuchSessionException e) {

					count++;

					if (count <= 3) {

						setPgDriver(brsrType, runtype,ip );
						stepdefinition.logger.info("Trying " + count + "time");

					} else {

						stepdefinition.isServicedown=true;
						extentmanager.ExtentManager.setTeststatus("Warning-Browser Issue");
						errLogger.logremoteNotconnectedError(e);

					}

					e.printStackTrace();
				}
			}

			try {

				log.info("Retrieving session id information...");
				SessionId session = ((RemoteWebDriver) Driver.pgDriver).getSessionId();
				log.info("Session id: " + session.toString());
				String hubIP = ip.split(":")[0];
				int hubPort = Integer.parseInt(ip.split(":")[1]);
				log.info("Hub IP = " + hubIP);
				log.info("Hub Port = " + hubPort);
				getNodeInfoForSession(hubIP, hubPort, session); 
				log.info("Session ID information is retrieved.");

			} catch (Exception ex) {

				log.info("Exception Cause = " + ex.getCause());
				log.info("Exception = " + ex.toString());
				log.info("Exception Thrown while retrieving the Session ID information.");

			}

		} else if (runtype.equals("Mobile")) {

			File file = new File("webdrivers//chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			System.setProperty("webdriver.chrome.driver","webdrivers//chromedriver.exe" );

			Map<String, String> mobileEmulation = new HashMap<String, String>();
			mobileEmulation.put("deviceName", "Nexus 5");

			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
			WebDriver driver = new ChromeDriver(chromeOptions);						

			pgDriver = driver;
			drType = "chrome";

		} else if (runtype.equals("API")) {

			log.info("API");

		}	

	}

	public static void setPgDriver(String brsrType) throws Exception {	

		if (brsrType.equalsIgnoreCase("chrome")) {

			File file = new File("webdrivers//chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			System.setProperty("webdriver.chrome.driver","webdrivers//chromedriver.exe" );

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("useAutomationExtension", false);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

			WebDriver chromeDriver = new ChromeDriver(options);
			pgDriver = chromeDriver;
			drType = "chrome";

		} else if (brsrType.equalsIgnoreCase("firefox")) {

			WebDriver fireFoxDriver = new FirefoxDriver();
			pgDriver = fireFoxDriver;
			drType = "firefox";

		} else if (brsrType.equalsIgnoreCase("ie")) {

			File file = new File("webdrivers//IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

			try {

				WebDriver ieDriver = new InternetExplorerDriver();
				pgDriver = ieDriver;
				drType = "ie";

			} catch(Exception e) {

				WebDriver ieDriver = new InternetExplorerDriver();
				pgDriver = ieDriver;
				drType = "ie";

			}

		} else if (brsrType.equals("Remote")) {

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			pgDriver = new RemoteWebDriver(new URL("http://30.142.9.59:4444/wd/hub"), capabilities);

		}

	}

	public static void openSIT() {

		Driver.getPgDriver().get("https://solutioncentral.sit.va.antheminc.com/public/login.html");

	}

	public static void openUAT() {

		Driver.getPgDriver().get("https://solutioncentral.uat.va.antheminc.com/public/login.html");

	}

	public static void killCloseDriver() {
		pgDriver.close();

		if (drType.equalsIgnoreCase("chrome")) {
			//	WindowsUtils.killByName("chromedriver.exe");
		} else if(drType.equalsIgnoreCase("ie")) {

			try {
				//WindowsUtils.killByName("IEDriverServer.exe");
			} catch(Exception e) {
				log.info(e);
			}
		} else {
		}

	}

	public static void setUrl(String url) throws Exception {

		Driver.getPgDriver().get(url);
		Driver.getPgDriver().manage().window().maximize();
		String str = Driver.getPgDriver().getCurrentUrl();
		log.info("The current URL is " + str);

	}

	public static void setPgDriverHeadless(String brsrType) throws Exception {

		if (brsrType.equalsIgnoreCase("chrome")) {

			File file = new File("webdrivers//chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			System.setProperty("webdriver.chrome.driver","webdrivers//chromedriver.exe" );

			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");			
			options.setExperimentalOption("useAutomationExtension", false);
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

			WebDriver chromeDriver = new ChromeDriver(options);
			pgDriver = chromeDriver;
			drType = "chrome";

		} else if (brsrType.equalsIgnoreCase("firefox")) {

			WebDriver fireFoxDriver = new FirefoxDriver();
			pgDriver = fireFoxDriver;
			drType = "firefox";

		} else if (brsrType.equalsIgnoreCase("ie")) {

			File file = new File("webdrivers//IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

			try {

				WebDriver ieDriver = new InternetExplorerDriver();
				pgDriver = ieDriver;
				drType = "ie";

			} catch(Exception e) {

				WebDriver ieDriver = new InternetExplorerDriver();
				pgDriver = ieDriver;
				drType = "ie";

			}
		}

	}


	private static void getNodeInfoForSession(String hubHostName, int hubPort, SessionId sessionID) {

		String nodeHostName = null;
		int nodePort = 0;

		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;

		try {
			
			log.info("Session ID : " + sessionID.toString());

			URL url = new URL("http://" + hubHostName + ":" + hubPort + "/grid/api/testsession?session=" + sessionID.toString());
			BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("GET", url.toExternalForm());
			response = client.execute(new HttpHost(hubHostName, hubPort), r);

			log.info("Get Node Session Info Response : " + System.lineSeparator() + response.toString());

			JsonObject object = extractJson(response.getEntity());
			URL tempUrl = new URL(object.get("proxyId").getAsString());
			nodeHostName = tempUrl.getHost();
			nodePort = tempUrl.getPort();
			
		} catch (Exception e) {
			
			log.error("Exception thrown while acquiring remote webdriver node and port info. Root cause  : " + e.toString());
			
		} finally {
			
			try {
				
				if (response != null) {
					response.close();
				}
				
			} catch (IOException e) {
				
				log.error("Exeption thrown while closing the response object for getting node session info : " + e.toString());
				
			}
		}

		log.info("Session ID : " + sessionID);
		log.info("Node Host Name : " + nodeHostName);
		log.info("Port : " + nodePort);

	}

	@SuppressWarnings("deprecation")
	private static JsonObject extractJson(HttpEntity entity) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()))) {
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				builder.append(line.trim());
			}
			return new JsonParser().parse(builder.toString().trim()).getAsJsonObject();
		}
	}

	public static void quit() {
		
		log.info("Quit Driver...");

		try {
			
			pgDriver.close();
			
			log.info("Driver Closed.");
			
		} catch (Exception ex) {
			
			log.warn("Exception in closing." + System.lineSeparator() + ex.toString());
			
		}

		try {
			
			pgDriver.quit();
			
			log.info("Driver Quit.");
			
		} catch (Exception ex) {
			
			log.warn("Exception in quitting." + System.lineSeparator() + ex.toString());
			
		}
	}

}