import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Random;

public class AddRemoveItemsFromCart extends TestBase{
    By acceptCookies = By.xpath("//div[@id='box-cookie-notice']//button[@name='accept_cookies']");
    By popularProducts = By.xpath("//section[@id='box-popular-products']//article[@class='product-column']");
    By addToCart = By.xpath("//button[@name='add_cart_product']");
    By countLabel = By.xpath("//*[contains(@class,'badge') and contains(@class,'quantity')]");
    By cart = By.xpath("//div[@id='cart']");
    By shoppingCart = By.xpath("//*[@id='box-checkout-cart']/h2");
    By removeCart = By.xpath("//*[@id='box-checkout-cart']//button[@name='remove_cart_item']");
    By noItemsTxt = By.xpath("//*[text()='There are no items in your cart.']");
    By cartItemsTable = By.cssSelector(".items.list-unstyled");

    Random random = new Random();

    @BeforeEach
    void setUp() {
        eventFiringWebDriver.get("http://158.101.173.161/");
        if(wait.until(ExpectedConditions.elementToBeClickable(acceptCookies)).isDisplayed()){
            new Actions(eventFiringWebDriver).moveToElement(eventFiringWebDriver.findElement(acceptCookies)).pause(500).click().perform();
        }
    }

    @Test
    void add_remove_items() {
        int count = 3;
        addProducts(count);
        Assertions.assertEquals(eventFiringWebDriver.findElement(countLabel).getText(), String.valueOf(count));
        removeProducts();
        Assertions.assertEquals(eventFiringWebDriver.findElement(countLabel).getText(), "");
    }

    private void removeProducts() {
        wait.until(ExpectedConditions.elementToBeClickable(cart)).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(shoppingCart, "Shopping Cart"));
        int size = eventFiringWebDriver.findElements(removeCart).size();
        for (int item = 0; item < size; item++) {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(removeCart));
            new Actions(eventFiringWebDriver).moveToElement(element).pause(500).click().perform();
            if(item < size-1)
                wait.until(ExpectedConditions.presenceOfElementLocated(cartItemsTable));
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(noItemsTxt));
        eventFiringWebDriver.get("http://158.101.173.161/");
    }

    private void addProducts(int count) {
        for (int i = 1; i <= count; i++){
            addRandomProduct();
            new Actions(eventFiringWebDriver).moveToElement(wait.until(ExpectedConditions.elementToBeClickable(addToCart)))
                    .pause(500).click().perform();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(countLabel, String.valueOf(i)));
            eventFiringWebDriver.get("http://158.101.173.161/");
        }
    }

    private void addRandomProduct() {
        int index = random.nextInt(eventFiringWebDriver.findElements(popularProducts).size()) + 1;
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//section[@id='box-popular-products']//article[@class='product-column']["+index+"]" ))).click();
    }
}
