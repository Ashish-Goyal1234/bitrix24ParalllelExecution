package org.bitrix.datadriven;



import org.testng.annotations.DataProvider;

import com.bitrix.base.Baseclassparallel;

public class DataProviderPage extends Baseclassparallel{
    
    private static String LoginSheetName                                              = "Logintest";
    private static String CreateContactsSheetName                                              = "createContacts";
    
    @DataProvider(name = "CredentailsTestData12")
    public static Object[][] getCredentailsTestData() {
            return DataDrivenScript.readSheetData("TestData//Login.xlsx", LoginSheetName);
    }
    
    @DataProvider(name = "CreteContactsTestData")
    public static Object[][] createContactsTestData() {
            return DataDrivenScript.readSheetData("TestData//TestData.xlsx", CreateContactsSheetName);
    }
    
    
    
}
