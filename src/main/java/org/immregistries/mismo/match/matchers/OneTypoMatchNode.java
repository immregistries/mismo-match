package org.immregistries.mismo.match.matchers;

import org.immregistries.mismo.match.model.Patient;

/**
 * This match node considers two field to match if they are the sae except for
 * one character. That means
 * that one character is removed from on of the fields, or that the character is
 * the same between the two fields.
 * But the fields have to be at least three characters long to be considered for
 * this match.
 * 
 * @author Nathan Bunker
 *
 */
public class OneTypoMatchNode extends MatchNode {

    protected String comparisonMatch = "~=";
    protected String comparisonNotMatch = "!=";

    public OneTypoMatchNode() {
        super();
    }

    public OneTypoMatchNode(String matchName, double minScore, double maxScore, String fieldName) {
        super(matchName, minScore, maxScore);
        this.fieldName = fieldName;
    }

    public OneTypoMatchNode(String matchName, double minScore, double maxScore, String fieldName,
            String fieldNameOther) {
        super(matchName, minScore, maxScore);
        this.fieldName = fieldName;
        this.fieldNameOther = fieldNameOther;
    }

    @Override
    public String getDescription(Patient patientA, Patient patientB) {
        String a = patientA.getValue(fieldName);
        String b = patientB.getValue(fieldName);
        if (hasFieldNameOther()) {
            String aOther = patientA.getValue(fieldNameOther);
            String bOther = patientB.getValue(fieldNameOther);
            String description = "";
            if (hasThreeOrMoreCharacters(a)) {
                description += "'" + a.toUpperCase() + "' "
                        + (sameButForOne(a, bOther) ? comparisonNotMatch : comparisonMatch) + "  '"
                        + bOther.toUpperCase() + "' ";
            }
            if (hasThreeOrMoreCharacters(b)) {
                description += "'" + b.toUpperCase() + "' "
                        + (sameButForOne(b, aOther) ? comparisonNotMatch : comparisonMatch) + "  '"
                        + aOther.toUpperCase() + "'";
            }
            return description;
        }

        if (hasThreeOrMoreCharacters(a)) {
            return "'" + a.toUpperCase() + "' " + (sameButForOne(a, b) ? comparisonNotMatch : comparisonMatch) + "  '"
                    + b.toUpperCase()
                    + "'";
        }
        return "";
    }

    @Override
    public double score(Patient patientA, Patient patientB) {
        String a = patientA.getValue(fieldName);
        String b = patientB.getValue(fieldName);
        if (hasFieldNameOther()) {
            String aOther = patientA.getValue(fieldNameOther);
            String bOther = patientB.getValue(fieldNameOther);
            boolean matchFound = false;
            if (hasThreeOrMoreCharacters(a) && sameButForOne(a, bOther)) {
                matchFound = true;
            }
            if (hasThreeOrMoreCharacters(b) && sameButForOne(b, aOther)) {
                matchFound = true;
            }
            return ifTrueOrNot(matchFound, 1.0);
        }
        if (hasThreeOrMoreCharacters(a)) {
            return ifTrueOrNot(sameButForOne(a, b), 1);
        }
        return 0;
    }

    private static boolean hasThreeOrMoreCharacters(String value) {

        return value != null && value.length() >= 3;
    }

    // This should return true if both values are the same except for one character.
    // This could be because one characters was changed, so they are the same length
    // and have the
    // same characters except for one position. Or it could be that one character
    // was added or removed, so the lengths differ by one and all other characters
    // are the same.
    private static boolean sameButForOne(String valueA, String valueB) {
        if (valueA == null || valueB == null) {
            return false;
        }
        int lenA = valueA.length();
        int lenB = valueB.length();
        if (Math.abs(lenA - lenB) > 1) {
            return false;
        }
        if (lenA == lenB) {
            // Check for one character difference
            int diffCount = 0;
            for (int i = 0; i < lenA; i++) {
                if (Character.toLowerCase(valueA.charAt(i)) != Character.toLowerCase(valueB.charAt(i))) {
                    diffCount++;
                    if (diffCount > 1) {
                        return false;
                    }
                }
            }
            return diffCount == 1;
        } else {
            // Check for one character addition/removal
            String longer = lenA > lenB ? valueA : valueB;
            String shorter = lenA > lenB ? valueB : valueA;
            int indexLonger = 0;
            int indexShorter = 0;
            int diffCount = 0;
            while (indexLonger < longer.length() && indexShorter < shorter.length()) {
                if (Character.toLowerCase(longer.charAt(indexLonger)) != Character
                        .toLowerCase(shorter.charAt(indexShorter))) {
                    diffCount++;
                    if (diffCount > 1) {
                        return false;
                    }
                    indexLonger++; // Skip the extra character in the longer string
                } else {
                    indexLonger++;
                    indexShorter++;
                }
            }
            return true; // Only one character difference found
        }

    }

}
