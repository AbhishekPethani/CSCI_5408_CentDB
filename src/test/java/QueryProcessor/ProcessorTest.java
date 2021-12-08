package QueryProcessor;

import org.junit.Test;

public class ProcessorTest {
    @Test
    public void submitCreateDBQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("CREATE DATABASE newDB;"));
    }

    @Test
    public void submitUseDBQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
    }

    @Test
    public void submitDropDBQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("DROP DATABASE newDB;"));
    }

    @Test
    public void submitCreateTableQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
        System.out.println(processor.submitQuery("CREATE TABLE people3 (PersonID INT PRIMARY_KEY, LastName TEXT, " +
                "FirstName TEXT NOT_NULL);"));
    }

    @Test
    public void submitDeleteTableQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
        System.out.println(processor.submitQuery("DROP TABLE people1;"));
    }

    @Test
    public void submitSelectTableQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
        System.out.println(processor.submitQuery("SELECT FirstName, LastName, PersonID FROM people2;"));
    }

    @Test
    public void submitInsertIntoTableQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
        System.out.println(processor.submitQuery("INSERT INTO people2 (PersonID, LastName, FirstName) VALUES " +
                "(101, last1, first1), (102, last2, first2), (103, last3, first3);"));
    }

    @Test
    public void submitDeleteFromTableQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
        System.out.println(processor.submitQuery("DELETE FROM people2 WHERE PersonID = 103;"));
    }

    @Test
    public void submitUpdateTableQueryTest() {
        Processor processor = new Processor();
        System.out.println(processor.submitQuery("USE newDB;"));
        System.out.println(processor.submitQuery("UPDATE people2 SET FirstName = 'first4', LastName= " +
                "'last4' WHERE PersonID = 103;"));
    }
}
