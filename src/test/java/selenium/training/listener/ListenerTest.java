package selenium.training.listener;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;


public class ListenerTest {

    EventFiringWebDriver edr;
    WebDriverWait wait;
    Random random = new Random();
    String popularProductsSelector = "//*[@id='box-popular-products']//*[@class='product-column'][%s]";

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        edr = new EventFiringWebDriver(new ChromeDriver());
        edr.register(new Listener());
        wait = new WebDriverWait(edr, 10);
        edr.get("");
        if (edr.findElement(By.cssSelector("#box-cookie-notice")).isDisplayed()) {
            edr.findElement(By.cssSelector("[name=accept_cookies]")).click();
        }
    }


    @AfterEach
    public void tearDown() {
        edr.quit();
    }

    @Test
    public void cartItemsAddDelete(){
        int itemsInTheCart = 3;
        addToCart(itemsInTheCart);
        removeProductsFromCart();
    }

    private void addToCart(int itemsInTheCart) {
        for (int i = 0; i < itemsInTheCart; i++) {
            WebDriverWait wait = new WebDriverWait(edr, 10);
            wait.until(elementToBeClickable(By.cssSelector("#box-popular-products .product-column")));
            addRandomProduct();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-success"))).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".badge.quantity"), String.valueOf((i) + 1)));
            edr.get("");
        }
    }

    private void addRandomProduct() {
        WebDriverWait wait = new WebDriverWait(edr, 10);
        int products = edr.findElements(By.cssSelector("#box-popular-products .product-column")).size();
        WebElement product =
                wait.until(elementToBeClickable(By.xpath(String.format(popularProductsSelector, random.nextInt(products) + 1))));
        new Actions(edr).moveToElement(product).pause(500).click(product).perform();
    }


    private void removeProductsFromCart() {
        WebDriverWait wait = new WebDriverWait(edr, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#cart"))).click();
        wait.until(presenceOfElementLocated(By.cssSelector(".items.list-unstyled")));
        int productsInTheCart = edr.findElements(By.cssSelector(".item")).size();
        for (int cartItems = 0; cartItems <= productsInTheCart; cartItems++) {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[name='remove_cart_item']"))).click();
            wait.until(stalenessOf(edr.findElement(By.cssSelector(".items.list-unstyled"))));
        }
    }
}