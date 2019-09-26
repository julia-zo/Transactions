import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TransactionsTest {

    @Test
    public void shouldReturnEmptyListIfThereIsNoTransactions() {
        assertThat(Transactions.findRejectedTransactions(new ArrayList<>(), 0).size(), is(0));
    }

    @Test
    public void shouldReturnEmptyListIfThereIsATransactionWithinCreditLimit() {
        List<String> transactions = Arrays.asList("John,Doe,john@doe.com,200,TR0001");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 200);

        assertThat(rejectedTransactions.size(), is(0));
    }

    @Test
    public void shouldReturnTransationThatIsOverCreditLimit() {
        List<String> transactions = Arrays.asList("John,Doe,john@doe.com,201,TR0001");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 200);

        assertThat(rejectedTransactions, is(Arrays.asList("TR0001")));
    }
  
    @Test
    public void shouldReturnTransationThatIsOverCreditLimitMultipleConsumers() {
        List<String> transactions = new ArrayList<>(4);
        transactions.add("John,Doe,john@doe.com,55,TR0001");
        transactions.add("John,Carl,john@carl.com,100,TR0002");
        transactions.add("Mary,Jane,mary@jane.com,60,TR0003");
        transactions.add("Stacy,Lee,stacy@lee.com,51,TR0004");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 50);

        assertThat(rejectedTransactions, is(Arrays.asList("TR0001","TR0002","TR0003","TR0004")));
    }
  
    @Test
    public void shouldApproveAllTransationsMultipleConsumers() {
        List<String> transactions = new ArrayList<>(8);
        transactions.add("John,Doe,john@doe.com,55,TR0001");
        transactions.add("John,Carl,john@carl.com,100,TR0002");
        transactions.add("Mary,Jane,mary@jane.com,60,TR0003");
        transactions.add("Stacy,Lee,stacy@lee.com,51,TR0004");
        transactions.add("John,Doe,john@doe.com,70,TR0005");
        transactions.add("John,Carl,john@carl.com,100,TR0006");
        transactions.add("Mary,Jane,mary@jane.com,12,TR0007");
        transactions.add("Stacy,Lee,stacy@lee.com,130,TR0008");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 200);

        assertThat(rejectedTransactions.size(), is(0));
    }
  
    @Test
    public void shouldReturnTransationsOverCreditLimitMultipleConsumersTwoTransactions() {
        List<String> transactions = new ArrayList<>(8);
        transactions.add("John,Doe,john@doe.com,55,TR0001");
        transactions.add("John,Carl,john@carl.com,100,TR0002");
        transactions.add("Mary,Jane,mary@jane.com,60,TR0003");
        transactions.add("Stacy,Lee,stacy@lee.com,51,TR0004");
        transactions.add("John,Doe,john@doe.com,100,TR0005");
        transactions.add("John,Carl,john@carl.com,51,TR0006");
        transactions.add("Mary,Jane,mary@jane.com,200,TR0007");
        transactions.add("Stacy,Lee,stacy@lee.com,130,TR0008");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 150);

        assertThat(rejectedTransactions, is(Arrays.asList("TR0005","TR0006","TR0007","TR0008")));
    }
  
    @Test
    public void shouldReturnTransationOverCreditLimitSameConsumerMultipleTransactions() {
        List<String> transactions = new ArrayList<>(8);
        transactions.add("John,Doe,john@doe.com,55,TR0001");
        transactions.add("John,Doe,john@doe.com,400,TR0002");
        transactions.add("John,Doe,john@doe.com,60,TR0003");
        transactions.add("John,Doe,john@doe.com,10,TR0004");
        transactions.add("John,Doe,john@doe.com,46,TR0005");
        transactions.add("John,Doe,john@doe.com,1,TR0006");
        transactions.add("John,Doe,john@doe.com,45,TR0007");
        transactions.add("John,Doe,john@doe.com,34,TR0008");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 651);

        assertThat(rejectedTransactions.size(), is(0));
    }
  
    @Test
    public void shouldApproveAllTransationsSameConsumerMultipleTransactions() {
        List<String> transactions = new ArrayList<>(8);
        transactions.add("John,Doe,john@doe.com,55,TR0001");
        transactions.add("John,Doe,john@doe.com,400,TR0002");
        transactions.add("John,Doe,john@doe.com,60,TR0003");
        transactions.add("John,Doe,john@doe.com,10,TR0004");
        transactions.add("John,Doe,john@doe.com,46,TR0005");
        transactions.add("John,Doe,john@doe.com,1,TR0006");
        transactions.add("John,Doe,john@doe.com,45,TR0007");
        transactions.add("John,Doe,john@doe.com,34,TR0008");

        List<String> rejectedTransactions = Transactions.findRejectedTransactions(transactions, 500);

        assertThat(rejectedTransactions, is(Arrays.asList("TR0003","TR0005","TR0007")));
    }

}