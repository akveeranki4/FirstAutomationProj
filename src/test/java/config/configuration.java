package config;

public class configuration {

	public static String screenshotsFolder = "";
	public static String wordFolder = "";
	private static String featureFileName;

	public static class url {

		public static String app_url = System.getProperty("TEST_URL");
		public static String tagName = System.getenv("bamboo_TEST_CUCUMBER_SCENARIO_TAG");
		public static String runtype = "local";

	}

	public static void setFeatureFileName(String featureFileName) {
		configuration.featureFileName = featureFileName;
	}

	public static String getFeatureFileName() {
		return configuration.featureFileName;
	}

	public static class browser {

		public static String browser = "chrome";// System.getenv("bamboo_browser");
	}

	public static final String CucumberTag = System.getenv("bamboo_TC_type");


	public static void setScreentshotsFolder(String screenshotsFolder) {
		configuration.screenshotsFolder = screenshotsFolder;
	}

	public static String getScreenshotsFolder() {
		return configuration.screenshotsFolder;
	}

}