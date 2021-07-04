import io.github.bonigarcia.wdm.WebDriverManager;
import listener.EventsListener;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    public WebDriver driver;
    public WebDriverWait wait;
    public EventFiringWebDriver eventFiringWebDriver;

    @BeforeEach
    void start() {
        WebDriverManager.chromedriver().setup();

        if (tlDriver.get() != null) {
            driver = tlDriver.get();
            eventFiringWebDriver = new EventFiringWebDriver(driver);
            eventFiringWebDriver.register(new EventsListener());
            wait = new WebDriverWait(eventFiringWebDriver, 10);
            return;
        }

        driver = new ChromeDriver();
        eventFiringWebDriver = new EventFiringWebDriver(driver);
        eventFiringWebDriver.register(new EventsListener());
        wait = new WebDriverWait(eventFiringWebDriver, 10);
        tlDriver.set(eventFiringWebDriver);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    eventFiringWebDriver.quit();
                    driver = null;
                    eventFiringWebDriver = null;
                }));
    }

    public void loginAdminPage() {
        eventFiringWebDriver.get("http://158.101.173.161/admin/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//form//input[@name='username']"))).sendKeys("");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form input[name=password]"))).sendKeys("");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form//button[@name='login' and @type='submit']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='box-apps-menu']")));
    }
}
