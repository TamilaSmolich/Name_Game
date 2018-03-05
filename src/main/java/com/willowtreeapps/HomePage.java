package com.willowtreeapps;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Defining test suit variables.
    private int defaultTimeout = 500;
    /**
     * Looks like UI is waiting about 4 sec after correct picture was chosen. I'd doublecheck with devs exact timeout
     * and tune it according to requirement for optimal performance.
     */
    private int timeoutAfterCorrectAnswer = 4100;


    /**
     * Implemented simple WaitForPageLoad method based on visibility of "name" element to replace previously used command
     * "Sleep 6000" which concerned me a lot. This method allows us to perform testing much more faster, because we
     * don't need to explicit 6 sec wait.
     */
    public void waitForPageLoad(WebDriver drv, int timeout) {
        sleep(timeout);		//	Make sure new page init has started...
        new WebDriverWait(drv,10).until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    }

    public void validateTitleIsPresent() {
        WebElement title = driver.findElement(By.cssSelector("h1"));
        Assert.assertTrue(title != null);
    }

    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        // Wait for page to load
        waitForPageLoad(driver, defaultTimeout);

        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        driver.findElement(By.className("photo")).click();

        waitForPageLoad(driver, defaultTimeout);

        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        Assert.assertTrue(countAfter > count);

    }

    public void validateClickingCorrectPhotoIncreasesStreakCounter() {
        // Wait for page to load
        waitForPageLoad(driver, defaultTimeout);

        int count = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        // Find and get a person's name to match with a picture
        String name = driver.findElement(By.id("name")).getText();
        System.out.println("name = " + name);

        // Find a matching picture by xPath
        driver.findElement(By.xpath("//div[./div[contains(@class, 'name') and text()='" + name + "']]")).click();
        waitForPageLoad(driver, defaultTimeout);

        // Find a "streak" counter and verify it increments
        int countStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        System.out.println("countStreak = " + countStreak);
        Assert.assertTrue(countStreak == count+1);

    }

    public void validateStreakCounterResetsAfterIncorrectAnswer() {
        // Wait for page to load
        waitForPageLoad(driver, defaultTimeout);

        int count = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        // Find and get a person's name to match with a picture
        String name = driver.findElement(By.id("name")).getText();
        System.out.println("name = " + name);

        // Find a matching picture by xPath
        driver.findElement(By.xpath("//div[./div[contains(@class, 'name') and text()='" + name + "']]")).click();
        waitForPageLoad(driver, timeoutAfterCorrectAnswer);

        // Find a "streak" counter and verify it increments
        int countStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        System.out.println("countStreak = " + countStreak);
        Assert.assertTrue(countStreak == count+1);

        // Find and get a person's name to match with a picture
        name = driver.findElement(By.id("name")).getText();
        System.out.println("new_name = " + name);

        // Find a picture by xPath that doesn't match
        driver.findElement(By.xpath("//div[./div[contains(@class, 'name') and text()!='" + name + "']]")).click();
        waitForPageLoad(driver, defaultTimeout);

        // Find a "streak" counter and verify it resets
        countStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        System.out.println("countStreak = " + countStreak);
        Assert.assertTrue(countStreak == 0);


    }
    public void validateCountersTenRandomTries() {
        //Wait for page to load
        waitForPageLoad(driver, defaultTimeout);

        int counter_correct = 0;
        int tries = 0;
        // Generating list 0 - 4
        List<Integer> range = IntStream.rangeClosed(0, 4)
                .boxed().collect(Collectors.toList());

        for (int i=0; i<10; i++) {

            String name = driver.findElement(By.id("name")).getText();
            System.out.println("Correct name: '" + name + "', try = " + Integer.toString(i));

            // Picking random number from list
            Random randomizer = new Random();
            int randomNum = range.get(randomizer.nextInt(range.size()));

            String actual_name = driver.findElements(By.className("name")).get(randomNum).getText();
            System.out.println("Click on: '" + actual_name + "', try = " + Integer.toString(i));

            // Clicking random photo
            driver.findElements(By.className("photo")).get(randomNum).click();
                if (name.equals(actual_name)) {
                    System.out.println("Correct guess");
                    counter_correct++;
                    tries++;
                    // Regenerating list in case of correct answer
                    range = IntStream.rangeClosed(0, 4)
                            .boxed().collect(Collectors.toList());
                    // Wait for page to load
                    waitForPageLoad(driver, timeoutAfterCorrectAnswer);
                }
                else {
                    System.out.println("Incorrect guess");
                    tries++;
                    // Removing incorrect photo number from the list
                    range.remove(new Integer(randomNum));
                }
        }

        // Finding displayed number of tries and correct answers
        int tries_displayed = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        int counter_correct_displayed = Integer.parseInt(driver.findElement(By.className("correct")).getText());

        System.out.println("Tries: " + Integer.toString(tries));
        System.out.println("Counter correct: " + Integer.toString(counter_correct));

        Assert.assertTrue(tries_displayed==tries);
        Assert.assertTrue(counter_correct_displayed==counter_correct);

        }

        public void validateNamePhotoChange () {
            //Wait for page to load
            waitForPageLoad(driver, defaultTimeout);

            String name = driver.findElement(By.id("name")).getText();
            System.out.println("name = " + name);

            // Getting list of web elements
            List<WebElement> photos = driver.findElements(By.xpath("//div[@class='photo']//img"));
            List<String> srcs = new ArrayList<>();

            // Getting list of actual photo srcs
            for (WebElement element : photos) {
                String src = element.getAttribute("src");
                srcs.add(src);
            }
            System.out.println(srcs);

            // Finding correct name by xPath
            driver.findElement(By.xpath("//div[./div[contains(@class, 'name') and text()='" + name + "']]")).click();
            waitForPageLoad(driver, timeoutAfterCorrectAnswer);

            String nameUpdated = driver.findElement(By.id("name")).getText();
            System.out.println("name = " + nameUpdated);

            // Getting updated list of photo srcs
            photos = driver.findElements(By.xpath("//div[@class='photo']//img"));
            List<String> srcsUpdated = new ArrayList<>();
            for (WebElement element : photos) {
                String src = element.getAttribute("src");
                srcsUpdated.add(src);
            }
            System.out.println(srcsUpdated);


            Assert.assertTrue(!name.equals(nameUpdated));
            Assert.assertTrue(!srcs.equals(srcsUpdated));

        }

    public void validateFrequencyIncreaseAppearance () {
        /**
         * Couldn't figure out approach how to automatically test this functionality.
         * I think we should use Java HashMap for this and run about 50-100 attempts to get statistics
         * of appearance and store the results in a HashMap.
         */
    }
}
