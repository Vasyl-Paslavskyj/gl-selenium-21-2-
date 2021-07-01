import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {
    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    public WebDriver driver;
    public WebDriverWait wait;

    @BeforeEach
    void start() {
        WebDriverManager.chromedriver().setup();

        if (tlDriver.get() != null) {
            driver = tlDriver.get();
            wait = new WebDriverWait(driver, 10);
            return;
        }

        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        tlDriver.set(driver);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    driver.quit();
                    driver = null;
                }));
    }

    public void loginAdminPage() {
        driver.get("http://158.101.173.161/admin/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//form//input[@name='username']"))).sendKeys("");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("form input[name=password]"))).sendKeys("");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form//button[@name='login' and @type='submit']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='box-apps-menu']")));
    }
}
