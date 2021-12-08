package FileParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author zhaoling
 */
public class Transaction implements TransactionInterface {

	private final String path = "Databases/";
	private FileOperation fileOperation = new FileOperation();
	private Map<String, Map<MyThread, String>> transactions;
	private Map<String, ArrayList<TreeMap<String, String>>> tables = new HashMap<String, ArrayList<TreeMap<String, String>>>();
	private Map<String, TreeMap<String, String>> headers = new HashMap<String, TreeMap<String, String>>();
	private String databaseName;
	private String tableName;
	private TreeMap<String, Object> updateColumnAndValue = new TreeMap<String, Object>();
	private TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
	private TreeMap<String, Object> insertColumnAndValue = new TreeMap<String, Object>();
	private String currentWorking;
	public static Object object = new Object();
	
	class MyThread extends Thread {
		
		@Override
		public void run () {
			while (!isInterrupted()) {
				if (currentWorking.equals("update")) 
					updateTable();
				if (currentWorking.equals("insert")) 
					insertTable();
				if (currentWorking.equals("delete")) 
					deleteTable();
				synchronized (object) {
					try {
						object.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public int startTransaction (String transactionName) {
		Transaction transaction = new Transaction();
		MyThread thread = transaction.new MyThread();
		thread.setName(transactionName);
		thread.start();
		Map<MyThread, String> threadInfo = new HashMap<MyThread, String>();
		threadInfo.put(thread, "unlock");
		transactions.put(transactionName, threadInfo);
		return 1;
	}
	
	public int abortTransaction (String transactionName) {
		Thread thread = transactions.get(transactionName).keySet().iterator().next();
		thread.interrupt();
		transactions.remove(transactionName);
		if (transactions.isEmpty()) {
			tables.clear();
			headers.clear();
		}
		return 1;
	}
	
	public int commitTransaction (String transactionName) {
		MyThread thread = transactions.get(transactionName).keySet().iterator().next();
		thread.interrupt();
		transactions.remove(transactionName);
		saveMemoryIntoFile();
		if (transactions.isEmpty()) {
			tables.clear();
			headers.clear();
		}
		return 1;
	}
	
	public int acquireLockTransaction (String transactionName) {
		for (String name : transactions.keySet()) {
			MyThread thread = transactions.get(name).keySet().iterator().next();
			if (name.equals(transactionName)) {
				transactions.get(transactionName).put(thread, "lock");
				return 1;
			}
		}
		return 0;
	}

	public int blockTransaction(String transactionName) {
		for (String name : transactions.keySet()) {
			MyThread thread = transactions.get(name).keySet().iterator().next();
			if (name.equals(transactionName)) {
				transactions.get(transactionName).put(thread, "wait");
				return 1;
			}
		}
		return 0;
	}
	
	public void setDatabase (String databaseName) {
		this.databaseName = databaseName;
	}
	
	public void transactionUpdate (String transactionName, String tableName, TreeMap<String, Object> updateColumnAndValue, TreeMap<String, List<Object>> conditionColumnAndValue) {
		this.tableName = tableName;
		this.conditionColumnAndValue = conditionColumnAndValue;
		this.updateColumnAndValue = updateColumnAndValue;
		this.currentWorking = "update";
		if (!transactions.get(transactionName).values().toArray()[0].equals("wait")) {
			synchronized (transactions.get(transactionName)) {
				transactions.get(transactionName).keySet().toArray()[0].notify();
			}
		}
	}
	
	private void updateTable () {
		if (!tables.containsKey(tableName)) 
			readFileIntoMemory(databaseName, tableName);
		
		ArrayList<TreeMap<String, String>> table = tables.get(tableName);
		for (TreeMap<String, String> row : table) {
			Boolean isFit = fileOperation.checkIsSatisfyCondition(conditionColumnAndValue, row);
			if (isFit) 
				for (String update : updateColumnAndValue.keySet()) 
					row.put(update, updateColumnAndValue.get(update).toString());
		}
	}
	
	public void transactionInsert (String transactionName, String tableName, TreeMap<String, Object> insertColumnAndValue) {
		this.tableName = tableName;
		this.insertColumnAndValue = insertColumnAndValue;
		this.currentWorking = "insert";
		if (!transactions.get(transactionName).values().toArray()[0].equals("wait")) {
			synchronized (transactions.get(transactionName)) {
				transactions.get(transactionName).keySet().toArray()[0].notify();
			}
		}
	}
	
	private void insertTable () {
		if (!tables.containsKey(tableName)) 
			readFileIntoMemory(databaseName, tableName);
		
		ArrayList<TreeMap<String, String>> table = tables.get(tableName);
		TreeMap<String, String> header = headers.get(tableName);
		TreeMap<String, String> newRow = new TreeMap<String, String>();
		for (String s : header.keySet()) {
			if (insertColumnAndValue.containsKey(s)) 
				newRow.put(s, insertColumnAndValue.get(s).toString());
			else if (s.equals("-id"))
				newRow.put(s, String.valueOf(table.size()+1));
			else
				newRow.put(s, "NULL");
		}
		table.add(newRow);
	}
	
	public void transactionDelete (String transactionName, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		this.tableName = tableName;
		this.conditionColumnAndValue = conditionColumnAndValue;
		this.currentWorking = "delete";
		if (!transactions.get(transactionName).values().toArray()[0].equals("wait")) {
			synchronized (transactions.get(transactionName)) {
				transactions.get(transactionName).keySet().toArray()[0].notify();
			}
		}
	}
	
	private void deleteTable () {
		if (!tables.containsKey(tableName)) 
			readFileIntoMemory(databaseName, tableName);
		
		ArrayList<TreeMap<String, String>> table = tables.get(tableName);
		for (TreeMap<String, String> row : table) {
			Boolean isFit = fileOperation.checkIsSatisfyCondition(conditionColumnAndValue, row);
			if (isFit) {
				table.remove(row);
			}
		}
	}
	
	private void readFileIntoMemory (String databaseName, String tableName) {
		String tableFileName = path + databaseName + "/" + tableName + ".tb";
		TreeMap<String, String> header = fileOperation.getTableHeader(tableFileName);
		this.headers.put(tableName, header);
		TreeMap<String, String> contents = fileOperation.getTableContent(tableFileName);
		ArrayList<TreeMap<String, String>> table = new ArrayList<TreeMap<String, String>>();
		for (String id : contents.keySet()) {
			TreeMap<String, String> content = fileOperation.transferRowStringToRowMap(contents.get(id), header);
			table.add(content);
		}
		this.tables.put(tableName, table);
	}
	
	private void saveMemoryIntoFile () {
		for (String tableName : tables.keySet()) {
			ArrayList<TreeMap<String, String>> table = tables.get(tableName);
			String tableFileName = path + databaseName + "/" + tableName + ".tb";
			TreeMap<String, String> header = headers.get(tableName);
			File tableFile = new File(tableFileName);
			List<String> fileContent = new ArrayList<String>();

			try {
				tableFile.createNewFile();
				BufferedReader in = new BufferedReader(new FileReader(tableFile));
				String firstLine = in.readLine();
				fileContent.add(firstLine);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int id = 1;
			for (TreeMap<String, String> row : table) {
				String tableContent = String.valueOf(id) + "/";
				for (String column : header.keySet()) {
					tableContent += row.get(column) + "/";
				}
				fileContent.add(tableContent);
				id++;
			}
			
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(tableFile));
				for (String row : fileContent) {
					out.write(row + "\r\n");
				}
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
