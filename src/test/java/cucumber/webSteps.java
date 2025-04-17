package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static dataProviders.datasetFileReader.getDatasetValue;
import static org.junit.Assert.*;

import static dataProviders.configFileReader.getPropertyValue;
import static dataProviders.repositoryFileReader.findElementRepo;
import static dataProviders.repositoryFileReader.constructElement;

public class webSteps {

    WebDriver driver;
    private Scenario scenario;
    private WebDriverWait wait;

    @Before
    public void setup(Scenario scenario){

        String browser = getPropertyValue("browser");
        switch (browser) {
            case "chrome":
                driver = WebDriverManager.chromedriver().create();
                break;

            case "firefox":
                driver = WebDriverManager.firefoxdriver().create();
                break;

            default:
                throw new RuntimeException("Browser is not supported");
        }

        scenario.log("Scenario executed on "+browser+" browser");
        this.scenario = scenario;
    }

    @After
    public void afterScenario(Scenario scenario) throws IOException {

        if(scenario.isFailed()){
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            byte[] fileContent = FileUtils.readFileToByteArray(screenshot);
            scenario.attach(fileContent, "image/png", "Failed step screenshot");
        }

        driver.quit();
    }

    @Given("I have opened the system")
    public void iHaveOpenedTheSystem() {
        driver.get(getPropertyValue("baseURL"));
    }

    @When("I set the browser to full screen")
    public void iSetTheBrowserToFullScreen() {
        driver.manage().window().fullscreen();
    }

    @When("I type {string} to the {string}")
    public void type(String text, String locator) throws InterruptedException {
        By xpath = constructElement(findElementRepo(locator));
        WebElement inputField = driver.findElement(xpath);

        inputField.click();

        String existingValue = inputField.getAttribute("value");
        if (existingValue != null && !existingValue.isEmpty()) {
            inputField.clear();
            inputField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        }

        inputField.sendKeys(text);
        waiting();
    }


    @And("I click on {string}")
    public void iClickOn(String locator) {
        By element = constructElement(findElementRepo(locator));
        driver.findElement(element).click();
    }


    @And("I wait few seconds")
    public void waiting() throws InterruptedException {
        Thread.sleep(2000);
    }

}
