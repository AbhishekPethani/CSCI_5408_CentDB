package TransactionManagement;

import FileParsing.Transaction;

import java.util.*;

/**
 * @author Rajath Bharadwaj
 * @author Devarshi Vyas
 */

public class TransactionManager {
    private TransactionManager(){}

    ArrayList<Map> transactions = new ArrayList<>();
    ArrayList<Map> readingTransactions = new ArrayList<>();
    ArrayList<Map> writingTransactions = new ArrayList<>();
    Transaction transaction = new Transaction();

    public static TransactionManager transactionManager = null;
    public TransactionManager getInstance(){
        if (transactionManager == null) {
            transactionManager = new TransactionManager();
        }
        return transactionManager;
    }


    public void transactionStart(String transactionName, long timeStamp) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("transactionName",transactionName);
        hashMap.put("timestamp", timeStamp);
        transactions.add(hashMap);
        transaction.startTransaction(transactionName);
    }

    public void commitTransaction(String transactionName){
        transaction.commitTransaction(transactionName);
    }

    public void rollbackTransaction(String transactionName){
        transaction.abortTransaction(transactionName);
    }

    private boolean isConflicting(String operationType, String tableName, String transactionName){
        boolean write = false;
        boolean read = true;

        if(operationType == "INSERT_INTO" ||operationType == "UPDATE" || operationType == "DELETE_FROM") {
            write = true;
            read = false;
        }
        HashMap<String, Object> currentTransaction = (HashMap<String, Object>) transactions.stream().filter(element -> element.get("transactionName").equals(transactionName));
        ArrayList<Map<String, Object>> readingTheResource = (ArrayList<Map<String, Object>>) readingTransactions.stream().filter(element -> element.get("tableName").equals(tableName));
        ArrayList<Map<String, Object>> writingTheResource = (ArrayList<Map<String, Object>>) writingTransactions.stream().filter(element -> element.get("tableName").equals(tableName));

        if ((write && writingTheResource.size() > 0) || (write && readingTheResource.size() > 0) || (read && writingTheResource.size() > 0)) {
            this.writingTransactions.add(currentTransaction);
            return true;

        } else {
            this.readingTransactions.add(currentTransaction);
            return false;
        }
    }

    public void currentQuery(Map<String,Object> validatedQuery, String transactionName, String tableName){
        HashMap<String, Object> currentTransaction = (HashMap<String, Object>) transactions.stream().filter(element -> element.get("transactionName").equals(transactionName));
        ArrayList<Map<String, Object>> readingTheResource = (ArrayList<Map<String, Object>>) readingTransactions.stream().filter(element -> element.get("tableName").equals(tableName));
        ArrayList<Map<String, Object>> writingTheResource = (ArrayList<Map<String, Object>>) writingTransactions.stream().filter(element -> element.get("tableName").equals(tableName));

        if (isConflicting((String) validatedQuery.get("queryType"), (String) validatedQuery.get("tableName"), transactionName)) {
            if (writingTheResource.size() > 0 && readingTheResource.size() > 0 &&(Long) currentTransaction.get("timestamp") < (Long) writingTheResource.get(0).get("timestamp")) {
                transaction.acquireLockTransaction(transactionName);
                transaction.blockTransaction(String.valueOf(writingTheResource.get(0).get("transactionName")));
            }
        } else {
             if(validatedQuery.get("queryType") == "DELETE_FROM"){
                 transaction.transactionDelete(transactionName, tableName, (TreeMap<String, List<Object>>) validatedQuery.get("conditionColumnAndValue"));
             }
             else if(validatedQuery.get("queryType") == "INSERT_INTO"){
                 transaction.transactionInsert(transactionName, tableName, (TreeMap<String, Object>) validatedQuery.get("columnAndValue"));
             }
             else if(validatedQuery.get("queryType") == "UPDATE"){
                 transaction.transactionInsert(transactionName, tableName, (TreeMap<String, Object>) validatedQuery.get("columnAndValue"));
             }
             else{

             }
        }
    }
}
