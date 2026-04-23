package test;

//import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("All Tests")
@SelectPackages("test")
//@SelectClasses({BlackjackServerTest.class, BlackjackClient.class, MessageStatusTest.class})
//@SelectClasses({MessageStatusTest.class})
public class AllTests {

}
