package com.at.shopping.cart.util;


import java.util.function.Supplier;

/**
 * GENERIC RETRY UTILITY
 *
 * DESIGN:
 * - Separates retry logic from business logic
 * - Reusable across data sources, APIs, DB calls
 * - Fail-fast after max attempts
 * - Simple + predictable control flow
 */
public final class RetryExecutor {

    private RetryExecutor() {}

    /**
     * Executes a task with retry support.
     *
     * @param maxRetries number of attempts
     * @param backoffMs  delay between retries
     * @param task       logic to execute (doWork)
     * @return result of successful execution
     */
    public static <T> T execute(
            int maxRetries,
            long backoffMs,
            Supplier<T> task
    ) {

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                // 🔥 actual business call
                return task.get();

            } catch (Exception e) {

                // last attempt → fail fast
                if (attempt == maxRetries - 1) {
                    throw new RuntimeException("Operation failed after retries", e);
                }

                // simple backoff strategy
                try {
                    Thread.sleep(backoffMs * (attempt + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }

        // unreachable but required by compiler
        throw new IllegalStateException("Unexpected retry state");
    }
}
