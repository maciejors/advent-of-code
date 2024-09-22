package com.maciejors.aoc21.day18;

import com.maciejors.aoc21.shared.CommonFunctions;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day18 {
    public static void main(String[] args) {
        List<String> input = CommonFunctions.readInput("18", false);
        System.out.println("Task 1:");
        task1(input);
        System.out.println();
        System.out.println("Task 2:");
        task2(input);
    }

    private static List<Pair> parsePairs(List<String> input) {
        List<Pair> result = new ArrayList<>();
        for (String line : input) {
            result.add((Pair) PairElement.parse(line));
        }
        return result;
    }

    private static void task1(List<String> input) {
        List<Pair> pairs = parsePairs(input);
        Pair sum = pairs.get(0);
        for (Pair pair : pairs.subList(1, pairs.size())) {
            sum = sum.add(pair);
        }
        System.out.println(sum.magnitude());
    }

    private static void task2(List<String> input) {
        List<Pair> pairs = parsePairs(input);
        int maxMagnitude = 0;
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = 0; j < pairs.size(); j++) {
                if (j == i) {
                    continue;
                }
                pairs = parsePairs(input);
                PairElement leftSummand = pairs.get(i);
                PairElement rightSummand = pairs.get(j);
                PairElement sum = leftSummand.add(rightSummand);
                int sumMagnitude = sum.magnitude();
                maxMagnitude = Math.max(maxMagnitude, sumMagnitude);
            }
        }
        System.out.println(maxMagnitude);
    }
}

abstract class PairElement {
    public static PairElement parse(String s) {
        Pattern pattern = Pattern.compile("\\[(.+)]");
        Matcher matcher = pattern.matcher(s);
        // a regular number
        if (!matcher.find()) {
            return new RegularNumber(Integer.parseInt(s));
        }
        // a pair
        String pairContent = matcher.group(1);
        // store left & right element in a list
        List<String> pairElementsStr = getPairElementsStr(pairContent);
        PairElement leftElement = parse(pairElementsStr.get(0));
        PairElement rightElement = parse(pairElementsStr.get(1));
        return new Pair(leftElement, rightElement);
    }

    private static List<String> getPairElementsStr(String pairContent) {
        List<String> pairElementsStr = new ArrayList<>();
        // depth in terms of pairs
        int depth = 0;
        // build elements char by char
        StringBuilder currElement = new StringBuilder();
        for (char c : pairContent.toCharArray()) {
            if (depth == 0 && c == ',') {
                pairElementsStr.add(currElement.toString());
                currElement = new StringBuilder();
            } else {
                if (c == '[') {
                    depth++;
                } else if (c == ']') {
                    depth--;
                }
                currElement.append(c);
            }
        }
        pairElementsStr.add(currElement.toString());
        return pairElementsStr;
    }

    public Pair add(PairElement other) {
        Pair sum = new Pair(this, other);
        sum.reduce();
        return sum;
    }

    public abstract int magnitude();
}

class RegularNumber extends PairElement {
    private int value;

    public RegularNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int magnitude() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

class Pair extends PairElement {
    private PairElement left;
    private PairElement right;

