package com.parallel.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.parallel.listeners.CustomAssertion;
import com.parallel.utils.FrameworkConstant;
import com.parallel.utils.TestUtil;

public class TestBase {

	public static Properties prop = new Properties();
    public static File folder;
    static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
    protected CustomAssertion assertTestStep = new CustomAssertion();

    public TestBase() {

        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(FrameworkConstant.PROPERTYFILE_PATH);
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RemoteWebDriver getDriver() {
        return driver.get();
    }

    public void setDriver(RemoteWebDriver driverref) {
        driver.set(driverref);
    }

    public void closeBrowser() {
        driver.get().quit();
        // driver.remove();
    }

    public void initialization(String browserType, String app) {
 //       folder = new File(UUID.randomUUID().toString());
//        folder.mkdir();
//        String runLocation = System.getProperty("runenv").toString().equals("hub1")
//                ? prop.getProperty("HUB_URL1").toString()
//                : System.getProperty("runenv").toString().equals("hub2") ? prop.getProperty("HUB_URL2").toString()
//                        : System.getProperty("runenv").toString().equals("hub3")
//                                ? prop.getProperty("HUB_URL3").toString()
//                                : System.getProperty("runenv").toString().equals("hub4")
//                                        ? prop.getProperty("HUB_URL4").toString()
//                                        : "NA";

        switch (browserType) {
        case "Chrome":

            System.out.println("Launching google chrome with new profile..");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-save-password-bubble");
            chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
//            chromeOptions.addExtensions(new File(FrameworkConstant.CSP_EXTENTION));
            DesiredCapabilities capability = new DesiredCapabilities();
            capability.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_settings.popups", 0);
//            prefs.put("download.default_directory", folder.getAbsolutePath());
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", prefs);
            try {
//                if (!runLocation.equals("NA")) {
 //                  setDriver(new RemoteWebDriver(new URL("http://192.168.1.22:4444"), capability));
//                    getDriver().setFileDetector(new LocalFileDetector());
//                } else {
                  setDriver(new ChromeDriver(chromeOptions));
  //              }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;

        case "Firefox":

            System.out.println("Launching Firefox browser..");
            FirefoxOptions options = new FirefoxOptions();
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.download,dir", folder.getAbsolutePath());
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                    "image/png, application/pdf, application/csv, application/xls, application/xlsx");
            profile.setPreference("pdfjs.disabled", true);
            options.setProfile(profile);
            setDriver(new FirefoxDriver(options));
            break;

        case "headless":

            System.out.println("Launching google chrome headless with new profile..");
            ChromeOptions options1 = new ChromeOptions();
            options1.addArguments("window-size=1400,800");
            options1.addArguments("headless");
            setDriver(new ChromeDriver(options1));
            break;

        case "Edge":

            System.out.println("Launching Microsoft Edge with new profile..");
            HashMap<String, Object> edgePrefs = new HashMap<String, Object>();
            edgePrefs.put("download.default_directory", folder.getAbsolutePath());
            EdgeOptions opt = new EdgeOptions();
            opt.setExperimentalOption("prefs", edgePrefs);
            setDriver(new EdgeDriver());
            break;

        default:
            System.out.println("browser : " + browserType + " is invalid, Launching Chrome as browser of choice..");
            System.out.println("Launching google chrome with new profile..");
            chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            chromeOptions.addExtensions(new File(FrameworkConstant.CSP_EXTENTION));
            DesiredCapabilities capability1 = new DesiredCapabilities();
            capability1.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            Map<String, Object> prefs1 = new HashMap<String, Object>();
            prefs1.put("profile.default_content_settings.popups", 0);
//            prefs1.put("download.default_directory", folder.getAbsolutePath());
            prefs1.put("credentials_enable_service", false);
            prefs1.put("profile.password_manager_enabled", false);
            chromeOptions.setExperimentalOption("prefs", prefs1);
           try {
//                if (!runLocation.equals("NA")) {
//                    setDriver(new RemoteWebDriver(new URL(runLocation), capability1));
//                } else {
                    setDriver(new ChromeDriver(chromeOptions));
 //               }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(FrameworkConstant.PAGE_LOAD_TIMEOUT));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstant.IMPLICIT_WAIT));
        if (app.contains("TEST")) {
            getDriver().navigate().to(prop.getProperty("URL"));
        } else {
            getDriver().navigate().to(prop.getProperty("URL"));
        }
    }

    public void pipeLineinitialization(String browserName, String app) {
        if (browserName.equalsIgnoreCase("Chrome")) {
            System.out.println("Webdriver started");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("window-size=1024,768");
            options.addArguments("--no-sandbox");
            setDriver(new ChromeDriver(options));
        }
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        if (app.contains("TEST")) {
            getDriver().navigate().to(prop.getProperty("URL"));
        } else {
            getDriver().navigate().to(prop.getProperty("URL"));
        }
    }

    public void tearDown() {
        closeBrowser();
        // Delete the download folder
//      try {
//          TestUtil.deleteDownloadfolder();
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
    }

    public static void setPropertyValue(String Key, String value, String config_path)
            throws ConfigurationException, IOException {
        PropertiesConfiguration config = new PropertiesConfiguration(config_path);
        config.setProperty(Key, value);
        config.save();
        FileInputStream file = new FileInputStream(config_path);
        prop.load(file);
    }

    public static Object fetchPropertyValue(String Key, String config_path) throws IOException {
        FileInputStream file = new FileInputStream(config_path);
        Properties property = new Properties();
        property.load(file);
        return property.getProperty(Key);
    }

}
