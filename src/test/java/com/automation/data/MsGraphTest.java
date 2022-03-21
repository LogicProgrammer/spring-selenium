package com.automation.data;

import com.automation.framework.utilities.ConfigUtils;
import com.automation.framework.utilities.TableFormatter;
import com.automation.framework.utilities.msgraph.ExcelClient;
import com.automation.framework.utilities.msgraph.MsAuthenticator;
import com.microsoft.graph.models.WorkbookTable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MsGraphTest extends AbstractTestNGSpringContextTests {

    ConfigUtils utils = new ConfigUtils();


    @Test
    public void tablesTest(){
        utils.load("src/main/resources/test-data.yaml");
        String userId =  utils.getDecodedValue("graph.user_id");
        MsAuthenticator msAuthenticator = new MsAuthenticator();
        ExcelClient client = new ExcelClient(msAuthenticator.clientCredentials()
                ,userId);
        List<List<String>> table1 = client.getTables("01H3UXEIYKTZRHTSIYAZGJWFA2BLM5ML2G","tables","simpleTable");
        System.out.println(TableFormatter.formatAsTable(table1));
        List<List<String>> table2 = client.getRawTableContentFromSheet("01H3UXEIYKTZRHTSIYAZGJWFA2BLM5ML2G","rawTable");
        System.out.println(TableFormatter.formatAsTable(table2));

        List<List<String>> table3 = client.getTableDataUsingKey("01H3UXEIYKTZRHTSIYAZGJWFA2BLM5ML2G",
                "rawTable",new ArrayList<String>(){{
                    add("header1");
                    add("header2");
                    add("header5");
                    add("header10");
                }});
        System.out.println(TableFormatter.formatAsTable(table3));

    }

}
