package numbers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Rule {
    EVEN(num -> num % 2 == 0),
    ODD(num -> num % 2 != 0),
    BUZZ(num -> num % 7 == 0 || num % 10 == 7),
    DUCK(num -> String.valueOf(num).indexOf('0') != -1),
    PALINDROMIC(num -> {
        String str = String.valueOf(num);
        String reversed = new StringBuilder(str).reverse().toString();
        return reversed.equals(str);
    }),
    GAPFUL(num -> {
        String str = String.valueOf(num);
        if (str.length() < 3) {
            return false;
        }
        long divisor = (str.charAt(0) - '0') * 10 + num % 10;
        return num % divisor == 0;
    }),
    SPY(num -> {
        List<Integer> digits = String.valueOf(num).chars()
                .mapToObj(Character::toString)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int sum = digits.stream().reduce(0, Integer::sum);
        int product = digits.stream().reduce((d1, d2) -> d1 * d2).orElse(0);
        return sum == product;
    }),
    SUNNY(num -> {
        double result = Math.sqrt(num + 1);
        return result == (int) result;
    }),
    SQUARE(num -> {
        double result = Math.sqrt(num);
        return result == (int) result;
    }),
    JUMPING(num -> {
        long copy = num;
        while (copy != 0) {
            long d1 = copy % 10;
            copy /= 10;
            if(copy != 0){
                long d2 = copy % 10;
                if(Math.abs(d1 - d2) != 1){
                    return false;
                }
            }
        }
        return true;
    }),
    HAPPY(Rule::isHappy),
    SAD(num -> !isHappy(num));

    private final Function<Long, Boolean> function;

    Rule(Function<Long, Boolean> function) {
        this.function = function;
    }

    public boolean apply(long num) {
        return function.apply(num);
    }

    public String format(long num) {
        return String.format("%15s: %s", this, this.apply(num));
    }

    private static boolean isHappy(long num) {
        Set<Long> set = new HashSet<>();
        do {
            if (set.contains(num)) {
                return false;
            }
            set.add(num);
            num = squareSum(num);
        } while(num != 1);
        return true;
    }

    private static int squareSum(long num)
    {
        int sum = 0;
        while (num!= 0)
        {
            sum += (num % 10) * (num % 10);
            num /= 10;
        }
        return sum;
    }

    public boolean isMutuallyExclusive(Rule rule, boolean negative) {
        Rule[][] mutuallyExclusive;
        if (negative) {
            mutuallyExclusive = new Rule[][]{
                    {EVEN, ODD},
                    {HAPPY, SAD}
            };
        } else {
            mutuallyExclusive = new Rule[][]{
                    {EVEN, ODD},
                    {DUCK, SPY},
                    {SUNNY, SQUARE},
                    {HAPPY, SAD}
            };
        }
        for (Rule[] set : mutuallyExclusive) {
            if (this.equals(set[0]) && rule.equals(set[1])) {
                return true;
            }
            if (this.equals(set[1]) && rule.equals(set[0])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    }
