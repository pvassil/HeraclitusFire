package test.mainEngine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import datamodel.MonthSchemaStats;
import datamodel.SchemaHeartbeatElement;
import datamodel.SchemaLevelInfo;


public class StagelessSchemaStatsMainEngineTest {

	/**
	 * To test {@link mainEngine.SchemaStatsMainEngine#produceSchemaFiguresAndStats()}.
	 * use this one instead of the TableStatsMainEngineTest
	 * to avoid creating stages.
	 *  
	 */
	
	
	@Test
	void testProduceFiguresAndStats_T0_V1_Egee_noDates() {
		StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Egee", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 
		String schemaLevelInfoString = "Egee\t\t\t\t17\t6\t10\t34\t71\t6\t2\t31\t10\t28\t12\t18\t1\t59\t41\t100\t3.471\t\t\t2.412\t\t\t5.882\t\t\t1.667\t3\t2\t0.19\t0.18\t95\t61\t13\t13\t0.81\t0.76\t39\t39\t16\t0.0\t0.0\t0.94";
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessSchemaStatsMainEngine.loadData("resources/Egee/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,18);
		assertEquals(inputTupleCollection.size(),17);
		assertTrue(header.equals(headerExpected));
		
		stagelessSchemaStatsMainEngine.produceFiguresAndStats();
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().size(), 0);
		
		File infoFileProduced = new File("resources/test/Profiling/Egee_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = infoFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = stagelessSchemaStatsMainEngine.extractSchemaLevelInfo("Egee", inputTupleCollection, "resources/test/Profiling", false);
		assertTrue(schemaLevelInfoString.equals(schemaLevelInfo.toString()));
		Long newTimeStamp = infoFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		File htmlFileProduced = new File("resources/test/Profiling/Egee_Summary.html"); 
		originalTimeStamp = htmlFileProduced.lastModified();
		int produceSummaryReturnCode = stagelessSchemaStatsMainEngine.produceSummaryHTML("Egee", "resources/Egee/figures", "resources/test/Profiling", "resources/test/Profiling");
		assertEquals(produceSummaryReturnCode, 0);
		newTimeStamp = htmlFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		File statsFileProduced = new File("resources/test/Profiling/Egee_MonthlySchemaStats.tsv"); 
		originalTimeStamp = statsFileProduced.lastModified();
		ArrayList<MonthSchemaStats> monthlySchemaStatsCollection = stagelessSchemaStatsMainEngine.extractMonthlySchemaStats("Egee", inputTupleCollection, "resources/test/Profiling", false);
		assertEquals(monthlySchemaStatsCollection.size(), 0);
		newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp == originalTimeStamp);
	}
	
	@Test
	void testProduceFiguresAndStats_T0_V0_Atlas_HappyDay() {
		StagelessSchemaStatsMainEngine stagelessSchemaStatsMainEngine = new StagelessSchemaStatsMainEngine("resources/Atlas", null);
		
		ArrayList<String> headerExpected = new ArrayList<String>(Arrays.asList(
				"trID", "epochTime", "oldVer", "newVer", "humanTime", "distFromV0InDays", "runningYearFromV0", 
				"runningMonthFromV0", "#numOldTables", "#numNewTables", "#numOldAttrs", "#numNewAttrs", 
				"tablesIns", "tablesDel", "attrsInsWithTableIns", "attrsbDelWithTableDel", "attrsInjected", 
				"attrsEjected", "attrsWithTypeUpd", "attrsInPKUpd", "tableDelta", "attrDelta", "attrBirthsSum", 
				"attrDeathsSum", "attrUpdsSum", "Expansion", "Maintenance", "TotalAttrActivity")); 
		String schemaLevelInfoString = "Atlas\t971\t32\t3\t85\t56\t73\t709\t858\t34\t17\t233\t122\t154\t116\t245\t1\t387\t484\t871\t4.553\t12.094\t129.0\t5.694\t15.125\t161.333\t10.247\t27.219\t290.333\t1.304\t19\t18\t0.27\t0.22\t1366\t657\t51\t51\t0.73\t0.6\t214\t214\t70\t2.19\t2.66\t0.82";
		ArrayList<String> header = new ArrayList<String>();
		ArrayList<SchemaHeartbeatElement> inputTupleCollection = new ArrayList<SchemaHeartbeatElement>();
		
		
		//TODO: Fix! why does this fail, if I comment out the loadData() call?
		int numRows = stagelessSchemaStatsMainEngine.loadData("resources/Atlas/results/SchemaHeartbeat.tsv", "\t", true, 28, header, inputTupleCollection);
		assertEquals(numRows,86);
		assertEquals(inputTupleCollection.size(),85);
		assertTrue(header.equals(headerExpected));
		
		stagelessSchemaStatsMainEngine.produceFiguresAndStats();
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().size(), 4);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(0).size(),1);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(1).size(),36);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(2).size(),40);
		assertEquals(stagelessSchemaStatsMainEngine.getTuplesPerRYFV0Collection().get(3).size(),8);

		File infoFileProduced = new File("resources/test/Profiling/Atlas_SchemaLevelInfo.tsv"); 
		Long originalTimeStamp = infoFileProduced.lastModified();
		SchemaLevelInfo schemaLevelInfo = stagelessSchemaStatsMainEngine.extractSchemaLevelInfo("Atlas", inputTupleCollection, "resources/test/Profiling", true);
		assertTrue(schemaLevelInfoString.equals(schemaLevelInfo.toString()));
		Long newTimeStamp = infoFileProduced.lastModified();

		assertTrue(newTimeStamp > originalTimeStamp);
		
		File htmlFileProduced = new File("resources/test/Profiling/Atlas_Summary.html"); 
		originalTimeStamp = htmlFileProduced.lastModified();
		int produceSummaryReturnCode = stagelessSchemaStatsMainEngine.produceSummaryHTML("Atlas", "resources/Atlas/figures", "resources/test/Profiling", "resources/test/Profiling");
		assertEquals(produceSummaryReturnCode, 0);
		newTimeStamp = htmlFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
		
		File statsFileProduced = new File("resources/test/Profiling/Atlas_MonthlySchemaStats.tsv"); 
		originalTimeStamp = statsFileProduced.lastModified();
		ArrayList<MonthSchemaStats> monthlySchemaStatsCollection = stagelessSchemaStatsMainEngine.extractMonthlySchemaStats("Atlas", inputTupleCollection, "resources/test/Profiling", true);
		assertEquals(monthlySchemaStatsCollection.size(), 33);
		newTimeStamp = statsFileProduced.lastModified();
		assertTrue(newTimeStamp > originalTimeStamp);
	}

}
