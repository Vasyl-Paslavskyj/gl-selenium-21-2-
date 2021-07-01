import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Iterator;
import java.util.Set;

public class OpenLinksInNewWindow extends TestBase{
    By addNewCountry = By.xpath("//*[text()=' Add New Country']");
    By arrowIcon = By.cssSelector(".fa.fa-external-link");

    @BeforeEach
    void setUp() {
        loginAdminPage();
    }

    @Test
    void name() {
        driver.get("http://158.101.173.161/admin/?app=countries&doc=countries");
        wait.until(ExpectedConditions.elementToBeClickable(addNewCountry)).click();
        wait.until(ExpectedConditions.urlContains("edit_country"));
        for (WebElement element: driver.findElements(arrowIcon)) {
            String originWindow = driver.getWindowHandle();
            new Actions(driver).moveToElement(element).pause(200).click().perform();
            Set<String> allWindows = driver.getWindowHandles();
            Iterator<String> iterator = allWindows.iterator();
            while (iterator.hasNext()) {
                String childWindow = iterator.next();
                if(!originWindow.equalsIgnoreCase(childWindow)) {
                    driver.switchTo().window(childWindow);
                    wait.until(ExpectedConditions.urlContains("wikipedia"));
                    Assertions.assertTrue(driver.getTitle().contains("Wikipedia"));
                    driver.close();
                }
            }
            driver.switchTo().window(originWindow);
            Assertions.assertTrue(driver.getTitle().contains("Add New Country"));
        }
    }
}
