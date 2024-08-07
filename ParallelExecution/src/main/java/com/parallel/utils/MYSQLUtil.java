package com.parallel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import static com.parallel.base.TestBase.prop;
import static com.parallel.listeners.ListenerBase.testCaseCounter;
import static com.parallel.listeners.ListenerBase.testRunId;

public class MYSQLUtil {

	 static String host, port, user, password, url;
	    static String[][] sqlData;
	    static Connection con;
	    ResultSet rs;
	    static Vector<String> columnNames = new Vector<String>();
	    public static Connection connSqlite = null;
	    final static Logger log  = LoggerFactory.getLogger(MYSQLUtil.class);
	    static final DecimalFormat df = new DecimalFormat("0.00");

	    public void setUpConnection() throws SQLException, ClassNotFoundException {


	        host = prop.getProperty("MYSQLhost");
	        port = prop.getProperty("MYSQLport");
	        user = prop.getProperty("MYSQLuser");
	        password = prop.getProperty("MYSQLpassword");
	        url = "jdbc:mysql://" + host + ":" + port + "/IDS";


	        Class.forName("com.mysql.cj.jdbc.getDriver()");
	        con = DriverManager.getConnection(url, user, password);


	    }

	    public String[][] executeQuery(String query) {
	        try {
	            Statement s = con.createStatement();
	            ResultSet rs = s.executeQuery(query);

	            if (rs != null) {
	                ResultSetMetaData columns = rs.getMetaData();
	                int i = 0;
	                int cnt = columns.getColumnCount();
	                sqlData = new String[2][cnt];

	                while (i < columns.getColumnCount()) {
	                    i++;

	                    columnNames.add(columns.getColumnName(i));
	                    sqlData[0][i - 1] = columns.getColumnName(i);
	                }

	                while (rs.next()) {
	                    for (i = 0; i < columnNames.size(); i++) {
	                        sqlData[1][i] = rs.getString(columnNames.get(i));

	                    }

	                }

	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return sqlData;
	    }


	    public ResultSet executeQueryresult(String query) {
	        try {
	            Statement s = con.createStatement();
	            rs = s.executeQuery(query);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return rs;
	    }


	    public String getcolumnValue(String[][] sqldata, String columnName) {
	        int i = 0;
	        int columnCnt = 0;
	        String coulmnValue = null;
	        while (i < 1) {
	            for (int j = 0; j < sqldata[i].length; j++) {
	                if (sqldata[i][j].equalsIgnoreCase(columnName)) {
	                    columnCnt = j;
	                    break;
	                }

	            }

	            i++;
	        }
	        i = 1;

	        coulmnValue = sqldata[i][columnCnt];

	        return coulmnValue;

	    }

	    public void closeConnection() {
	        // Closing DB Connection
	        try {
	            if (con != null) {
	                con.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static void connectDb() throws Exception {
	        //connectSqlite();
	        connectRemitraMySql();
	    }

	    public static void connectSqlite() {
	        try {
	            String url = "jdbc:sqlite:C:\\Users\\bsanthan\\AppData\\Roaming\\DBeaverData\\workspace6\\.metadata\\sample-database-sqlite-1\\Chinook.db";
	            // db parameters
	            connSqlite = DriverManager.getConnection(url);
	            // create a connection to the database
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }

	    public static void connectRemitraMySql() throws Exception {
	        String dbUrl = "c3duremitdb01.premierinc.com";
	        String dbName = "automation_report";
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        String connectionInfo = String.format("jdbc:mysql://%s/%s", dbUrl, dbName);
	        connSqlite = DriverManager.getConnection(connectionInfo, "rem_app", "remitra1");
	    }

	    public static void closeSqliteConnection() {
	        try {
	            if (connSqlite != null) {
	                connSqlite.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static void insertModuleSummary(List<Map> listMap) throws Exception {
	        try{
	        for(Map map : listMap) {
	            connectDb();
	            String query = "INSERT INTO MODULE_SUMMARY\n" +
	                    "(TEST_RUN_ID, MODULE_ID, TOTAL_TESTCASES_EXECUTED, TOTAL_TESTCASES_PASSED, TOTAL_TESTCASES_FAILED, " +
	                    "TOTAL_TESTCASES_SKIPPED, PASS_PERCENTAGE, START_TIME, END_TIME)\n" +
	                    "VALUES('testRunIDVal', " +
	                    "'" + map.get("Module Name").toString() + "'," +
	                    "'" + (Integer.parseInt(map.get("Tests Passed").toString()) + Integer.parseInt(map.get("Tests Failed").toString()) + Integer.parseInt(map.get("Tests Skipped").toString())) + "'," +
	                    "'" + map.get("Tests Passed").toString() + "'," +
	                    "'" + map.get("Tests Failed").toString() + "'," +
	                    "'" + map.get("Tests Skipped").toString() + "'," +
	                    "'passPercentage'," +
	                    "'startTimeVal'," +
	                    "'endTimeVal')";
	            Statement stmt = connSqlite.createStatement();
	            stmt.executeUpdate(query);
	        }}finally{
	            closeSqliteConnection();
	        }
	    }

	    public static void insertExecutionSummary(Map map) throws Exception {
	        connectDb();
	        String query="INSERT INTO EXECUTION_SUMMARY\n" +
	                "(TEST_RUN_ID, APP_NAME, RUN_ENVIRONMENT, TEST_TYPE, TOTAL_TESTCASES_EXECUTED, TOTAL_TESTCASES_PASSED, " +
	                "TOTAL_TESTCASES_FAILED, TOTAL_TESTCASES_SKIPPED, PASS_PERCENTAGE, EXECUTED_BY, START_TIME, END_TIME)\n" +
	                "VALUES('"+ map.get("TEST_RUN_ID").toString()+"'," +
	                "'"+ map.get("APP_NAME").toString()+"'," +
	                "'"+ map.get("RUN_ENVIRONMENT").toString()+"'," +
	                "'"+ map.get("TEST_TYPE").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_EXECUTED").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_PASSED").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_FAILED").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_SKIPPED").toString()+"'," +
	                "'"+ map.get("PASS_PERCENTAGE").toString()+"'," +
	                "'"+ map.get("EXECUTED_BY").toString()+"'," +
	                "'"+ map.get("START_TIME").toString()+"'," +
	                "'"+ map.get("END_TIME").toString()+"')";
	        Statement stmt = connSqlite.createStatement();
	        stmt.executeUpdate(query);
	        closeSqliteConnection();
	    }
	    
	    public static void insertQaTestCaseSummary(Map map) throws Exception {
	        connectDb();
	        String query="INSERT INTO TESTCASE_SUMMARY\n" +
	                "(TEST_RUN_ID, TEST_CASE_ID, TEST_CASE_NAME, TEST_CASE_STATUS, EXCEPTION_ERROR_MESSAGE," +
	                " MODULE_ID, PAGE_ID,APP_NAME, RUN_ENVIRONMENT, TEST_TYPE, USER_ROLE, EXECUTED_BY, START_TIME, END_TIME)\n" +
	                "VALUES('"+ map.get("TEST_RUN_ID").toString()+"'," +
	                "'"+ map.get("TEST_CASE_ID").toString()+"'," +
	                "'"+ map.get("TEST_CASE_NAME").toString()+"'," +
	                "'"+ map.get("TEST_CASE_STATUS").toString()+"'," +
	                "'"+ map.get("EXCEPTION_ERROR_MESSAGE").toString()+"'," +
	                "'"+ map.get("MODULE_ID").toString()+"'," +
	                "'"+ map.get("PAGE_ID").toString()+"'," +
	                "'"+ map.get("APP_NAME").toString()+"'," +
	                "'"+ map.get("RUN_ENVIRONMENT").toString()+"'," +
	                "'"+ map.get("TEST_TYPE").toString()+"'," +
	                "'"+ map.get("USER_ROLE").toString()+"'," +
	                "'"+ map.get("EXECUTED_BY").toString()+"'," +
	                "'"+ map.get("START_TIME").toString()+"'," +
	                "'"+ map.get("END_TIME").toString()+"')";
	        Statement stmt = connSqlite.createStatement();
	        stmt.executeUpdate(query);
	        closeSqliteConnection();
	    }

	    public static void insertModuleSummary(Map map) throws Exception {
	        connectDb();
	        String query="INSERT INTO MODULE_SUMMARY\n" +
	                "(TEST_RUN_ID, MODULE_ID, TOTAL_TESTCASES_EXECUTED, TOTAL_TESTCASES_PASSED, " +
	                "TOTAL_TESTCASES_FAILED, TOTAL_TESTCASES_SKIPPED, PASS_PERCENTAGE, USER_ROLE, EXECUTED_BY, START_TIME, END_TIME)\n" +
	                "VALUES('"+ map.get("TEST_RUN_ID").toString()+"'," +
	                "'"+ map.get("MODULE_ID").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_EXECUTED").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_PASSED").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_FAILED").toString()+"'," +
	                "'"+ map.get("TOTAL_TESTCASES_SKIPPED").toString()+"'," +
	                "'"+ map.get("PASS_PERCENTAGE").toString()+"'," +
	                "'"+ map.get("USER_ROLE").toString()+"'," +
	                "'"+ map.get("EXECUTED_BY").toString()+"'," +
	                "'"+ map.get("START_TIME").toString()+"'," +
	                "'"+ map.get("END_TIME").toString()+"')";
	        Statement stmt = connSqlite.createStatement();
	        stmt.executeUpdate(query);
	        closeSqliteConnection();
	    }

	    public  static ResultSet getQueryResults(String sql)  {
	        ResultSet rs = null;
	        try {
	            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	            rs =   stmt.executeQuery(sql);
	        }
	        catch (Exception e)  { log.error("ERROR CAUGHT in executeQuery():  " + e.toString() + "\n"+sql);  }
	        return rs;
	    }

	    // Gets record data from the result Set of Column names.
	    // RETURNS: Map, or empty Map - if no records found.
	    public static Map<String, String> getResultMap(ResultSet record, String[] columnNames) {
	        Map<String, String> result = new HashMap<String, String>();
	        try {
	            if (record.next()) {
	                for (int i = 0; i < columnNames.length; i++) {
	                    result.put(columnNames[i], record.getString(columnNames[i]));
	                }
	            }
	        } catch (SQLException e) {
	            log.error("SQLException occurred " + e.getMessage());
	        } finally {
	            if (record != null) try {
	                record.close();
	            } catch (SQLException ignore) {
	            }
	        }
	        return result;
	    }

	    public static Map<String, String> getResultSetInMap(String query) throws Exception {
	        Map returnMap = new HashMap<String, String>();
	        connectDb();
	        Statement stmt = connSqlite.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            ResultSetMetaData metadata = rs.getMetaData();
	            int columnCount = metadata.getColumnCount();
	            for (int j = 1; j <= columnCount; j++) {
	                String columnName = metadata.getColumnName(j).toString();
	                returnMap.put(columnName,rs.getString(columnName));
	            }
	        break;
	        }
	        closeSqliteConnection();
	        return returnMap;
	    }

	    public static List<Map<String, String>> getResultSetInListMap(String query) throws Exception {
	        List<Map<String, String>> returnVal = new ArrayList<>();
	        connectDb();
	        Statement stmt = connSqlite.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            Map returnMap = new HashMap<String, String>();
	            ResultSetMetaData metadata = rs.getMetaData();
	            int columnCount = metadata.getColumnCount();
	            for (int j = 1; j <= columnCount; j++) {
	                String columnName = metadata.getColumnName(j).toString();
	                returnMap.put(columnName,rs.getString(columnName));
	            }
	            returnVal.add(returnMap);
	        }
	        closeSqliteConnection();
	        return returnVal;
	    }

	    public static void updateModuleSummaryTable(Map map,String testRunId, String moduleId) throws Exception {
	        connectDb();
	        if(!map.isEmpty()){
	            String query = "UPDATE module_summary SET \n" +
	                    "TOTAL_TESTCASES_EXECUTED='"+map.get("TOTAL_TESTCASES_EXECUTED").toString()+"', \n" +
	                    "TOTAL_TESTCASES_PASSED='"+map.get("TOTAL_TESTCASES_PASSED").toString()+"', \n" +
	                    "TOTAL_TESTCASES_FAILED='"+map.get("TOTAL_TESTCASES_FAILED").toString()+"', \n" +
	                    "TOTAL_TESTCASES_SKIPPED='"+map.get("TOTAL_TESTCASES_SKIPPED").toString()+"', \n" +
	                    "PASS_PERCENTAGE='"+String.valueOf(df.format((double)(Float.parseFloat(map.get("TOTAL_TESTCASES_PASSED").toString()) * 100f)/ Float.parseFloat(map.get("TOTAL_TESTCASES_EXECUTED").toString())))+"', \n" +
	                    "END_TIME='"+String.valueOf(new Timestamp(System.currentTimeMillis()))+"'\n" +
	                    "where TEST_RUN_ID='"+testRunId+"' and MODULE_ID ='"+moduleId+"'";
	            Statement stmt = connSqlite.createStatement();
	            stmt.executeUpdate(query);
	        }
	        closeSqliteConnection();
	    }

	    public static void updateExecutionSummaryTable(Map map,String testRunId) throws Exception {
	        connectDb();
	        if(!map.isEmpty()){
	            String query = "UPDATE execution_summary SET \n" +
	                    "TOTAL_TESTCASES_EXECUTED='"+map.get("TOTAL_TESTCASES_EXECUTED").toString()+"', \n" +
	                    "TOTAL_TESTCASES_PASSED='"+map.get("TOTAL_TESTCASES_PASSED").toString()+"', \n" +
	                    "TOTAL_TESTCASES_FAILED='"+map.get("TOTAL_TESTCASES_FAILED").toString()+"', \n" +
	                    "TOTAL_TESTCASES_SKIPPED='"+map.get("TOTAL_TESTCASES_SKIPPED").toString()+"', \n" +
	                    "PASS_PERCENTAGE='"+String.valueOf(df.format((double)(Float.parseFloat(map.get("TOTAL_TESTCASES_PASSED").toString()) * 100f)/ Float.parseFloat(map.get("TOTAL_TESTCASES_EXECUTED").toString())))+"', \n" +
	                    "END_TIME='"+String.valueOf(new Timestamp(System.currentTimeMillis()))+"'\n" +
	                    "where TEST_RUN_ID='"+testRunId+"'";
	            Statement stmt = connSqlite.createStatement();
	            stmt.executeUpdate(query);
	        }
	        closeSqliteConnection();
	    }

	    private static String getUpdateQuery(Map map, String tableName) {
	        String query = "UPDATE "+tableName+" SET ";
	        for (Object keys : map.keySet()) {
	            query = query+ keys.toString() +" = '"+map.get(keys)+"',";
	        }
	        return map.keySet().toString().replaceAll("\\[|\\]", "");
	    }

	    public static String getTestCaseCode(String moduleName) throws Exception {
	        String returnString = "";
	        String query = "select * from qa_module_details WHERE APP_NAME = 'Remitra' and MODULE_NAME = '"+moduleName+"'";
	        connectDb();
	        Statement stmt = connSqlite.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            returnString = rs.getString("TEST_CASE_ID");
	            break;
	        }
	        closeSqliteConnection();
	        return returnString;
	    }

	    public static void updateTestCaseSummaryTable(String testStatus, String testCaseId, String testRunId)
	            throws Exception {
	        connectDb();
	        String query = "UPDATE TESTCASE_SUMMARY SET \n" + "TEST_CASE_STATUS='" + testStatus + "', " + "END_TIME ='"
	                + String.valueOf(new Timestamp(System.currentTimeMillis())) + "' " + "WHERE \n" + "TEST_RUN_ID='"
	                + testRunId + "' AND TEST_CASE_ID = '" + testCaseId + "_" + String.format("%03d", testCaseCounter)
	                + "'";
	        Statement stmt = connSqlite.createStatement();
	        stmt.executeUpdate(query);
	        closeSqliteConnection();
	    }
	     
	    public static Map getTestDetails(String moduleName) throws Exception {

	        String[] testDetails = moduleName.split("-");
	        String product = testDetails[0].trim();
	        String module = testDetails[1].trim();
	        Map mapTestDetails = new HashMap();
	        String query = "select md.MODULE_NAME module_name,\n" +
	                "md.TEST_CASE_ID test_case_id,\n" +
	                "md.MODULE_ID module_id,\n" +
	                "pd.PRODUCT_ID product_id,\n" +
	                "pd.PRODUCT_NAME product_name\n" +
	                "from module_details md \n" +
	                "join product_details pd \n" +
	                "on md.PRODUCT_ID = pd.PRODUCT_ID \n" +
	                "where md.MODULE_NAME = '"+module+"' " +
	                "and pd.PRODUCT_NAME  = '"+product+"' ";
	        connectDb();
	        Statement stmt = connSqlite.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            mapTestDetails.put("module_name", rs.getString("module_name"));
	            mapTestDetails.put("test_case_id", rs.getString("test_case_id"));
	            mapTestDetails.put("module_id", rs.getString("module_id"));
	            mapTestDetails.put("product_id", rs.getString("product_id"));
	            mapTestDetails.put("product_name", rs.getString("product_name"));
	            break;
	        }
	        closeSqliteConnection();
	        return mapTestDetails;
	    }

	    public static Map getTestDetails(String moduleName, String description) throws Exception {

	        String[] testDetails = moduleName.split("-");
	        String product = testDetails[0].trim();
	        String module = testDetails[1].trim();
	        String pageName = description.substring(0,description.indexOf("-")).trim();

	        Map mapTestDetails = new HashMap();
	        String query = "select \n" +
	                "md.MODULE_NAME module_name,\n" +
	                "md.TEST_CASE_ID test_case_id,\n" +
	                "md.MODULE_ID module_id,\n" +
	                "pd.PRODUCT_ID product_id,\n" +
	                "pd.PRODUCT_NAME product_name,\n" +
	                "pgd.PAGE_ID page_id,\n" +
	                "pgd.PAGE_NAME page_name \n" +
	                "from page_details pgd \n" +
	                "join module_details md on md.MODULE_ID = pgd.MODULE_ID\n" +
	                "join product_details pd on md.PRODUCT_ID = pd.PRODUCT_ID  \n" +
	                "where md.MODULE_NAME ='"+module+"' \n" +
	                "and pd.PRODUCT_NAME  = '"+product+"' \n" +
	                "and pgd.PAGE_NAME ='"+pageName+"'";

	        connectDb();
	        Statement stmt = connSqlite.createStatement();
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            mapTestDetails.put("module_name", rs.getString("module_name"));
	            mapTestDetails.put("test_case_id", rs.getString("test_case_id"));
	            mapTestDetails.put("module_id", rs.getString("module_id"));
	            mapTestDetails.put("product_id", rs.getString("product_id"));
	            mapTestDetails.put("product_name", rs.getString("product_name"));
	            mapTestDetails.put("page_id", rs.getString("page_id"));
	            mapTestDetails.put("page_name", rs.getString("page_name"));
	            break;
	        }
	        closeSqliteConnection();
	        return mapTestDetails;
	    }
	    
	    public static void updateTestCaseSummaryTable(String testStatus, String description) throws Exception {
	        connectDb();
	        String query = "UPDATE TESTCASE_SUMMARY SET \n" + "TEST_CASE_STATUS='" + testStatus + "', " + "END_TIME ='"
	                + String.valueOf(new Timestamp(System.currentTimeMillis())) + "' " + "WHERE TEST_CASE_NAME='"
	                + description + "' AND \n" + "TEST_RUN_ID='" + testRunId + "'";
	        Statement stmt = connSqlite.createStatement();
	        stmt.executeUpdate(query);
	        closeSqliteConnection();
	    }
	    
	    public static void updateTestCaseSummaryTable(String testStatus, String description, Throwable exception)
	            throws Exception {
	        String exceptiontxt="";
	        connectDb();
	        if(exception.toString().length()>81)
	        {
	            exceptiontxt=exception.toString().substring(0,80);
	        }
	        else{
	            exceptiontxt=exception.toString();
	        }
	        String query = "UPDATE TESTCASE_SUMMARY SET \n" + "TEST_CASE_STATUS='" + testStatus + "', " + "END_TIME ='"
	                + String.valueOf(new Timestamp(System.currentTimeMillis())) + "', EXCEPTION_ERROR_MESSAGE = '"
	                + exceptiontxt + "' " + "WHERE TEST_CASE_NAME='" + description + "' AND \n" + "TEST_RUN_ID='" + testRunId
	                + "'";
	        Statement stmt = connSqlite.createStatement();
	        stmt.executeUpdate(query);
	        closeSqliteConnection();
	    }

	}

