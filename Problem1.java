import java.util.*;

public class CoinSimulation {

    static Random rand = new Random();

    // 1. Toss the coin n times with probability p for H
    public static List<Character> Toss(double p, int n) {
        List<Character> results = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            results.add(rand.nextDouble() < p ? 'H' : 'T');
        }
        return results;
    }

    // 2. Count frequency of H in Toss(p, N)
    public static double relativeFrequencyOfH(double p, int N) {
        List<Character> tosses = Toss(p, N);
        long countH = tosses.stream().filter(c -> c == 'H').count();
        return (double) countH / N;
    }

    // 3. Count runs in Toss(p, n)
    public static int Runs(double p, int n) {
        List<Character> tosses = Toss(p, n);
        if (tosses.isEmpty()) return 0;
        int runs = 1;
        for (int i = 1; i < tosses.size(); i++) {
            if (!tosses.get(i).equals(tosses.get(i - 1))) {
                runs++;
            }
        }
        return runs;
    }

    public static void computeRunStats(double p, int n, int N) {
        List<Integer> runList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            runList.add(Runs(p, n));
        }
        double mean = runList.stream().mapToDouble(x -> x).average().orElse(0.0);
        double variance = runList.stream().mapToDouble(x -> Math.pow(x - mean, 2)).average().orElse(0.0);

        System.out.println("Mean of Runs ≈ " + mean);
        System.out.println("Variance of Runs ≈ " + variance);

        // Theoretical values
        double q = 1 - p;
        double theoreticalMean = 1 + 2 * (n - 1) * p * q;
        double theoreticalVar = 2 * p * q * (2 * n - 3 - 2 * p * q * (3 * n - 5));
        System.out.println("Theoretical Mean ≈ " + theoreticalMean);
        System.out.println("Theoretical Variance ≈ " + theoreticalVar);
    }

    // 4. Toss until number of H > number of T, return X = number of tosses
    public static int tossUntilHGreaterThanT(double p) {
        int countH = 0, countT = 0, tosses = 0;
        while (countH <= countT) {
            tosses++;
            if (rand.nextDouble() < p)
                countH++;
            else
                countT++;
        }
        return tosses;
    }

    public static void computeExpectedX(double p, int N) {
        long total = 0;
        for (int i = 0; i < N; i++) {
            total += tossUntilHGreaterThanT(p);
        }
        double expectedX = (double) total / N;
        System.out.println("Estimated E(X) ≈ " + expectedX);
    }

    // 5. Count number of strings in S(a, b)
    public static long countS(int a, int b) {
        return binomial(a + b, a);  // C(a+b, a)
    }

    public static long binomial(int n, int k) {
        if (k > n - k) k = n - k;
        long res = 1;
        for (int i = 0; i < k; ++i) {
            res *= (n - i);
            res /= (i + 1);
        }
        return res;
    }

    // 5b: Generate random string in S(a, b)
    public static String randomABString(int a, int b) {
        StringBuilder sb = new StringBuilder();
        int A = a, B = b;
        while (A > 0 || B > 0) {
            if (A == 0) {
                sb.append('B'); B--;
            } else if (B == 0) {
                sb.append('A'); A--;
            } else {
                if (rand.nextDouble() < (double) A / (A + B)) {
                    sb.append('A'); A--;
                } else {
                    sb.append('B'); B--;
                }
            }
        }
        return sb.toString();
    }

    // Count runs of A
    public static int RunA(String s) {
        int runs = 0;
        boolean inRun = false;
        for (char c : s.toCharArray()) {
            if (c == 'A') {
                if (!inRun) {
                    runs++;
                    inRun = true;
                }
            } else {
                inRun = false;
            }
        }
        return runs;
    }

    // Estimate mean of RunA over N samples
    public static void estimateRunA(int a, int b, int N) {
        int total = 0;
        for (int i = 0; i < N; i++) {
            String s = randomABString(a, b);
            total += RunA(s);
        }
        double empiricalMean = (double) total / N;
        double theoreticalMean = (double) a * (b + 1) / (a + b);
        System.out.println("Empirical Mean RunA ≈ " + empiricalMean);
        System.out.println("Theoretical Mean RunA = " + theoreticalMean);
    }

    public static void main(String[] args) {
        double p = 0.6;
        int n = 20;
        int N = 10000;

        System.out.println("=== Relative Frequency of H ===");
        System.out.println("P(H) = " + p + ", Empirical Frequency of H = " + relativeFrequencyOfH(p, N));

        System.out.println("\n=== Run Statistics ===");
        computeRunStats(p, n, 1000);

        System.out.println("\n=== Expected Tosses Until H > T ===");
        computeExpectedX(p, N);

        System.out.println("\n=== RunA in S(a,b) ===");
        int a = 5, b = 7;
        System.out.println("|S(" + a + "," + b + ")| = " + countS(a, b));
        estimateRunA(a, b, 10000);
    }
}
