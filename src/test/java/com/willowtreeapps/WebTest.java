package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebTest {

    private WebDriver driver;

    /**
     * Change the prop if you are on Windows or Linux to the corresponding file type
     * The chrome WebDrivers are included on the root of this project, to get the
     * latest versions go to https://sites.google.com/a/chromium.org/chromedriver/downloads
     */


    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.mac");
        Capabilities capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        driver.navigate().to("http://www.ericrochester.com/name-game/");
    }

    @Test
    public void test_validate_title_is_present() {
        new HomePage(driver)
                .validateTitleIsPresent();
    }

    @Test
    public void test_clicking_photo_increases_tries_counter() {
        new HomePage(driver)
                .validateClickingFirstPhotoIncreasesTriesCounter();
    }

    @Test
    public void test_streak_counter_incremented() {
        new HomePage(driver)
                .validateClickingCorrectPhotoIncreasesStreakCounter();
    }

    @Test
    public void test_streak_counter_resets() {
        new HomePage(driver)
                .validateStreakCounterResetsAfterIncorrectAnswer();
    }

    @Test
    public void test_counters_ten_random_tries() {
        new HomePage(driver)
                .validateCountersTenRandomTries();
    }

    @Test
    public void test_name_photo_change() {
        new HomePage(driver)
                .validateNamePhotoChange();
    }

    /* Test is not ready yet
    @Test
    public void test_frequency_increase_appearance() {
        new HomePage(driver)
                .validateFrequencyIncreaseAppearance();
    }
    */


    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

}
