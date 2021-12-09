import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rajath Bharadwaj
 * @author Devarshi Vyas
 */

public class TransactionManager {
    private TransactionManager(){}

    ArrayList<Map> transactions = new ArrayList<>();
    ArrayList<Map> readingTransactions = new ArrayList<>();
    ArrayList<Map> writingTransactions = new ArrayList<>();

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

//    public void currentQuery(Map<String,Object> validatedQuery, String transactionName){
//        HashMap<String, Object> currentTransaction = (HashMap<String, Object>) transactions.stream().filter(element -> element.get("transactionName").equals(transactionName));
//        ArrayList<Map<String, Object>> readingTheResource = (ArrayList<Map<String, Object>>) readingTransactions.stream().filter(element -> element.get("tableName").equals(tableName));
//        ArrayList<Map<String, Object>> writingTheResource = (ArrayList<Map<String, Object>>) writingTransactions.stream().filter(element -> element.get("tableName").equals(tableName));
//
//        if (isConflicting((String) validatedQuery.get("queryType"), (String) validatedQuery.get("tableName"), transactionName)) {
//            if (writingTheResource.size() > 0 && (Long) currentTransaction.get("timestamp") < (Long) writingTheResource.get(0).get("timestamp")) {
//
//            }
//        } else {
//            // FIleParsing.transactionselect();
//        }
//    }
}
