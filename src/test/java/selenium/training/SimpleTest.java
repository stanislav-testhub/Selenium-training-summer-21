package selenium.training;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SimpleTest {

    WebDriver driver;

    @BeforeEach
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("start-maximized");
        driver = new ChromeDriver(opts);
        driver.get("");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement loginform = driver.findElement(By.cssSelector("div.content"));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.content")));
        loginform.findElement(By.name("username")).sendKeys("");
        loginform.findElement(By.name("password")).sendKeys("");
        driver.findElement(By.cssSelector("button[name=login]")).click();
    }

    @AfterEach
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void authorizationAction() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#box-apps-menu")));

        for (int menuIterator=1;
             menuIterator<driver.findElements(By.cssSelector("#box-apps-menu>li")).size(); menuIterator++)
            {
                    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#box-apps-menu")));
                    driver.findElements(By.cssSelector("#box-apps-menu>li")).get(menuIterator).click();
                    Assertions.assertTrue(driver.findElement(By.cssSelector("div.panel-heading")).isDisplayed());

                    for (int submenuIterator=0;
                         submenuIterator<driver.findElements(By.cssSelector(".doc")).size(); submenuIterator++)
                    {
                        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#box-apps-menu")));
                        driver.findElements(By.cssSelector(".doc")).get(submenuIterator).click();
                        Assertions.assertTrue(driver.findElement(By.cssSelector("div.panel-heading")).isDisplayed());
                    }
            }

    }

}
