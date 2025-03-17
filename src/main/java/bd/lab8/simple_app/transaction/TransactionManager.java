package bd.lab8.simple_app.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionManager {

    public static void printIsolationLevel() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
            System.out.println("Current Transaction Isolation Level: " + getIsolationLevelName(isolationLevel));
        } else {
            System.out.println("No active transaction.");
        }
    }

    private static String getIsolationLevelName(Integer isolationLevel) {
        if (isolationLevel == null) {
            return "DEFAULT (database-specific)";
        }
        return switch (isolationLevel) {
            case 1 -> "READ_UNCOMMITTED";
            case 2 -> "READ_COMMITTED";
            case 4 -> "REPEATABLE_READ";
            case 8 -> "SERIALIZABLE";
            default -> "UNKNOWN";
        };
    }
}
