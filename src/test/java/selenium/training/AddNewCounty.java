package selenium.training;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Set;



public class AddNewCounty {

    public ExpectedCondition<String> anyWindowOtherThan(Set<String> windows){
        return new ExpectedCondition<String>() {
            @NullableDecl
            @Override
            public String apply(@NullableDecl WebDriver input) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(windows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }

    WebDriver driver;

    @BeforeEach
    public void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("start-maximized");
        driver = new ChromeDriver(opts);
        driver.get("");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.content")));

        WebElement loginform = driver.findElement(By.cssSelector("div.content"));
        loginform.findElement(By.name("username")).sendKeys("");
        loginform.findElement(By.name("password")).sendKeys("t");
        driver.findElement(By.cssSelector("button[name=login]")).click();
        Thread.sleep(2000);
    }

    @AfterEach
    public void tearDown()
    {
        driver.quit();
    }

    @Test //Homework 2.2: Add new county
    public void addNewCounty(){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.btn.btn-default"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("form")));
        String originalTab = driver.getWindowHandle();
        Set<String> existTabs = driver.getWindowHandles();

        for(int linkIterator=0;
            linkIterator<driver.findElements(By.cssSelector("i.fa.fa-external-link")).size(); linkIterator++){
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("form")));
            driver.findElements(By.cssSelector("i.fa.fa-external-link")).get(linkIterator).click();
            String newTab= wait.until(anyWindowOtherThan(existTabs));
            driver.switchTo().window(newTab);
            new WebDriverWait(driver, 10).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            driver.close();
            driver.switchTo().window(originalTab);
        }
    }
}