    public Pair(PairElement left, PairElement right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Handle explode maximum once.
     * @return true if there was an explosion, otherwise false
     */
    private boolean handleSingleExplode() {
        RegularNumber firstRegularToLeft = null;
        RegularNumber firstRegularToRight = null;
        Pair pairToExplode = null;
        Deque<PairElement> pathStack = new ArrayDeque<>();
        // visitedPairs - pairs of which both elements have been checked
        Set<PairElement> visitedPairs = new HashSet<>();
        // traverse the pair
        pathStack.push(this);
        while (!pathStack.isEmpty()) {
            PairElement currElement = pathStack.peek();
            // 1. HANDLE REGULAR NUMBERS
            if (currElement instanceof RegularNumber) {
                visitedPairs.add(currElement);
                if (pairToExplode == null) {
                    // pair to explode not found yet - track the closest one to the left
                    firstRegularToLeft = (RegularNumber) currElement;
                } else {
                    // pair to explode already found, so the thing we are looking at is the
                    // closest one to the right. Since we have now found it, we can stop
                    // the traversal
                    firstRegularToRight = (RegularNumber) currElement;
                    break;
                }
                pathStack.pop();
                continue;
            }
            Pair currPair = (Pair) currElement;
            // 2. HANDLE EXPLODE
            // check if this pair should explode
            if (pairToExplode == null && pathStack.size() == 5) {
                // the fifth one is nested inside four - explosion criterion met
                pairToExplode = currPair;
                pathStack.pop();
                PairElement replacement = new RegularNumber(0);
                // update parent once we find out whether this pair is left or right element
                Pair parent = (Pair) pathStack.peek();
                assert parent != null;
                if (pairToExplode == parent.left) {
                    parent.left = replacement;
                } else {
                    parent.right = replacement;
                }
                visitedPairs.add(replacement);
                continue;
            }

            // 3. ROUTING IN OTHER SCENARIOS - BACKTRACK OR MOVE TO A CHILD
            if (visitedPairs.contains(currPair.left)) {
                if (visitedPairs.contains(currPair.right)) {
                    // both visited so we backtrack
                    visitedPairs.add(currElement);
                    pathStack.pop();
                } else {
                    // left visited but right not
                    pathStack.push(currPair.right);
                }
            } else {
                // neither left nor right visited
                pathStack.push(currPair.left);
            }
        }
        // loop finished
        // handle no explosion
        if (pairToExplode == null) {
            return false;
        }
        // update regular numbers
        if (firstRegularToLeft != null) {
            RegularNumber explodedLeftElement = (RegularNumber) pairToExplode.left;
            int originalValue = firstRegularToLeft.getValue();
            firstRegularToLeft.setValue(originalValue + explodedLeftElement.getValue());
        }
        if (firstRegularToRight != null) {
            RegularNumber explodedRightElement = (RegularNumber) pairToExplode.right;
            int originalValue = firstRegularToRight.getValue();
            firstRegularToRight.setValue(originalValue + explodedRightElement.getValue());
        }
        return true;
    }

    /**
     * Handle split maximum once.
     * @return true if there was a split, otherwise false
     */
    private boolean handleSingleSplit() {
        Deque<PairElement> pathStack = new ArrayDeque<>();
        // visitedPairs - pairs of which both elements have been checked
        Set<PairElement> visitedPairs = new HashSet<>();
        // traverse the pair
        pathStack.push(this);
        while (!pathStack.isEmpty()) {
            PairElement currElement = pathStack.peek();
            // 1. HANDLE REGULAR NUMBERS
            if (currElement instanceof RegularNumber currRegularNumber) {
                visitedPairs.add(currElement);
                pathStack.pop();
                double currValue = currRegularNumber.getValue();
                // handle SPLIT here
                if (currValue >= 10) {
                    Pair parent = (Pair) pathStack.peek();
                    assert parent != null;
                    Pair replacement = new Pair(
                            new RegularNumber((int) Math.floor(currValue / 2)),
                            new RegularNumber((int) Math.ceil(currValue / 2))
                    );
                    if (currElement == parent.left) {
                        parent.left = replacement;
                    } else {
                        parent.right = replacement;
                    }
                    // no need to keep going
                    return true;
                }
                continue;
            }
            Pair currPair = (Pair) currElement;

            // 2. ROUTING IN OTHER SCENARIOS - BACKTRACK OR MOVE TO A CHILD
            if (visitedPairs.contains(currPair.left)) {
                if (visitedPairs.contains(currPair.right)) {
                    // both visited so we backtrack
                    visitedPairs.add(currElement);
                    pathStack.pop();
                } else {
                    // left visited but right not
                    pathStack.push(currPair.right);
                }
            } else {
                // neither left nor right visited
                pathStack.push(currPair.left);
            }
        }
        // loop finished - nothing found
        return false;
    }

    /**
     * Perform all reductions if needed
     */
    public void reduce() {
        boolean actionTaken;
        do {
            actionTaken = handleSingleExplode();
            if (!actionTaken) {
                actionTaken = handleSingleSplit();
            }
        } while (actionTaken);
    }

    @Override
    public int magnitude() {
        return 3 * left.magnitude() + 2 * right.magnitude();
    }

    @Override
    public String toString() {
        return "[" + left + "," + right + "]";
    }
}
