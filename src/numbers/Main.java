package numbers;

import numbers.service.AmazingNumbersException;
import numbers.service.UserIO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {


    public static void main(String[] args) {
        Request request;
        UserIO.greetUser();
        UserIO.writeInstructions();
        do {
            try {
                request = UserIO.readNatural();
                if (request.getNumber() == 0) {
                    System.out.println("Goodbye!");
                    return;
                }
                switch (request.getParamCount()) {
                    case 1:
                        process(request.getNumber(), true);
                        break;
                    case 2:
                        LongStream.range(request.getNumber(), request.getNumber() + request.getNumbersCount())
                                .forEach(num -> process(num, false));
                        break;
                    case 3:
                        final List<Rule> positiveRules = request.getPositiveRules();
                        final List<Rule> negativeRules = request.getNegativeRules();
                        LongStream.iterate(request.getNumber(), n -> n + 1)
                                .filter(n -> positiveRules.stream().allMatch(rule -> rule.apply(n)))
                                .filter(n -> negativeRules.stream().noneMatch(rule -> rule.apply(n)))
                                .limit(request.getNumbersCount())
                                .forEach(n -> process(n, false));
                        break;
                    case 4:
                }
            } catch (AmazingNumbersException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private static void process(final long number, boolean fullOutput) {
        if (fullOutput) {
            UserIO.writeProperties(number,
                    Arrays.stream(Rule.values())
                            .map(rule -> rule.format(number))
                            .collect(Collectors.toList())
            );
        } else {
            String text = number + " is ";
            text += Arrays.stream(Rule.values())
                    .filter(rule -> rule.apply(number))
                    .map(Rule::toString)
                    .collect(Collectors.joining(", "));
            UserIO.write(text);
        }
    }
}
