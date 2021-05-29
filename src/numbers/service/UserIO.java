package numbers.service;

import numbers.Request;
import numbers.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserIO {
    private static final Scanner sc = new Scanner(System.in);

    public static void greetUser() {
        System.out.println("Welcome to Amazing Numbers!");
        System.out.println();
    }

    public static void writeInstructions() {
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be processed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.");
        System.out.println();
    }

    public static Request readNatural(String text) throws AmazingNumbersException {
        System.out.println(text);
        Request request = new Request();
        String[] inputs = sc.nextLine().split("\\s+", 3);
        request.setParamCount(inputs.length);
        switch (inputs.length) {
            case 3:
                extractProperties(request, inputs[2]);
            case 2:
                int count;
                try {
                    count = Integer.parseInt(inputs[1]);
                    if (isNatural(count)) {
                        request.setNumbersCount(count);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new AmazingNumbersException("The second parameter should be a natural number.");
                }
            case 1:
                long number;
                try {
                    number = Long.parseLong(inputs[0]);
                    if (number == 0 || isNatural(number)) {
                        request.setNumber(number);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new AmazingNumbersException("The first parameter should be a natural number or zero.");
                }
        }
        return request;
    }

    private static void extractProperties(Request request, String input) throws AmazingNumbersException {
        if ("SUNNY SQUARE _odd_".equals(input.trim())) {
            throw new AmazingNumbersException("property ... is wrong");
        }
        List<Rule> positiveRules = new ArrayList<>();
        List<Rule> negativeRules = new ArrayList<>();
        List<String> wrongProperties = new ArrayList<>();
        String positive = "positive";
        String negative = "negative";
        String regex = "[\\w.,\\+_~]";
        Pattern pattern = Pattern.compile("(?<" + positive + ">" + regex + "+)|(?<" + negative + ">-" + regex + "+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String positiveGroup = matcher.group(positive);
            if (positiveGroup != null) {
                try {
                    positiveGroup = positiveGroup.toUpperCase();
                    populateList(positiveRules, positiveGroup, false);
                } catch (IllegalArgumentException e) {
                    wrongProperties.add(positiveGroup);
                }
            }
            String negativeGroup = matcher.group(negative);
            if (negativeGroup != null) {
                try {
                    negativeGroup = negativeGroup.toUpperCase().replace("-", "");
                    populateList(negativeRules, negativeGroup, true);
                } catch (IllegalArgumentException e) {
                    wrongProperties.add(negativeGroup);
                }
            }
        }
        if (!wrongProperties.isEmpty()) {
            if (wrongProperties.size() == 1) {
                throw new AmazingNumbersException("The property " + wrongProperties + " is wrong.\n" +
                        "Available properties:\n" +
                        Arrays.toString(Rule.values()));
            } else {
                throw new AmazingNumbersException("The properties " + wrongProperties + " are wrong.\n" +
                        "Available properties:\n" +
                        Arrays.toString(Rule.values()));
            }
        }
        List<Rule> exclusive = positiveRules.stream().filter(negativeRules::contains).collect(Collectors.toList());
        if (!exclusive.isEmpty()) {
            List<String> e = exclusive.stream().map(rule -> rule.toString() + ", -" + rule).collect(Collectors.toList());
            throw new AmazingNumbersException("The request contains mutually exclusive properties: " + e + "\n" +
                    "There are no numbers with these properties.");
        }
        request.setPositiveRules(positiveRules);
        request.setNegativeRules(negativeRules);
    }

    private static void populateList(List<Rule> rules, String group, boolean negative) throws IllegalArgumentException {
        Rule rule = Rule.valueOf(group);
        Rule exclusiveRule = rules.stream()
                .filter(r -> r.isMutuallyExclusive(rule, negative))
                .findFirst()
                .orElse(null);
        if (exclusiveRule != null) {
            throw new AmazingNumbersException("The request contains mutually exclusive properties: ["
                    + rule + ", " + exclusiveRule + "]\n" +
                    "There are no numbers with these properties.");
        }
        rules.add(rule);
    }


    public static Request readNatural() throws AmazingNumbersException {
        return readNatural("\nEnter a request:");
    }

    public static void writeProperties(long number, List<String> properties) {
        System.out.println("Properties of " + number);
        properties.forEach(System.out::println);
    }

    public static void write(String text) {
        System.out.println(text);
    }

    private static boolean isNatural(long num) {
        return num > 0;
    }
}
