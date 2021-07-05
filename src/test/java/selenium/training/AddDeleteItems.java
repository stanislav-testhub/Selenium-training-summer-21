package selenium.training;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class AddDeleteItems {

    WebDriver driver;
    Random random = new Random();
    String popularProductsSelector = "//*[@id='box-popular-products']//*[@class='product-column'][%s]";

    @BeforeEach
    public void setUp() {
        extracted();
    }

    private void extracted() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("start-maximized");
        driver = new ChromeDriver(opts);
        driver.get("http://158.101.173.161/");
        if (driver.findElement(By.cssSelector("#box-cookie-notice")).isDisplayed()) {
            driver.findElement(By.cssSelector("[name=accept_cookies]")).click();
        }
    }


    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void cartItemsAddDelete(){
        int itemsInTheCart = 3;
        addToCart(itemsInTheCart);
        removeProductsFromCart();
    }

    private void addToCart(int itemsInTheCart) {
        for (int i = 0; i < itemsInTheCart; i++) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(elementToBeClickable(By.cssSelector("#box-popular-products .product-column")));
            addRandomProduct();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-success"))).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".badge.quantity"), String.valueOf((i) + 1)));
            driver.get("http://158.101.173.161/");
        }
    }

    private void addRandomProduct() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        int products = driver.findElements(By.cssSelector("#box-popular-products .product-column")).size();
        WebElement product =
                wait.until(elementToBeClickable(By.xpath(String.format(popularProductsSelector, random.nextInt(products) + 1))));
        new Actions(driver).moveToElement(product).pause(500).click(product).perform();
    }


    private void removeProductsFromCart() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#cart"))).click();
        int productsInTheCart = wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(".items"))).size();
        for (int cartItems = 0; cartItems <= productsInTheCart; cartItems++) {
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[name='remove_cart_item']"))).click();
                wait.until(stalenessOf(driver.findElement(By.cssSelector(".items.list-unstyled"))));
        }
    }
}

