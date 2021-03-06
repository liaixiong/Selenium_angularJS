package angularPage.PageTest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;

import angularPage.DriverFactory.DriverFactory;
import angularPage.DriverFactory.DriverType;
import angularPage.Page.DialogAPage;
import angularPage.abstractObjects.AbstractPageModalObject;
import angularPage.abstractObjects.AbstractSelenium;

@RunWith(Parameterized.class)
public class EditModalDialogSeleniumTest extends AbstractSelenium {

	private DialogAPage dialogAPage;
	private DriverType driverType;
	
	@Parameters
	public static Collection<DriverType[]> data() {
		DriverType[][] driver = new DriverType[][] { { DriverType.Chrome }, {DriverType.Opera}, {DriverType.InternetExplorer} };
	    return Arrays.asList(driver);
	}
	
	public EditModalDialogSeleniumTest(DriverType driverType) {
		this.driverType = driverType;
	}
	
	@Override
	@Before
	public void setUp() {
		WebDriver driver = DriverFactory.getDriver(driverType);
		setDriver(driver);
		super.setUp();
		dialogAPage = openDialogAPage();
	}
	@Test
	public void editModalDialogShouldHaveTheSameDataAsRow() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		//when
		List<String> clickedRow = dialogAPage.clickRowOfTable(index).getRow(index);
		List<String> dialogInputsText = dialogAPage.clickEditButton().getDialogInputsText();
		//then
		assertArrayEquals(clickedRow.toArray(), dialogInputsText.toArray());
	}
	
	@Test
	public void emptyTitleShouldBlockSaveButton() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		AbstractPageModalObject editModalDialog = dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("").setAuthor("author").setGenre("it").setYear("2000");
		//then
		assertFalse(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void emptyAuthorShouldBlockSaveButton() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		AbstractPageModalObject editModalDialog =  dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("title").setAuthor("").setGenre("it").setYear("2000");
		//then
		assertFalse(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void emptyTitleAndAuthorShouldBlockSaveButton() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		AbstractPageModalObject editModalDialog =  dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("").setAuthor("").setGenre("it").setYear("2000");
		//then
		assertFalse(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void yearUnder1800ShouldBlockSaveButton() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		AbstractPageModalObject editModalDialog =  dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("title").setAuthor("author").setGenre("it").setYear("1799");
		//then
		assertFalse(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void yearHigherThenNextYearShouldBlockSaveButton() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;		int year = LocalDate.now().getYear()+2;
		AbstractPageModalObject editModalDialog =  dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("title").setAuthor("author").setGenre("it").setYear(String.valueOf(year));
		//then
		assertFalse(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void titleAndAuthorShouldBeEnoughToSave() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		AbstractPageModalObject editModalDialog = dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("title").setAuthor("author").setGenre("").setYear("");
		//then
		assertTrue(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void allDataShouldPermitSave() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		AbstractPageModalObject editModalDialog =   dialogAPage.clickRowOfTable(index).clickEditButton();
		//when
		editModalDialog.setTitle("title").setAuthor("author").setGenre("it").setYear("2016");
		//then
		assertTrue(editModalDialog.confirmButtonIsEnable());
	}
	
	@Test
	public void allDataShouldSaveAndUpdateTable() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		List<String> data = new ArrayList<String>();
		data.add("title");
		data.add("author");
		data.add("it");
		data.add("2017");
		//when
		dialogAPage.clickRowOfTable(index).clickEditButton()
			.setTitle(data.get(0))
			.setAuthor(data.get(1))
			.setGenre(data.get(2))
			.setYear(data.get(3))
			.clickConfirmButton();
		
		List<String> editedRow = dialogAPage.getRow(index);
		//then
		assertArrayEquals(data.toArray(), editedRow.toArray());
	}
	
	@Test
	public void cancelButtonShouldCloseModalDialog() throws InterruptedException{
		//given
		int countOfRow = dialogAPage.getCountOfRow();
		int index = countOfRow-1;
		//when
		AbstractPageModalObject editModalDialog =  dialogAPage.clickRowOfTable(index).clickEditButton();
		//then
		assertTrue(dialogAPage.modalDialogIsDisplayed());
		//when
		editModalDialog.clickCancelButton();
		//then
		assertFalse(dialogAPage.modalDialogIsDisplayed());
	}

}
