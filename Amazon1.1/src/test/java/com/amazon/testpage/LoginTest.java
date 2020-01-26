package com.amazon.testpage;



import org.testng.annotations.Test;

import com.amazon.pages.LoginPage;
import com.amazon.pages.RegistrationPage;
import com.amazon.pages.VerifyUserLoggedInPage;
//import com.amazon.testspage.ReadExcel;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

//import javax.servlet.Registration;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
//import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;


//import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class LoginTest {
 ChromeDriver driver;
 RegistrationPage RP;
 LoginPage LP;
 VerifyUserLoggedInPage VP;
 Base64.Decoder dec = Base64.getDecoder();
 
 //private static final Logger logger = LogManager.getLogger(LoginTest.class);

 //ExtentReports extent;
 //private static ExtentTest logger;
 ExtentReports extent = new ExtentReports();
 ExtentHtmlReporter reporter = new ExtentHtmlReporter("./Reports/Amazonee_LOGIN_automation.html");
 
 
 @Test(dataProvider = "getdatalogin") //ScenarioOne : Valid email id and password is entered.
 public void ScenarioOne(String usrnm, String pswd, String expstring, String Invldemail, String InvlPass,
   String ValidPhnNo, String InvalidPhnNo) throws InterruptedException, IOException {

  
  //Report related code
  extent.attachReporter(reporter);
  //Report related code
  ExtentTest logger= extent.createTest("LoginTest 1 - Valid Email and Pass");
    
  logger.log(Status.INFO, "Login to amazon");
  logger.log(Status.INFO,"====LoginTest --> Scenario One =====");
  
  
  
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I01-LoginPage.png");// screenshot code line
  logger.log(Status.INFO,"Starting Test.");
  
  LP.EnterUsername(usrnm);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I02-UsernameEntered.png");// screenshot code line
  LP.ContinueAction();

  logger.log(Status.INFO,"Username done landed on Next Page");
  //Decode data
  String decoded = new String(dec.decode(pswd));
  //System.out.println("decoded value is \t" + decoded);
  
  LP.EnterPassword(decoded);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I03-PasswordEntered.png");// screenshot code line
  
  LP.SubmitAction();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I04-AfterSubmit.png");// screenshot code line

  //logger.trace("Landed successfully on user home page");
  logger.log(Status.INFO,"Landed successfully on user home page");
  try {
   Alert alert = driver.switchTo().alert();
   alert.dismiss();

  } catch (Exception e) {
   System.out.println("Alert didn't appear");
  }
  
  String captchaText = "To better protect your account, please re-enter your password and then enter the characters as they are shown in the image below.";
  try {
   while(captchaText.equals(LP.CaptchTxtMatch())) { 
    
    logger.log(Status.INFO,"We are on captcha page");

      // prompt user to enter captcha
      String captchaVal1 = JOptionPane.showInputDialog("Please enter the captcha value : ");
      LP.CaptchaEnter(captchaVal1);
      LP.EnterPassword(decoded);
      LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I05-CaptchHandled.png");// screenshot code line
      LP.SubmitAction();  
    
    }
  } catch (Exception e) {
   System.out.println("Captcha didn't appear..");
  }

  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I06-SuccessfullyLoggedIn.png");// screenshot code line
  Thread.sleep(3000);
  String result = VP.Verifylogin();

  
  if(result.equals(expstring)) {
   logger.log(Status.PASS,"Completed test execution");   
  }else
  {
   logger.fail("Test case FAILED");
  }
  
  Assert.assertEquals(result, expstring);
  

  Thread.sleep(4000);
  // Hover over 'Account and Lists'
  Actions hover = new Actions(driver);
  Action hoverOverSignin = hover.moveToElement(VP.HoveroverSign()).build();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I07-HovertoSignOutButton.png");// screenshot code line
  
  hoverOverSignin.perform();

  // Click on Sign out button
  VP.Signoutbutton();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S01I08-AfterSignOut.png");// screenshot code line
  

 }

 @Test(dataProvider = "getdatalogin") //ScenarioTwo : invalid email id.
 public void ScenarioTwo(String usrnm, String pswd, String expstring, String Invldemail, String InvlPass, String ValidPhnNo, 
   String InvalidPhnNo) throws IOException {
  
  //Report related code
  extent.attachReporter(reporter);
  ExtentTest logger= extent.createTest("LoginTest 2 - Invalid Email");
  
  logger.log(Status.INFO,"====LoginTest --> Scenario Two =====");
  LP.EnterUsername(Invldemail);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S02I01-InvalidEmailEntered.png");// screenshot code line
  LP.ContinueAction();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S02I02-SubmittedInvalidEmail.png");// screenshot code line
  
  if(LP.InvalidEmlMesage().equals("We cannot find an account with that email address")) {
   logger.log(Status.PASS,"Completed test execution");   
  }else
  {
   logger.fail("Test case FAILED");
  }
  
    
  Assert.assertEquals(LP.InvalidEmlMesage(), "We cannot find an account with that email address");
  
  

 }

 @Test(dataProvider = "getdatalogin") //ScenarioThree : Valid email id and invalid password is entered.
 public void ScenarioThree(String usrnm, String pswd, String expstring, String Invldemail, String InvlPass,
   String ValidPhnNo, String InvalidPhnNo) throws InterruptedException, IOException {
  
  //Report related code
  extent.attachReporter(reporter);
  
  ExtentTest logger= extent.createTest("LoginTest 3 - Valid Email and Invalid Password");
  
  logger.log(Status.INFO,"====LoginTest --> Scenario Three =====");
  LP.EnterUsername(usrnm);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S03I01-ValEmlEntered.png");// screenshot code line
  
  LP.ContinueAction(); 

  logger.log(Status.INFO,"Username done landed on Next Page");
  LP.EnterPassword(InvlPass);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S03I02-InvldPassEntered.png");// screenshot code line
  LP.SubmitAction();
  String captchaText = "To better protect your account, please re-enter your password and then enter the characters as they are shown in the image below.";
  try {
   while(captchaText.equals(LP.CaptchTxtMatch())) {
    
    logger.log(Status.INFO,"We are on captcha page");

    // prompt user to enter captcha
    String captchaVal1 = JOptionPane.showInputDialog("Please enter the captcha value : ");
    LP.CaptchaEnter(captchaVal1);
    LP.EnterPassword(InvlPass);
    LP.SubmitAction();  
  }

  } catch (Exception e) {
   System.out.println("Captcha didn't appear..");
  }
  
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S03I03-InvldPassMessage.png");// screenshot code line
  Thread.sleep(3000);
  
  if(LP.InvalidEmlMesage().equals("Your password is incorrect")) {
   logger.log(Status.PASS,"Completed test execution");   
  }else
  {
   logger.fail("Test case FAILED");
  }
  
  Assert.assertEquals(LP.InvalidEmlMesage(), "Your password is incorrect");
  
 }

 @Test() //ScenarioFour : Email id and password are left blank and Sign in entered.
 public void ScenarioFour() throws IOException {
  ExtentTest logger= extent.createTest("LoginTest 4 - Email and Password left Blank");
  logger.log(Status.INFO,"====LoginTest --> Scenario Four =====");
  LP.ContinueAction();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S04I01-BlankEmail.png");// screenshot code line
  
  if(LP.BlankEmail().equals("Enter your email or mobile phone number")) {
   logger.log(Status.PASS,"Completed test execution");   
  }else
  {
   logger.fail("Test case FAILED");
  }
  
  
  Assert.assertEquals(LP.BlankEmail(), "Enter your email or mobile phone number");
 }

 @Test(dataProvider = "getdatalogin") //ScenarioFive : Forgot your password is working as expected
 public void ScenarioFive(String usrnm, String pswd, String expstring, String Invldemail, String InvlPass,
   String ValidPhnNo, String InvalidPhnNo) throws IOException {
  
  //Report related code
  extent.attachReporter(reporter);
  
  //until this is the latest code
  
  
  ExtentTest logger= extent.createTest("LoginTest 5 - Forgot Password Working");
  
  logger.log(Status.INFO,"====LoginTest --> Scenario Five =====");
  
  
  logger.log(Status.INFO,"Starting Test.");
  LP.EnterUsername(usrnm);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S05I01-UsernameEntered.png");// screenshot code line
  LP.ContinueAction();
  logger.log(Status.INFO,"Username done landed on Next Page");
  
  //clicking on forgot password
  LP.Forgotpass();

LoginTest.screencapture(driver, "./Screenshots/LoginPage/S05I01-AfterForgotPasswordClicked.png");// screenshot code line

  
  if(LP.PassAssist().equals("Password assistance")) {
	   logger.log(Status.PASS,"Completed test execution");   
	  }else
	  {
	   logger.fail("Test case FAILED");
	  }
  
  // String AssistText = LP.PassAssist();
  Assert.assertEquals(LP.PassAssist(), "Password assistance"); 

 }

 @Test(dataProvider = "getdatalogin") //ScenarioSix : Valid Phn No and password is entered.
 public void ScenarioSix(String usrnm, String pswd, String expstring, String Invldemail, String InvlPass,
   String ValidPhnNo, String InvalidPhnNo) throws InterruptedException, IOException {
	 
	  
	  ExtentTest logger= extent.createTest("LoginTest 6 - Valid Phn No and Password");
	  
	  logger.log(Status.INFO,"====LoginTest --> Scenario Six =====");
	 
	 logger.log(Status.INFO,"Starting Test.");
  LP.EnterUsername(ValidPhnNo);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I01-UsernameEntered.png");// screenshot code line
  LP.ContinueAction();

  logger.log(Status.INFO,"Username done landed on Next Page");
  
//Decode data
  String decoded = new String(dec.decode(pswd));
  //System.out.println("decoded value is \t" + decoded);
  
  LP.EnterPassword(decoded);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I02-PasswordEntered.png");// screenshot code line
  LP.SubmitAction();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I03-AfterSubmit.png");// screenshot code line
  
  logger.log(Status.INFO,"Landed successfully on user home page");
  try {
   Alert alert = driver.switchTo().alert();
   alert.dismiss();

  } catch (Exception e) {
   System.out.println("Alert didn't appear");
  }

  String captchaText = "To better protect your account, please re-enter your password and then enter the characters as they are shown in the image below.";
  try {
   while(captchaText.equals(LP.CaptchTxtMatch())) {
    
	   logger.log(Status.INFO,"We are on captcha page");

    // prompt user to enter captcha
    String captchaVal1 = JOptionPane.showInputDialog("Please enter the captcha value : ");
    LP.CaptchaEnter(captchaVal1);
    LP.EnterPassword(pswd);
    LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I04-CaptchHandled.png");// screenshot code line
    LP.SubmitAction();

    
   
  
  } 
  } catch (Exception e) {
   System.out.println("Captcha didn't appear..");
  }
  
  
  // VP = new VerifyUserLoggedInPage(driver);
  String result = VP.Verifylogin();

  if(result.equals(expstring)) {
	   logger.log(Status.PASS,"Completed test execution");   
	  }else
	  {
	   logger.fail("Test case FAILED");
	  }
  
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I05-SuccessfullyLoggedIn.png");// screenshot code line
  
  Assert.assertEquals(result, expstring);
  

  Thread.sleep(4000);
  // Hover over 'Account and Lists'
  Actions hover = new Actions(driver);
  Action hoverOverSignin = hover.moveToElement(VP.HoveroverSign()).build();
  hoverOverSignin.perform();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I06-HovertoSignOutButton.png");// screenshot code line
  // Click on Sign out button
  VP.Signoutbutton();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I07-AfterSignOut.png");// screenshot code line

 }

 @Test(dataProvider = "getdatalogin") //ScenarioSeven : Invalid Phone No is entered.
 public void ScenarioSeven(String usrnm, String pswd, String expstring, String Invldemail, String InvlPass,
   String ValidPhnNo, String InvalidPhnNo) throws IOException {
	 
  ExtentTest logger= extent.createTest("LoginTest 7 - Invalid Phone No");
	  
	  logger.log(Status.INFO,"====LoginTest --> Scenario Seven =====");
	 
  LP.EnterUsername(InvalidPhnNo);
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I01-InvalidPhoneEntered.png");// screenshot code line
  LP.ContinueAction();
  LoginTest.screencapture(driver, "./Screenshots/LoginPage/S06I02-AfterSubmitInvalidPhone.png");// screenshot code line
  
  if(LP.InvalidEmlMesage().equals(expstring)) {
	   logger.log(Status.PASS,"We cannot find an account with that mobile number");   
	  }else
	  {
	   logger.fail("Test case FAILED");
	  }
  
  Assert.assertEquals(LP.InvalidEmlMesage(), "We cannot find an account with that mobile number");
 }

 @BeforeMethod
 public void beforeMethod() throws InterruptedException {
  LP = new LoginPage(driver);
  String url = "https://www.amazon.in/ap/signin?openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.in%2F%3Fref_%3Dnav_ya_signin&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.assoc_handle=inflex&openid.mode=checkid_setup&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&";
  driver.get(url);
  VP = new VerifyUserLoggedInPage(driver);
  //VP.GotoLoginClick();

 }

 @AfterMethod
 public void afterMethod() {
  //String endOfATest = JOptionPane.showInputDialog("Please enter Okay to proceed with further test execution: ");
  //System.out.println("Confirmation from tester to start next test exection: "+endOfATest);
  
 }

 @DataProvider
 public Object[][] getdatalogin() throws IOException {
  String currentDirectory = System.getProperty("user.dir");
  System.out.println(currentDirectory);
  String datafile = currentDirectory + "\\src\\test\\resources\\utils\\Amazone.xlsx";
  System.out.println("Current Directory of test data: " + datafile);
  String sheetname = "LoginTest";
  Object[][] myTestData = ReadExcel.readTestData(datafile, sheetname);

  return myTestData;

 }

 @BeforeClass
 public void beforeClass() {
  System.setProperty("webdriver.chrome.driver",
    "C:\\Training\\Jar Files\\Drivers\\chromedriver.exe");
  driver = new ChromeDriver();
  driver.manage().window().maximize();
  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  
  /*
   * LP = new LoginPage(driver); driver.get("https://www.amazon.in/"); VP = new
   * VerifyUserLoggedInPage(driver); VP.GotoLoginClick();
   */
 }

 @AfterClass
 public void afterClass() {
  driver.quit();
  
  extent.flush();
 }
 
 public static void screencapture(WebDriver driver, String fname) throws IOException {
	  TakesScreenshot ts = (TakesScreenshot) driver;
	  File source = ts.getScreenshotAs(OutputType.FILE);
	  FileUtils.copyFile(source, new File(fname));

	  // FileUtils.copyFile(source, new File("./Screenshots/HomePage.png"));

	 }

}

  
  
  
  
