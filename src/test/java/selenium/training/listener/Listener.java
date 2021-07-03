package selenium.training.listener;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;

public class Listener extends AbstractWebDriverEventListener {

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver){
        System.out.println("Trying to search => " + by + "element");
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver){
        System.out.println(by + " has been found");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {

        File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.copy(tempFile,
                    new File(System.currentTimeMillis() + "screen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[Shit Happened:] "+throwable.getMessage().split(":")[0]);
    }
}
