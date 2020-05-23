package tqs.justlikehome.functional;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.awaitility.Awaitility.await;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebPlatformTesting {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();


  @BeforeEach
  public void setUp() throws Exception {
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void checkPersonalHousesDetails() throws Exception {
    driver.get("http://localhost:3000/");
    driver.findElement(By.linkText("Seus anúncios")).click();
    Thread.sleep(5000);
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr/td")).click();
    assertEquals("house by the beach", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr/td[2]")).getText());
    assertEquals("nice house", driver.findElement(By.name("description")).getText());
  }

  @Test
  public void AddNewHouse() throws Exception {
    driver.get("http://localhost:3000/");
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
    assertEquals("house by the pool", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr[3]/td[2]")).getText());
    assertEquals("30€", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div[2]/div/table/tbody/tr[3]/td[5]/strong")).getText());
  }

  @Test
  public void SearchForHouse() throws Exception {
    driver.get("http://localhost:3000/");
    driver.findElement(By.linkText("Imóveis")).click();
    driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div/div/form/div/div")).click();
    driver.findElement(By.name("city")).click();
    driver.findElement(By.name("city")).clear();
    driver.findElement(By.name("city")).sendKeys("aveiro");
    driver.findElement(By.name("guests")).click();
    driver.findElement(By.name("guests")).clear();
    driver.findElement(By.name("guests")).sendKeys("1");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    Thread.sleep(5000);
    assertEquals("aveiro", driver.findElement(By.xpath("//span[@id='root']/div/div/div[3]/div/div[2]/div/div/div/div/div")).getText());
  }

  @AfterEach
  public void tearDown() throws Exception {
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
