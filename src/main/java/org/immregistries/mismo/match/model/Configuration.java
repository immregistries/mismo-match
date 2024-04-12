package org.immregistries.mismo.match.model;

import java.io.InputStream;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.immregistries.mismo.match.StringUtils;
import org.immregistries.mismo.match.matchers.AggregateMatchNode;
import org.immregistries.mismo.match.matchers.MatchNode;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Configuration {
    public static final String SHOULD_MATCH_MATCHES = "shouldMatch_Matches";
    public static final String SHOULD_MATCH_POSSIBLE = "shouldMatch_Possible";
    public static final String SHOULD_MATCH_NO_MATCH = "shouldMatch_NoMatch";
    public static final String SHOULD_POSSIBLE_MATCHES = "shouldPossible_Matches";
    public static final String SHOULD_POSSIBLE_POSSIBLE = "shouldPossible_Possible";
    public static final String SHOULD_POSSIBLE_NO_MATCH = "shouldPossible_NoMatch";
    public static final String SHOULD_NO_MATCH_MATCHES = "shouldNoMatch_Matches";
    public static final String SHOULD_NO_MATCH_POSSIBLE = "shouldNoMatch_Possible";
    public static final String SHOULD_NO_MATCH_NO_MATCH = "shouldNoMatch_NoMatch";

    private int configurationId = 0;
    private String worldName = "";
    private String islandName = "";
    private int generation = 0;
    private double generationScore = 0.0;
    private Date generatedDate = null;
    private String hashForSignature = null;
    private String configurationScript = "";
    private boolean configurationScriptRead = false;
    private Set<String> patientFieldSet = null;

    public Set<String> getPatientFieldSet() {
        return patientFieldSet;
    }

    public String getHashForSignature() {
        if (!configurationScriptRead) {
            setup();
        }
        return hashForSignature;
    }

    public void setHashForSignature(String hashForSignature) {
        this.hashForSignature = hashForSignature;
    }

    public String getConfigurationScript() {
        return configurationScript;
    }

    public void setConfigurationScript(String configurationScript) {
        this.configurationScript = configurationScript;
    }

    private int[][] scoringWeights = { /* Should Match*/  {  20,     0,   -5 }, 
    /* Possible */   { -20,    10,    0 }, 
    /* Not Match*/    { -40,   -10,   10 } };
    
    Map<String, Integer> scoringWeightsStrings = new HashMap<String, Integer>();
    {
        scoringWeightsStrings.put(SHOULD_MATCH_MATCHES, 20);
        scoringWeightsStrings.put(SHOULD_MATCH_POSSIBLE, 0);
        scoringWeightsStrings.put(SHOULD_MATCH_NO_MATCH, -5);
        scoringWeightsStrings.put(SHOULD_POSSIBLE_MATCHES, -20);
        scoringWeightsStrings.put(SHOULD_POSSIBLE_POSSIBLE, 10);
        scoringWeightsStrings.put(SHOULD_POSSIBLE_NO_MATCH, 0);
        scoringWeightsStrings.put(SHOULD_NO_MATCH_MATCHES, -40);
        scoringWeightsStrings.put(SHOULD_NO_MATCH_POSSIBLE, -10);
        scoringWeightsStrings.put(SHOULD_NO_MATCH_NO_MATCH, 10);
    }
    
    private AggregateMatchNode match = null;
    private AggregateMatchNode notMatch = null;
    private AggregateMatchNode twin = null;
    private AggregateMatchNode missing = null;
    
    public int getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public double getGenerationScore() {
        return generationScore;
    }

    public AggregateMatchNode getMatch() {
        if (!configurationScriptRead) {
            setup();
        }
        return match;
    }

    public void setMatch(AggregateMatchNode match) {
        this.match = match;
    }

    public AggregateMatchNode getNotMatch() {
        if (!configurationScriptRead) {
            setup();
        }
        return notMatch;
    }

    public void setNotMatch(AggregateMatchNode notMatch) {
        this.notMatch = notMatch;
    }

    public AggregateMatchNode getTwin() {
        if (!configurationScriptRead) {
            setup();
        }
        return twin;
    }

    public void setTwin(AggregateMatchNode twin) {
        this.twin = twin;
    }

    public AggregateMatchNode getMissing() {
        if (!configurationScriptRead) {
            setup();
        }
        return missing;
    }

    public void setMissing(AggregateMatchNode missing) {
        this.missing = missing;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setGenerationScore(double generationScore) {
        this.generationScore = generationScore;
    }

    public Date getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    public Map<String, Integer> getScoringWeightsStrings() {
        if (!configurationScriptRead) {
            setup();
        }
        return scoringWeightsStrings;
    }

    public int[][] getScoringWeights() {
        if (!configurationScriptRead) {
            setup();
        }
        return scoringWeights;
    }

    public void setScoringWeights(int[][] scoringWeights) {
        this.scoringWeights = scoringWeights;
    }


    public Configuration() {
        configurationScriptRead = false;
    }

    public Configuration(final String configurationScript) {
        readConfiguration(configurationScript);
    }

    public Configuration(InputStream inputStream) {
        readConfiguration(inputStream);
    }

    public void setup()
    {
        if (StringUtils.isEmpty(configurationScript)) {
            InputStream inputStream = getClass().getResourceAsStream("Configuration.yml");
            readConfiguration(inputStream);
        }
        else {
            readConfiguration(configurationScript);
        }
        populateFieldSet();
        configurationScriptRead = true;
    }

    private void populateFieldSet()
    {
        patientFieldSet = new HashSet<String>();
        match.populateFieldSet(patientFieldSet);
        notMatch.populateFieldSet(patientFieldSet);
        twin.populateFieldSet(patientFieldSet);
        missing.populateFieldSet(patientFieldSet);
    }

    private void readConfiguration(final String configurationScript) {
        // create input stream reader from string
        InputStream inputStream = new InputStream() {
        private StringReader stringReader = new StringReader(configurationScript);

            @Override
            public int read() {
                try {
                    return stringReader.read();
                } catch (Exception e) {
                    return -1;
                }
            }
        };
        readConfiguration(inputStream);
    }
    
    private void readConfiguration(InputStream inputStream) {
        Yaml yaml = new Yaml(new Constructor(Map.class));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> data = yaml.load(inputStream);
        worldName = (String) data.get("worldName");
        islandName = (String) data.get("islandName");
        generation = (int) data.get("generation");
        generationScore = (double) data.get("generationScore");
        if (data.get("generatedDate") instanceof Date) {
            generatedDate = (Date) data.get("generatedDate");
        }
        else {
            try {
                generatedDate = sdf.parse((String) data.get("generatedDate"));
            } catch (Exception e) {
                e.printStackTrace();
                generatedDate = new Date();
            }
        }
        this.scoringWeightsStrings = (Map<String, Integer>) data.get("scoringWeights");

        scoringWeights[0][0] = scoringWeightsStrings.get(SHOULD_MATCH_MATCHES);
        scoringWeights[0][1] = scoringWeightsStrings.get(SHOULD_MATCH_POSSIBLE);
        scoringWeights[0][2] = scoringWeightsStrings.get(SHOULD_MATCH_NO_MATCH);
        scoringWeights[1][0] = scoringWeightsStrings.get(SHOULD_POSSIBLE_MATCHES);
        scoringWeights[1][1] = scoringWeightsStrings.get(SHOULD_POSSIBLE_POSSIBLE);
        scoringWeights[1][2] = scoringWeightsStrings.get(SHOULD_POSSIBLE_NO_MATCH);
        scoringWeights[2][0] = scoringWeightsStrings.get(SHOULD_NO_MATCH_MATCHES);
        scoringWeights[2][1] = scoringWeightsStrings.get(SHOULD_NO_MATCH_POSSIBLE);
        scoringWeights[2][2] = scoringWeightsStrings.get(SHOULD_NO_MATCH_NO_MATCH);

        match = readAggregateMatchNode("Match", (Map<String, Object>) data.get("Match"));
        notMatch = readAggregateMatchNode("Not Match", (Map<String, Object>) data.get("Not Match"));
        twin = readAggregateMatchNode("Twin", (Map<String, Object>) data.get("Twin"));
        missing = readAggregateMatchNode("Missing", (Map<String, Object>) data.get("Missing"));

        createConfigurationScript();
        hashForSignature = makeHashForSignature();
    }

    private AggregateMatchNode readAggregateMatchNode(String name, Map<String, Object> data) {
        if (data == null)
        {
            throw new RuntimeException("Unable to find matcher '" + name + "' in configuration file");
        }
        MatchNode matchNode = readMatchNode(name, data);
        if (matchNode instanceof AggregateMatchNode) {
            return (AggregateMatchNode) matchNode;
        } else {
            throw new RuntimeException("Match node must be an AggregateMatchNode");
        }
    }

    private MatchNode readMatchNode(String name, Map<String, Object> data) {
        String label = (String) data.get("label");
        if (data.get("enabled") instanceof String) {
            throw new RuntimeException("Unble to read 'enabled' value for '" + data.get("enabled")+ "' in configuration file");
        }
        boolean enabled = (boolean) data.get("enabled");
        String detector = (String) data.get("detector");
        double minScore = (double) data.get("minScore");
        double maxScore = (double) data.get("maxScore");

        if (enabled) {
            if (minScore <=0.0 && maxScore <= 0.0)
            {
                minScore = 0.0;
                maxScore = 0.0;
                enabled = false;
            }
            if (minScore > 1.0) {
                minScore = 1.0;
            }
            if (maxScore > 1.0) {
                maxScore = 1.0;
            }
            if (minScore > maxScore) {
                minScore = maxScore;
            }
        } else {
            minScore = 0.0;
            maxScore = 0.0;
        }

        MatchNode matchNode = null;
        if (detector != null) {
            String className = "org.immregistries.mismo.match.matchers." + detector;
            try {
                // Specific MatchNode type
                
                Class<?> clazz = Class.forName(className);
                matchNode = (MatchNode) clazz.getDeclaredConstructor().newInstance();

                // Set properties specific to MatchNode
            } catch (Exception exception) {
                throw new RuntimeException("Unrecognized matcher: " + className, exception);
            }
            if (data.containsKey("not")) {
                boolean not = (boolean) data.get("not");
                matchNode.setNot(not);
            }
            if (data.containsKey("fieldName")) {
                String fieldName = (String) data.get("fieldName");
                matchNode.setFieldName(fieldName);
            }
            if (data.containsKey("fieldName2")) {
                String fieldName2 = (String) data.get("fieldName2");
                matchNode.setFieldName2(fieldName2);
            }
            if (data.containsKey("fieldName3")) {
                String fieldName3 = (String) data.get("fieldName3");
                matchNode.setFieldName3(fieldName3);
            }
            if (data.containsKey("fieldNameOther")) {
                String fieldNameOther = (String) data.get("fieldNameOther");
                matchNode.setFieldNameOther(fieldNameOther);
            }
            if (data.containsKey("splitParameter")) {
                String splitParameter = (String) data.get("splitParameter");
                matchNode.setSplitParameter(splitParameter);
            }
        } else {
            // AggregateMatchNode, contains nested matchNodes
            AggregateMatchNode aggregateMatchNode = new AggregateMatchNode(label, minScore, maxScore);
            for (String key : data.keySet()) {
                if (key.equals("label") || key.equals("enabled") || key.equals("detector") || key.equals("minScore")
                || key.equals("maxScore")) {
                    continue;
                }
                aggregateMatchNode.add(readMatchNode(key, (Map<String, Object>) data.get(key)));
            }
            matchNode = aggregateMatchNode;
        }
        
        matchNode.setMatchName(name);
        matchNode.setMatchLabel(label);
        matchNode.setEnabled(enabled);
        matchNode.setMinScore(minScore);
        matchNode.setMaxScore(maxScore);


        return matchNode;
    }

  private String makeHashForSignature() {
    try {
      return generateShortHash(configurationScript, 15);
    }
    catch (NoSuchAlgorithmException e) {
      return "!! UNABLE TO GENERATE HASH !! " + e.getMessage();
    }
  }

  private static String generateShortHash(String input, int length) throws NoSuchAlgorithmException {
    if (length > 44) { // Base64-encoded SHA-256 strings are 44 characters long, so we limit the length.
      throw new IllegalArgumentException("Maximum length should be 44 or less.");
    }
    // Create a SHA-256 digest
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = digest.digest(input.getBytes());
    // Encode the bytes to Base64
    String base64Encoded = Base64.getEncoder().encodeToString(hashBytes);
    // Truncate or pad the string to the specified length
      return base64Encoded.length() > length ? base64Encoded.substring(0, length) : base64Encoded;
  }


    public String toString()
    {
        return configurationScript;
    }
    public void createConfigurationScript() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("worldName: " + worldName + "\n");
        sb.append("islandName: " + islandName + "\n"); 
        sb.append("generation: " + generation + "\n");
        sb.append("generationScore: " + generationScore + "\n"); 
        sb.append("generatedDate: \"" + sdf.format(generatedDate) + "\"\n");
        sb.append("scoringWeights: " + "\n");
        sb.append("  " + SHOULD_MATCH_MATCHES + ": " + scoringWeights[0][0] + "\n");
        sb.append("  " + SHOULD_MATCH_POSSIBLE + ": " + scoringWeights[0][1] + "\n");
        sb.append("  " + SHOULD_MATCH_NO_MATCH + ": " + scoringWeights[0][2] + "\n");
        sb.append("  " + SHOULD_POSSIBLE_MATCHES + ": " + scoringWeights[1][0] + "\n");
        sb.append("  " + SHOULD_POSSIBLE_POSSIBLE + ": " + scoringWeights[1][1] + "\n");
        sb.append("  " + SHOULD_POSSIBLE_NO_MATCH + ": " + scoringWeights[1][2] + "\n");
        sb.append("  " + SHOULD_NO_MATCH_MATCHES + ": " + scoringWeights[2][0] + "\n");
        sb.append("  " + SHOULD_NO_MATCH_POSSIBLE + ": " + scoringWeights[2][1] + "\n");
        sb.append("  " + SHOULD_NO_MATCH_NO_MATCH + ": " + scoringWeights[2][2] + "\n");

        printAggregateMatchNode("Match", match, sb, "");
        printAggregateMatchNode("Not Match", notMatch, sb, "");
        printAggregateMatchNode("Twin", twin, sb, "");
        printAggregateMatchNode("Missing", missing, sb, "");

        configurationScript = sb.toString();
    }

    private void printAggregateMatchNode(String matchName, AggregateMatchNode matchNode, StringBuilder sb, String indent) {
        sb.append(indent + matchName + ":\n");
        sb.append(indent + "  label: " + matchNode.getMatchLabel() + "\n");
        sb.append(indent + "  enabled: " + matchNode.isEnabled() + "\n");
        sb.append(indent + "  minScore: " + matchNode.getMinScore() + "\n");  
        sb.append(indent + "  maxScore: " + matchNode.getMaxScore() + "\n");
        for (MatchNode mn : matchNode.getMatchNodeList()) {
            if (mn instanceof AggregateMatchNode) {
                printAggregateMatchNode(mn.getMatchName(), (AggregateMatchNode) mn, sb, indent + "  ");
            } else {
                sb.append(indent + "  " + mn.getMatchName() + ":\n");
                sb.append(indent + "    label: " + mn.getMatchLabel() + "\n");
                sb.append(indent + "    detector: " + mn.getClass().getSimpleName() + "\n");
                if (StringUtils.isNotEmpty(mn.getFieldName())) {
                    sb.append(indent + "    fieldName: " + mn.getFieldName() + "\n");
                }
                if (StringUtils.isNotEmpty(mn.getFieldName2())) {
                    sb.append(indent + "    fieldName2: " + mn.getFieldName2() + "\n");
                }
                if (StringUtils.isNotEmpty(mn.getFieldName3())) {
                    sb.append(indent + "    fieldName3: " + mn.getFieldName3() + "\n");
                }
                if (StringUtils.isNotEmpty(mn.getFieldNameOther())) {
                    sb.append(indent + "    fieldNameOther: " + mn.getFieldNameOther() + "\n");
                }
                if (StringUtils.isNotEmpty(mn.getSplitParameter())) {
                    sb.append(indent + "    splitParameter: \"" + mn.getSplitParameter() + "\"\n");
                }
                if (mn.isNot()) {
                    sb.append(indent + "    not: " + mn.isNot() + "\n");
                }
                sb.append(indent + "    enabled: " + mn.isEnabled() + "\n");
                sb.append(indent + "    minScore: " + mn.getMinScore() + "\n");
                sb.append(indent + "    maxScore: " + mn.getMaxScore() + "\n");
            }
        }
        
    }

}
