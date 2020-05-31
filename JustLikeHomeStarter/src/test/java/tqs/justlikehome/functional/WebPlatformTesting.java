package tqs.justlikehome.functional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.openqa.selenium.interactions.Actions;
import org.springframework.test.context.ActiveProfiles;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import tqs.justlikehome.dtos.HouseSearchDTO;
import tqs.justlikehome.entities.House;
import tqs.justlikehome.repositories.HouseRepository;
import tqs.justlikehome.services.HouseService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("functional")
public class WebPlatformTesting {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private WebDriverWait wait;
    private JavascriptExecutor js;


    @Autowired
    private HouseRepository houseRepository;


    @BeforeEach
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        //options.addArguments("--headless","--disable-gpu");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:3000/");
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 15);
        js = (JavascriptExecutor) driver;


        // login
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector(".avatar")).click();
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.name("username")).click();
        driver.findElement(By.name("username")).sendKeys("andrex");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).sendKeys("12345");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        assertEquals("andrex", driver.findElement(By.cssSelector(".text-default")).getText());
    }

    @Test
    void checkPersonalHousesDetails() throws Exception {
        driver.findElement(By.linkText("Seus anúncios")).click();
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr/td")).click();
        assertEquals("house by the beach", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr/td[2]")).getText());
        assertEquals("nice house", driver.findElement(By.name("description")).getText());
    }

    @Test
    void addHouseToBookmaker() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).sendKeys("viseu");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.cssSelector(".btn-secondary")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".rounded")
        ));
        driver.findElement(By.cssSelector(".rounded")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".btn-warning")
        ));
        driver.findElement(By.cssSelector(".btn-warning")).click();
        assertEquals("Adicionado aos favoritos", driver.findElement(By.cssSelector(".col-lg-4 > span:nth-child(5)")).getText());
        driver.findElement(By.linkText("Profile")).click();
        assertEquals("house by the beach", driver.findElement(By.cssSelector(".card:nth-child(2) td:nth-child(2)")).getText());
    }

    @Test
    void eliminateHouseFromBookmarker() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).sendKeys("viseu");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.cssSelector(".btn-secondary")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".rounded")
        ));
        driver.findElement(By.cssSelector(".rounded")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".btn-warning")
        ));
        driver.findElement(By.cssSelector(".btn-warning")).click();
        assertEquals("Adicionado aos favoritos", driver.findElement(By.cssSelector(".col-lg-4 > span:nth-child(5)")).getText());
        driver.findElement(By.linkText("Profile")).click();
        assertEquals("house by the beach", driver.findElement(By.cssSelector(".card:nth-child(2) td:nth-child(2)")).getText());
        driver.findElement(By.linkText("Profile")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".card:nth-child(2) td:nth-child(2)")
        ));
        assertEquals(driver.findElement(By.cssSelector(".card:nth-child(2) td:nth-child(2)")).getText(), "house by the beach");
        driver.findElement(By.cssSelector("td:nth-child(5) .fa")).click();
        assertEquals(driver.findElement(By.cssSelector("h1")).getText(), "house by the beach");
        driver.findElement(By.linkText("Profile")).click();
        driver.findElement(By.cssSelector(".fe-trash")).click();
        {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div/div[2]/div[2]/table/tbody/tr/td[4]")
            ));
            List<WebElement> elements = driver.findElements(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div/div[2]/div[2]/table/tbody/tr/td[4]"));
            assert (elements.size() == 2);
        }
    }

    @Test
    void search() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        {
            WebElement element = driver.findElement(By.linkText("Imóveis"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).sendKeys("aveiro");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.name("inicio")).clear();
        {
            WebElement element = driver.findElement(By.name("inicio"));
            Actions builder = new Actions(driver);
            builder.click(element);
            for (int i = 0; i < 10; i++) element.sendKeys(Keys.BACK_SPACE);


        }
        driver.findElement(By.name("inicio")).sendKeys("02-02-2010");
        driver.findElement(By.cssSelector(".form-label:nth-child(1)")).click();
        {
            WebElement element = driver.findElement(By.name("fim"));
            Actions builder = new Actions(driver);
            builder.click(element);
            for (int i = 0; i < 10; i++) element.sendKeys(Keys.BACK_SPACE);
        }
        driver.findElement(By.name("fim")).sendKeys("02-02-2010");
        driver.findElement(By.cssSelector(".form-label:nth-child(1)")).click();
        driver.findElement(By.cssSelector("form > .row")).click();
        driver.findElement(By.cssSelector(".fe-search")).click();
        {
            wait.until(new ExpectedCondition<Object>() {
                        public Boolean apply(WebDriver driver){
                            int elementCount =  driver.findElements(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div[2]/div/div")).size();
                            return elementCount==0?true:false;
                        }

            });
            List<WebElement> elements = driver.findElements(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div[2]/div/div"));
            assertThat(elements.size()).isEqualTo(0);
        }
        {
            WebElement element = driver.findElement(By.name("inicio"));
            Actions builder = new Actions(driver);
            builder.click(element);
            for (int i = 0; i < 10; i++) element.sendKeys(Keys.BACK_SPACE);

        }
        driver.findElement(By.name("inicio")).sendKeys("02-02-2020");
        driver.findElement(By.cssSelector(".form-label:nth-child(1)")).click();
        {
            WebElement element = driver.findElement(By.name("fim"));
            Actions builder = new Actions(driver);
            builder.click(element);
            for (int i = 0; i < 10; i++) element.sendKeys(Keys.BACK_SPACE);

        }
        driver.findElement(By.name("fim")).sendKeys("02-02-2020");
        driver.findElement(By.cssSelector(".form-label:nth-child(1)")).click();

        driver.findElement(By.cssSelector(".fe-search")).click();
        {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div[2]/div/div")));
            List<WebElement> elements = driver.findElements(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div[2]/div/div"));
            assertThat(elements.size()).isPositive();
        }
        driver.findElement(By.name("guests")).sendKeys("10");
        driver.findElement(By.cssSelector(".fe-search")).click();
        {
            wait.until(new ExpectedCondition<Object>() {
                           public Boolean apply(WebDriver driver){
                               int elementCount =  driver.findElements(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div[2]/div/div")).size();
                               return elementCount==0?true:false;
                           }

                       }
            );
            List<WebElement> elements = driver.findElements(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div[2]/div/div"));
            assertThat(elements.size()).isEqualTo(0);
        }
    }


    @Test
    void lastExperiences() throws Exception {
        for(House house:houseRepository.findAll()){
            System.out.println(house.getHouseName());
        }
        driver.findElement(By.linkText("Profile")).click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div/div[2]/div[3]/table/tbody/tr/td[2]")
        ));
        assertEquals(driver.findElement(By.xpath("//span[@id=\'root\']/div/div/div[3]/div/div/div[2]/div[3]/table/tbody/tr/td[2]")).getText(), "house by the cloud");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/div[3]/table/tbody/tr/td[4]/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h1")
        ));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("h1")
        ));
        assertEquals(driver.findElement(By.cssSelector("h1")).getText(), "house by the cloud");
    }

    @Test
    void createUser() throws Exception {
        driver.findElement(By.xpath("//span[@id='root']/div/div/div/div/div/div/div/a/span[2]/span")).click();
        driver.findElement(By.linkText("Sign out")).click();
        driver.findElement(By.cssSelector(".avatar")).click();
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.linkText("Não tem conta?")).click();
        driver.findElement(By.name("username")).click();
        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys("migalhas");
        driver.findElement(By.name("firstName")).click();
        driver.findElement(By.name("firstName")).clear();
        driver.findElement(By.name("firstName")).sendKeys("miguel");
        driver.findElement(By.name("lastName")).click();
        driver.findElement(By.name("lastName")).clear();
        driver.findElement(By.name("lastName")).sendKeys("mota");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("12345");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("migalhas", driver.findElement(By.xpath("//span[@id='root']/div/div/div/div/div/div/div/a/span[2]/span")).getText());
    }


    @Test
    void logoutAndLogin() throws Exception {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".text-default")
        ));
        driver.findElement(By.cssSelector(".text-default")).click();
        driver.findElement(By.linkText("Sign out")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".avatar")
        ));
        driver.findElement(By.cssSelector(".avatar")).click();
        driver.findElement(By.linkText("Login")).click();
        driver.findElement(By.name("username")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        ));
        driver.findElement(By.name("username")).sendKeys("andrex");
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).click();
        driver.findElement(By.name("password")).sendKeys("12345");
        driver.findElement(By.cssSelector(".btn")).click();
        assertEquals("andrex", driver.findElement(By.cssSelector(".text-default")).getText());
    }

    @Test
    void addNewHouse() throws Exception {
        driver.findElement(By.linkText("Seus anúncios")).click();
        driver.findElement(By.name("houseName")).click();
        driver.findElement(By.name("houseName")).clear();
        driver.findElement(By.name("houseName")).sendKeys("house by the pool");
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("porto");
        driver.findElement(By.name("kmFromCityCenter")).click();
        driver.findElement(By.name("kmFromCityCenter")).clear();
        driver.findElement(By.name("kmFromCityCenter")).sendKeys("10");
        driver.findElement(By.name("numberOfBeds")).click();
        driver.findElement(By.name("numberOfBeds")).clear();
        driver.findElement(By.name("numberOfBeds")).sendKeys("4");
        driver.findElement(By.name("maxNumberOfUsers")).click();
        driver.findElement(By.name("maxNumberOfUsers")).clear();
        driver.findElement(By.name("maxNumberOfUsers")).sendKeys("5");
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("some cool house");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/form/div[7]/label[3]/span")).click();
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/form/div[7]/label[5]/span")).click();
        driver.findElement(By.name("pricePerNight")).click();
        driver.findElement(By.name("pricePerNight")).clear();
        driver.findElement(By.name("pricePerNight")).sendKeys("30");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr[3]/td[2]")));
        assertEquals("house by the pool", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr[3]/td[2]")).getText());
        assertEquals("30€", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr[3]/td[5]/strong")).getText());
    }


    @Test
    void acceptRentRequest() throws Exception {
        driver.findElement(By.linkText("Arrendamentos")).click();
        assertEquals("house by the desert", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[3]")).getText());
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[5]/button/i")).click();
        assertEquals("house by the desert", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div[2]/table/tbody/tr[2]/td[3]")).getText());
    }


    @Test
    void makeRentRequest() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("faro");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).clear();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
        ));
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Pedido feito com sucesso", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/form/div/div[4]/span[2]")).getText());
    }

    @Test
    void makeHouseReview() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("coimbra");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).clear();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button/i")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.name("description")
        ));
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("casa muito boa :)");
        driver.findElement(By.xpath("//span[@id='stars2']/span[4]")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        assertEquals("casa muito boa :)", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/div/div[2]")).getText());
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[3]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.name("description")
        ));
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("casa muito ma :(");
        driver.findElement(By.xpath("//span[@id='stars2']/span/span[2]/span")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/span")).getText());
    }

    @Test
    void makeUserReview() throws Exception {
        driver.findElement(By.linkText("Arrendamentos")).click();
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div[2]/table/tbody/tr/td/span")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/div/div[2]/h3")
        ));
        assertEquals("Joao", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/div/div[2]/h3")).getText());
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/button")).click();
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("Muito simpático :)");
        driver.findElement(By.xpath("//span[@id='stars2']/span[3]")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Avaliação: 2.5", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/div/div[2]/a/div/div[2]/div/small")).getText());
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/button/i")).click();
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("Spam Reviews :DDDD");
        driver.findElement(By.xpath("//span[@id='stars2']/span[3]")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/span")).getText());
    }

    @Test
    void makeInvalidHouseReview() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("visEU");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).clear();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button/i")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,500)");
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id=\"root\"]/div/div/div[3]/div[5]/form/div[1]/textarea")
        ));
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div[3]/div[5]/form/div[1]/textarea")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div[3]/div[5]/form/div[1]/textarea")).clear();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div[3]/div[5]/form/div[1]/textarea")).sendKeys("casa que nem vi...");
        driver.findElement(By.xpath("//span[@id='stars2']/span[4]")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='root']/div/div/div[3]/div[5]/span")
        ));
        assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/span")).getText());
    }

    @Test
    void makeInvalidUserReview() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("minho");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).clear();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/form/div/div/div/div")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/button/i")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/button/i")).click();
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("spam comment :(");
        driver.findElement(By.xpath("//span[@id='stars2']/span[2]")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/span")).getText());
    }

    @Test
    void checkAnotherUserReviews() throws Exception {
        driver.findElement(By.linkText("Imóveis")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("minho");
        driver.findElement(By.name("guests")).click();
        driver.findElement(By.name("guests")).clear();
        driver.findElement(By.name("guests")).sendKeys("1");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/form/div/div/div/div")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/div/div[2]/a/div/div[2]/div[2]")
        ));
        assertEquals("muito boa pessoa", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/div/div[2]/a/div/div[2]/div[2]")).getText());
        assertEquals("otimo dono de casa", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/div/div[2]/a[2]/div/div[2]/div[2]")).getText());
    }

    @Test
    void checkDeclineRequest() throws Exception{
        driver.findElement(By.linkText("Arrendamentos")).click();
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        int existing_requests = driver.findElements(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr")).size();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[5]/button[2]/i")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[5]/button[2]/i")).click();
        assertThat(driver.findElements(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[3]")).size() < existing_requests);
    }

    @Test
    void checkLoggedOutUserReview() throws Exception{
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".text-default")
        ));
        driver.findElement(By.cssSelector(".text-default")).click();
        driver.findElement(By.linkText("Sign out")).click();

        driver.findElement(By.linkText("Imóveis")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button")).click();
        driver.findElement(By.name("description")).click();
        driver.findElement(By.name("description")).clear();
        driver.findElement(By.name("description")).sendKeys("some bot review");
        driver.findElement(By.xpath("//span[@id='stars2']/span[3]")).click();
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@id='root']/div/div/div/div/div/form/div/div")
        ));
        assertEquals("Login", driver.findElement(By.xpath("//span[@id='root']/div/div/div/div/div/form/div/div")).getText());
    }

    @Test
    void checkLoggedOutRentRequest() throws Exception{
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".text-default")
        ));
        driver.findElement(By.cssSelector(".text-default")).click();
        driver.findElement(By.linkText("Sign out")).click();


        driver.findElement(By.linkText("Imóveis")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Login", driver.findElement(By.xpath("//span[@id='root']/div/div/div/div/div/form/div/div")).getText());
    }

    @Test
    void checkLoggedOutBookmark() throws Exception{
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".text-default")
        ));
        driver.findElement(By.cssSelector(".text-default")).click();
        driver.findElement(By.linkText("Sign out")).click();

        driver.findElement(By.linkText("Imóveis")).click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
        ));
        driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
        js.executeScript("window.scrollBy(0,1000)");
        driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
        assertEquals("Login", driver.findElement(By.xpath("//span[@id='root']/div/div/div/div/div/form/div/div")).getText());
    }

    private Callable<Boolean> awaitTTL(LocalDateTime ldt, int waitTime) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return ChronoUnit.SECONDS.between(ldt, LocalDateTime.now()) > waitTime;
            }
        };
    }

    @AfterEach
    void tearDown() throws Exception {

        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
