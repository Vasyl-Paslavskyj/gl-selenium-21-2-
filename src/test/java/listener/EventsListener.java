package listener;

import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.io.File;
import java.io.IOException;

public class EventsListener extends AbstractWebDriverEventListener {
    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println("[Starting search for:] " +by);
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        System.out.println("[" + by + "] has been found");
    }

    @Override
    public void afterSwitchToWindow(String windowName, WebDriver driver) {
        System.out.println("[After switch to window] - " + windowName);
    }

    @Override
    public void beforeSwitchToWindow(String windowName, WebDriver driver) {
        System.out.println("[Before switch to window] - " + windowName);
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> target) {
        System.out.println("[Before get screenshot as] - " + target);
    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
        System.out.println("[After get screenshot as]: Target - " + target + ", screenshot - " + screenshot);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(tempFile,
                    new File(System.currentTimeMillis() + "screen.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
