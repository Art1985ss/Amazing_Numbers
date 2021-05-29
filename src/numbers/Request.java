package numbers;

import numbers.service.AmazingNumbersException;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private long number;
    private int numbersCount;
    private List<Rule> positiveRules = new ArrayList<>();
    private List<Rule> negativeRules = new ArrayList<>();
    private int paramCount;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getNumbersCount() {
        return numbersCount;
    }

    public void setNumbersCount(int numbersCount) {
        this.numbersCount = numbersCount;
    }

    public List<Rule> getPositiveRules() {
        return positiveRules;
    }

    public void setPositiveRules(List<Rule> positiveRules) throws AmazingNumbersException {
        this.positiveRules = positiveRules;
        validateRules();
    }

    public void addRule(Rule rule) throws AmazingNumbersException {
        positiveRules.add(rule);
        validateRules();
    }

    public List<Rule> getNegativeRules() {
        return negativeRules;
    }

    public void setNegativeRules(List<Rule> negativeRules) {
        this.negativeRules = negativeRules;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

    private void validateRules() throws AmazingNumbersException {
        if (positiveRules.size() == 1) {
            return;
        }
        boolean even = positiveRules.contains(Rule.EVEN);
        boolean odd = positiveRules.contains(Rule.ODD);
        boolean duck = positiveRules.contains(Rule.DUCK);
        boolean spy = positiveRules.contains(Rule.SPY);
        boolean sunny = positiveRules.contains(Rule.SUNNY);
        boolean square = positiveRules.contains(Rule.SQUARE);
        boolean happy = positiveRules.contains(Rule.HAPPY);
        boolean sad = positiveRules.contains(Rule.SAD);
        if (even && odd) {
            throw new AmazingNumbersException("The request contains mutually exclusive properties: [ODD, EVEN]\n" +
                    "There are no numbers with these properties.");
        }
        if (duck && spy) {
            throw new AmazingNumbersException("The request contains mutually exclusive properties: [DUCK, SPY]\n" +
                    "There are no numbers with these properties.");
        }
        if (sunny && square) {
            throw new AmazingNumbersException("The request contains mutually exclusive properties: [SUNNY, SQUARE]\n" +
                    "There are no numbers with these properties.");
        }
        if (happy && sad) {
            throw new AmazingNumbersException("The request contains mutually exclusive properties: [HAPPY, SAD]\n" +
                    "There are no numbers with these properties.");
        }
    }
}
