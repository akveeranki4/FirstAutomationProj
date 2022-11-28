package stepdefinition;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import automationLib.Driver;
import config.configuration;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;
import extentmanager.ExtentManager;
//import extentmanager.ExtentManager;
import utils.BaseLogger;
import utils.CustomException;
import utils.ErrorLogger;
import utils.ExecutionEngine;
import utils.SeleniumUtilities;
import utils.Utilities;

public class stepdefinition {

	static Logger log = Logger.getLogger(stepdefinition.class);

	ExecutionEngine exec = new ExecutionEngine();
	ExtentReports reports = ExtentManager.getInstance();
	ErrorLogger err = new ErrorLogger();
	BaseLogger blogger = new BaseLogger();
	SeleniumUtilities utils;
	Utilities comnutils;
	CustomException exptn;

	WebDriver driver = Driver.getPgDriver();
	Actions action;

	DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	Date date = new Date();

	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(date);
	public String name = "";
	public static ArrayList<String> screenshotpath = new ArrayList<String>();
	public static ExtentTest logger;

	public static long startTime, endtime;
	public static int executionTime;
	public static boolean isServicedown = false;
	public static boolean isDataIssue = false;

	/**
	 * @param scenario
	 * @throws Exception
	 */
	@Before
	public void before(Scenario scenario) throws Exception {

		log.info("SCENARIO : " + scenario.getId() + " - " + scenario.getStatus() + " STARTED");

		isServicedown = false;
		logger = blogger.completeBeforeScenariologging(scenario);

		this.comnutils = new Utilities();

		boolean defectflag = false;

		String browser = comnutils.getPropertyvalue("browser");
		String runtype = comnutils.getPropertyvalue("runtype");
		String ip = "Local";

		if (!defectflag) {

			if (runtype.equalsIgnoreCase("Grid")) {
				ip = comnutils.getPropertyvalue("ip");
			}

			Driver.setPgDriver(browser, runtype, ip);

			if (runtype.equalsIgnoreCase("Grid")) {
				log.info("Scenario ID = '" + scenario.getId() + "'");
				log.info("Scenario Name = '" + scenario.getName() + "'");
				log.info("Scenario Status = '" + scenario.getStatus() + "'");
			}

			this.utils = new SeleniumUtilities();

		}

	}


	@After
	public void afterScenario(Scenario scenario) throws Exception {

		log.info("#SCENARIOSTATUS# - Scenario ID : " + scenario.getId());
		log.info("#SCENARIOSTATUS# - Scenario Name : " + scenario.getName());
		log.info("#SCENARIOSTATUS# - Scenario Status : " + scenario.getStatus());
		log.info("#SCENARIOSTATUS# - Scenario String : " + scenario.toString());
		log.info("#SCENARIOSTATUS# - EXECUTION COMPLETED");

		log.info("#SCENARIOEXESTATUS# - ~" + scenario.getStatus().toUpperCase() + "~" + scenario.getName() + "~"
				+ configuration.getFeatureFileName());

		blogger.completeAfterScenariologging(scenario);

	}

	@When("^(.*) \"([^\"]*)\" ((?!data).)* \"([^\"]*)\"(.*)$")
	//And I call the "createNewInteractionResearchmember" method on the "Header" page
	public void executeMethod(String whatever, String methodname, String Whtever, String classname, String whtever)  throws Throwable {

		log.info(" without data");

		String loginfo= whatever+methodname.substring(0);

		utils.logMethodResultAndAttachScreenShot(methodname, classname, loginfo,exec.executeMethod("automationLib."+classname, methodname));

	}

	@When("^(.*) \"([^\"]*)\" (.*data) \\(([^\"]*)\\) (.*) \"([^\"]*)\"(.*)$")
	public void executeMethod(String whatever, String methodname, String wteverbeforedata, String arlistconvert, String wtverbeforepage, String classname, String whatevaftrpage) throws IOException, Throwable  {

		log.info("Data");

		String[] arlist=arlistconvert.split(",");
		String loginfo= whatever+methodname.substring(0);

		utils.logMethodResultAndAttachScreenShot(methodname, classname, loginfo,exec.executeMethod("automationLib."+classname, methodname, arlist));

	}

	//Phase 2 work in progress
	@When("(.*data) \\(([^\"]*)\\)([^.*:)\"]*)([^\"]*)$")
	//@When("^([^\"]*)(.*data) \\(([^\"]*)\\)([^.*:)\"]*)([^\"]*)$")
	//(username,password,key1:value1;key2:value2,dropdown)
	public void execute(String wtver, String arlistconvert, String wat, String methodname) throws Throwable{

		log.info("Entry");
		String[] arlist=arlistconvert.split(",");
		String page = methodname.substring(methodname.indexOf("...")+3, methodname.indexOf(":"));
		String method = methodname.substring(methodname.indexOf(":")+1);
		String loginfo = wtver+arlistconvert+methodname.substring(0, methodname.indexOf("..."));

		log.info("Page: " + page + "method: " + method);
		utils.logMethodResultAndAttachScreenShot(method, page, loginfo, exec.executeMethod("automationLib." + page, method, arlist));

	}


	//@When("^([^\"]*) ([:]*)$")
	//@When("^([^\"]*) ([:]*)([^\"]*)$")
	//@When("(.*data)([^.*:)\"]*)([^\"]*)$")
	//@When("/^((?!data).)*$/ ([^.*:)\"]*)([^\"]*)$")
	@When("^([^\"\\(]*)$")
	//@When("^([^\"\\(]*) ([^.*:)\"]*)([^\"]*)$")
	//@When("(.*:)([^\"]*)$")
	public void execute(String methodname) throws Throwable{

		if (methodname.contains("application is opened")) {
			log.info("Skipping application is opened on the browser"); 
		} else if (methodname.contains("testcase is passed")) {
			log.info("Testcase passed");
		} else {
			log.info("Entry new");

			String page = methodname.substring(methodname.indexOf("...")+3, methodname.indexOf(":"));
			String method = methodname.substring(methodname.indexOf(":")+1);
			String loginfo = methodname.substring(0, methodname.indexOf("..."));

			log.info("Page " + page + "method  " + method);
			utils.logMethodResultAndAttachScreenShot(method, page, loginfo, exec.executeMethod("automationLib." + page, method));

		}

	}

}
