package runner;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import extentmanager.ExtentManager;

@RunWith(Cucumber.class)
@CucumberOptions(
				plugin = {"json:Destination/cucumber.json"},
				features = "Regression",
				glue = {"stepdefinition"},
				tags = {"@Test","@Test"}
				)

public class MainDriverTest {
	
	static Logger log = Logger.getLogger(MainDriverTest.class);
	
	private static Logger rootLogger;
	
	static Date date=new Date();
	
	public static String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
	public static String testExecutionRecords = "C:\\SolutionCentralEngine\\ExtentReports\\" + fileName + "\\";
	public static String logName = testExecutionRecords + "Log" + "\\" + fileName + ".log";
	public static String reportName = testExecutionRecords + "DetailedReport" + "\\" + fileName + ".html";;
	
	@BeforeClass
	public static void setUp() {
		
		initializeConsoleLog();
		
		if (System.getProperty("TEST_EXECUTION_REPORT") != null && !System.getProperty("TEST_EXECUTION_REPORT").isEmpty() && System.getProperty("TEST_EXECUTION_REPORT").trim().length() > 0) {

			log.info("Environment Variable TEST_EXECUTION_REPORT is set.");

			fileName = System.getProperty("TEST_EXECUTION_REPORT");				
			testExecutionRecords = "TestExecutionRecords" + System.getProperty("file.separator") + fileName + System.getProperty("file.separator");

			reportName = testExecutionRecords + "DetailedReport" + System.getProperty("file.separator") + fileName + ".html";
			logName = testExecutionRecords + "Log" + System.getProperty("file.separator") + fileName + ".log";

		}
		
		log.info("Creating Directories...");
		createDirectories();
		log.info("Directories Created.");

		
		log.info("Test Execution Report Name : " + reportName);
		
		ExtentManager.createInstance(reportName);
		
		log.info("@BeforeClass is completed.");
		
	} // --- setUp
	

	
	@AfterClass
	public static void wrapUp() {
		
		log.info("@AfterClass is executing...");
		
		if (System.getProperty("TEST_EXECUTION_REPORT") == null || System.getProperty("TEST_EXECUTION_REPORT").isEmpty() || System.getProperty("TEST_EXECUTION_REPORT").trim().length() == 0) {
			log.info("Copying Screenshots");
			
			String srcDir = "C:\\SolutionCentralEngine\\Screenshots";
			String destDir = "C:\\SolutionCentralEngine\\ExtentReports\\" + fileName + "\\Screenshots";
			
			try {
				Files.move(new File(srcDir).toPath(), new File(destDir).toPath(), StandardCopyOption.REPLACE_EXISTING);
				log.info("Screenshots Copied");
			} catch (Exception ex) {
				log.info("Copy Screenshots Exception" + System.lineSeparator() + ex.toString());
			}
			
		}
		
		log.info("@AfterClass is executed.");
		
	} // --- wrapUp
	
	private static void createDirectories() {
		
		log.info("Test Execution Records = '" + testExecutionRecords + "'");
		
		log.info("Creating directory for Test Execution Records...");
		
		String detailedReport = testExecutionRecords + "DetailedReport";
		String summaryReport = testExecutionRecords + "SummaryReport";
		String jsonFolder = testExecutionRecords + "JSON"; 
		String screenshotsFolder = testExecutionRecords + "Screenshots";
		String logFolder = testExecutionRecords + "Log";

		new File (detailedReport).mkdirs();
		new File (summaryReport).mkdirs();
		new File (jsonFolder).mkdirs();
		new File (logFolder).mkdirs();
		new File (screenshotsFolder).mkdirs();
				
		initializeLog();
		
		log.info("Detailed Report = " + detailedReport);
		log.info("Summary Report = " + summaryReport);
		log.info("JSON Folder = " + jsonFolder);
		log.info("Log Folder = " + logFolder);
		log.info("Screenshots = " + screenshotsFolder);
				
		log.info("Directory for Test Execution Records is created.");		
		log.info("Test Execution Report Name : " + reportName);
		log.info("Test Execution Log Name : " + logName);
		
	} // --- createDirectories
	
	private static void initializeLog() {

		PatternLayout fileLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L %C.%M - %m%n");
		
		FileAppender fileAppender = new FileAppender();
		fileAppender.setLayout(fileLayout);
		fileAppender.setFile(logName);
		fileAppender.activateOptions();
		rootLogger.addAppender(fileAppender);
		
	} // --- initializeLog
	
	private static void initializeConsoleLog() {
		
		rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		
		PatternLayout consoleLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L %C.%M - %m%n");

		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(consoleLayout);
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
		consoleAppender.activateOptions();
		rootLogger.addAppender(consoleAppender);
		
	}
} // --- MainDriverTest	
				