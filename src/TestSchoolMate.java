import org.junit.*;
import net.sourceforge.jwebunit.junit.WebTester;

public class TestSchoolMate {

	private WebTester tester;
	private String previousValue = null;

	@Before
	public void prepare() {
		tester = new WebTester();
		tester.setBaseUrl("http://localhost/schoolmate/");
	}

	public void login() {
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "schoolmate");
		tester.setTextField("password", "schoolmate");
		tester.submit();
	}

	@Test
	public void testVulnerability54() {
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "schoolmate");
		tester.setTextField("password", "schoolmate");
		tester.submit();

		tester.assertMatch("Manage Classes");
		tester.clickLinkWithText("School");

		tester.assertMatch("Manage School Information");
		previousValue = tester.getElementByXPath(
				"html//textarea[@name='sitetext']").getTextContent();
		tester.setTextField("sitetext",
				"<a href=http://unitn.it>malicious link</a>");
		tester.clickButtonWithText(" Update ");

		tester.clickLinkWithText("Log Out");
		tester.assertMatch("Today's Message");

		tester.assertLinkNotPresentWithText("malicious link");
	}

	@Test
	public void testVulnerabilityEditClass() {
		/*
		 * Pass unsanitized ID as $_POST["delete"] from AdminMain.php ->
		 * ManageClasses.php
		 */
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "admin");
		tester.setTextField("password", "admin");
		tester.submit();
		tester.assertMatch("Manage Classes");
		// manage
		tester.clickLinkWithText("Classes");
		tester.assertMatch("Manage Classes");
		tester.clickButtonWithText(" Edit ");

		// send data through post
	}

	@Test
	public void testVulnerabilityEditGrade() {
		/* TeacherMain.php -> ManageGrade.php -> Edit(=7) */
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "teacher");
		tester.setTextField("password", "teacher");
		tester.submit();
		tester.clickLinkWithText("Classes");
		tester.assertMatch("Manage Classes");
		tester.selectOptionsByValues("semester", "First Semester")
		tester.clickButtonWithText(" Edit ");
	}

	@Test
	public void testVulnerabilityEditParent() {
		/* AdminMain.php -> ManageParents -> Edit (=24) */
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "admin");
		tester.setTextField("password", "admin");
		tester.submit();
		tester.assertMatch("Manage Classes");
		// manage
		tester.clickLinkWithText("Parents");
		tester.assertMatch("Manage Parents");
		tester.clickButtonWithText(" Edit ");

	}

	@Test
	public void testVulnerabilityEditStudent() {
		/* AdminMain.php -> ManageStudent.php -> Edit(=21) */
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "admin");
		tester.setTextField("password", "admin");
		tester.submit();
		tester.assertMatch("Manage Classes");
		// manage
		tester.clickLinkWithText("Students");
		tester.assertMatch("Manage Students");
		tester.clickButtonWithText(" Edit ");
	}

	@Test
	public void testVulnerabilityEditUser() {
		/* Admin.php -> ManageUser.php -> ManageUser.php */
		tester.beginAt("index.php");
		tester.assertMatch("Today's Message");

		tester.setTextField("username", "admin");
		tester.setTextField("password", "admin");
		tester.submit();
		tester.assertMatch("Manage Classes");
		// manage
		tester.clickLinkWithText("Users");
		tester.assertMatch("Manage Users");
		tester.clickButtonWithText(" Edit ");
	}

	@After
	public void cleanUp() {

		if (previousValue != null) {
			tester.beginAt("index.php");

			tester.setTextField("username", "schoolmate");
			tester.setTextField("password", "schoolmate");
			tester.submit();

			tester.clickLinkWithText("School");

			tester.assertMatch("Manage School Information");
			tester.setTextField("sitetext", previousValue);
			tester.clickButtonWithText(" Update ");

			tester.clickLinkWithText("Log Out");
		}

	}

}