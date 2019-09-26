import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Transactions {
  
    /**
     * position of the field 'name' within the transaction CSV
     */
    private static final int INDEX_NAME = 0;
    /**
     * position of the field 'last name' within the transaction CSV
     */
    private static final int INDEX_LAST_NAME = 1;
    /**
     * position of the field 'email' within the transaction CSV
     */
    private static final int INDEX_EMAIL = 2;
    /**
     * position of the field 'value' within the transaction CSV
     */
    private static final int INDEX_VALUE = 3;
    /**
     * position of the field 'transaction id' within the transaction CSV
     */
    private static final int INDEX_TRANS_ID = 4;

    /**
     * Method to identify which transactions in a list of transactions should be
     * rejected for exceeding the credit limit. Each consumer, identified by its
     * consumer ID (composed of name, last name, and email), has its own credit
     * line and can make as many transactions as they wish as long as the
     * resulting sum of such transactions don't exceed the credit limit.
     *
     * @param transactions
     *            - list of each transaction made identified by a consumer ID
     *            and transaction ID format: ["name","last name", "email",
     *            transaction value, transaction ID]
     * @param creditLimit
     *            - maximum amount each consumer can spend
     * @return list of transaction IDs containing the rejected transactions,
     *         empty list if no transaction was rejected
     */
    public static List<String> findRejectedTransactions(final List<String> transactions, final int creditLimit) {
        final List<String> rejected = new ArrayList<>();
        final Map<String, Integer> approved = new HashMap<>();

        for( final String transaction : transactions ) {
            final String[] consumerData = transaction.split( "," );
            final String consumerID = String.format( "%s,%s,%s", consumerData[ INDEX_NAME ],
                    consumerData[ INDEX_LAST_NAME ], consumerData[ INDEX_EMAIL ] );
            final int value = Integer.parseInt( consumerData[ INDEX_VALUE ] );
            final String transactionID = consumerData[ INDEX_TRANS_ID ];

            if( value > creditLimit ) {
                //quick fail approach
                //transaction, by itself, is over the credit limit
                //don't let the consumer wait too long for a rejected transaction
                rejected.add( transactionID );
            }
            else {
                final int existingBalance = approved.getOrDefault( consumerID, 0 );
                final int futureBalance = existingBalance + value;
                if( futureBalance > creditLimit ) {
                    //this transaction, on top of any previous transactions
                    //this consumer has made, exceeded their credit limit
                    rejected.add( transactionID );
                }
                else {
                    //transaction was within the consumer's credit limit
                    approved.put( consumerID, futureBalance );
                }
            }
        }
        return rejected;
    }
}