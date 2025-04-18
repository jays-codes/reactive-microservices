package jayslabs.core.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
public class HigherOrderFunctions {
    public static void main(String[] args) {

        //testCreateRetrySupplier();
        //testCreateCounter();
        //testMemoizedFibonacci();
        testHtmlFormatter();
    }

    public static void testHtmlFormatter(){
        UnaryOperator<String> boldFormatter = htmlFormatter("b");
        System.out.println(boldFormatter.apply("Hello, World!"));
    }

    public static UnaryOperator<String> htmlFormatter(String tag){
        return s -> "<" + tag + ">" + s + "</" + tag + ">";
    }

    public static void testMemoizedFibonacci(){
        Function<Integer, Integer> fibonacci = new HigherOrderFunctions().memoizedFibonacci();
        System.out.println(fibonacci.apply(10));
    }

    public static void testCreateCounter(){
        Supplier<Integer> counter = createrCounter(10);
        System.out.println(counter.get());
        System.out.println(counter.get());
        System.out.println(counter.get());
    }

    public static void testCreateRetrySupplier(){
        Supplier<String> randomNumberSupplier = () -> {
            if (Math.random() < 0.5){
                throw new RuntimeException("Random number is less than 0.5");
            }
            return "Success!";
        };

        Supplier<String> retrySupplier = createRetrySupplier(randomNumberSupplier, 1);
        System.out.println(retrySupplier.get());

    }

    public static Supplier<Integer> createrCounter(int startVal){
        //one element array. counter by itself wont work as it needs to be final when used inside the lambda
        int[] counter = {startVal};
        return () -> counter[0]++;
    }

    public static <T> Supplier<T> createRetrySupplier(Supplier<T> operation, int maxRetries){
        return () -> {
            Exception lastException = null;
            for (int i = 0; i < maxRetries; i++){
                try {
                    return operation.get();
                } catch (Exception e){
                    lastException = e;
                    System.out.println("Attempt " + (i + 1) + " failed: " + e.getMessage());
                }
            }
            throw new RuntimeException("Failed after " + maxRetries + " attempts", lastException);
        };
    }

    public Function<Integer, Integer> memoizedFibonacci() {
        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(0, 0);
        cache.put(1, 1);
        
        Function<Integer, Integer> fibonacci = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer n) {
                if (cache.containsKey(n)) {
                    return cache.get(n);
                }
                int result = apply(n-1) + apply(n-2);
                cache.put(n, result);
                return result;
            }
        };
        
        return fibonacci;
    }
    
}
