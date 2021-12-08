import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionManager {
    private TransactionManager(){}

    public static TransactionManager transactionManager = null;
    public TransactionManager getInstance(){
        if (transactionManager == null) {
            transactionManager = new TransactionManager();
        }
        return transactionManager;
    }

    ArrayList<Map> transactionList = new ArrayList<>();

    public void transactionStart(String name, long timeStamp){
        HashMap<String, Long> hashMap = new HashMap<String, Long>();
        hashMap.put(name,timeStamp);
        transactionList.add(hashMap);
    }
}
