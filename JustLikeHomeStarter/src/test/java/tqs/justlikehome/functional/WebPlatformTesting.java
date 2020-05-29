package tqs.justlikehome.functional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.awaitility.Awaitility.await;
import org.awaitility.Duration;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("functional")
public class WebPlatformTesting {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private WebDriverWait wait;

  @BeforeEach
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    driver = new ChromeDriver(options);
    driver.get("http://localhost:3000/");
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    wait = new WebDriverWait(driver,12);
  }

  @Test
  void checkPersonalHousesDetails() throws Exception {
    driver.findElement(By.linkText("Seus anúncios")).click();
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr/td")).click();
    assertEquals("house by the beach", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr/td[2]")).getText());
    assertEquals("nice house", driver.findElement(By.name("description")).getText());
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
  public void searchForHouse() throws Exception {
    driver.findElement(By.linkText("Imóveis")).click();
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div")).click();
    driver.findElement(By.name("city")).click();
    driver.findElement(By.name("city")).clear();
    driver.findElement(By.name("city")).sendKeys("aveiro");
    driver.findElement(By.name("guests")).click();
    driver.findElement(By.name("guests")).clear();
    driver.findElement(By.name("guests")).sendKeys("1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/div/div/div")));
    assertThat(driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/div/div/div")).getText()).isEqualToIgnoringCase("aveiro");
  }

  @Test
  public void acceptRentRequest() throws Exception {
    driver.findElement(By.linkText("Arrendamentos")).click();
    assertEquals("house by the desert", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[3]")).getText());
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/table/tbody/tr/td[5]/button/i")).click();
    assertEquals("house by the desert", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div[2]/table/tbody/tr[2]/td[3]")).getText());
  }


  @Test
  public void makeRentRequest() throws Exception {
    driver.findElement(By.linkText("Imóveis")).click();
    driver.findElement(By.name("city")).click();
    driver.findElement(By.name("city")).clear();
    driver.findElement(By.name("city")).sendKeys("faro");
    driver.findElement(By.name("guests")).click();
    driver.findElement(By.name("guests")).clear();
    driver.findElement(By.name("guests")).sendKeys("1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Pedido feito com sucesso", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/form/div/div[4]/span")).getText());
  }

  @Test
  public void makeHouseReview() throws Exception {
    driver.findElement(By.linkText("Imóveis")).click();
    driver.findElement(By.name("city")).click();
    driver.findElement(By.name("city")).clear();
    driver.findElement(By.name("city")).sendKeys("coimbra");
    driver.findElement(By.name("guests")).clear();
    driver.findElement(By.name("guests")).sendKeys("1");
    wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")
    ));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")
    ));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button/i")
    ));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button/i")).click();
    driver.findElement(By.name("description")).click();
    driver.findElement(By.name("description")).clear();
    driver.findElement(By.name("description")).sendKeys("Bonita casa");
    wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button/i")
    ));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/form/div[2]/span/span[4]/span[2]/span")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[3]/button/i")).click();
    driver.findElement(By.name("description")).click();
    driver.findElement(By.name("description")).clear();
    driver.findElement(By.name("description")).sendKeys("Spammar reviews :D");
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/form/div[2]/span/span[2]/span[2]/span")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/span")).getText());
  }

  @Test
  public void makeUserReview() throws Exception {
    driver.findElement(By.linkText("Arrendamentos")).click();
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div[2]/table/tbody/tr/td/span")).click();
    assertEquals("André Baião", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/div/div[2]/h3")).getText());
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/button")).click();
    driver.findElement(By.name("description")).click();
    driver.findElement(By.name("description")).clear();
    driver.findElement(By.name("description")).sendKeys("Muito simpático :)");
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/form/div[2]/span/span/span[2]/span")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Avaliação: 0.5", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/div/div[2]/a/div/div[2]/div/small")).getText());
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/button/i")).click();
    driver.findElement(By.name("description")).click();
    driver.findElement(By.name("description")).clear();
    driver.findElement(By.name("description")).sendKeys("Spam Reviews :DDDD");
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/form/div[2]/span/span/span[2]/span")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div[2]/span")).getText());
  }

  @Test
  public void testMakeInvalidHouseReview() throws Exception {
    driver.findElement(By.linkText("Imóveis")).click();
    driver.findElement(By.name("city")).click();
    driver.findElement(By.name("city")).clear();
    driver.findElement(By.name("city")).sendKeys("viseu");
    driver.findElement(By.name("guests")).clear();
    driver.findElement(By.name("guests")).sendKeys("1");
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div[3]/button/i")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/a/img")).click();
    wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button")
    ));
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/div[2]/button")).click();
    driver.findElement(By.name("description")).click();
    driver.findElement(By.name("description")).clear();
    driver.findElement(By.name("description")).sendKeys("ma casa spam");
    driver.findElement(By.xpath("//span[@id='stars2']/span[4]/span[2]/span")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    assertEquals("Nunca esteve em nenhum dos seus bens imóveis", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div[5]/span")).getText());
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
