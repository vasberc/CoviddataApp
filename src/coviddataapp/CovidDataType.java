package coviddataapp;
//Enum κλάση με πεδία
public enum CovidDataType {
    DEATHS("deaths", 1, "Θάνατοι"),
    RECOVERED("recovered", 2, "Aσθενείς που έχουν ανακάμψει"),
    CONFIRMED("confirmed", 3, "Επιβεβαιωμένα κρούσματα");
    
    private final String string;
    private final int value;
    private final String labelName;
  
    //ο constructor θέτει τα πεδία των enum που ορίσαμε πιο πάνω
    private CovidDataType(String string, int value, String labelName) {
        this.string = string;
        this.value = value;
        this.labelName = labelName;
    }
   //Επιστρέφει ένα CovidDataType enum ανάλογα το labelName
    public static CovidDataType getByLabelName (String labelName) {
        CovidDataType type = null;
        for(CovidDataType t: CovidDataType.values()){
            if(t.getLabelName() == labelName) {
                type = t; 
                break;
            }
        }
        return type;
    }
   //Επιστρέφει ένα CovidDataType enum ανάλογα το labelName
    public static String getNameByValue (int value) {
        CovidDataType type = null;
        for(CovidDataType t: CovidDataType.values()){
            if(t.value == value) {
                type = t; 
                break;
            }
        }
        return type.labelName;
    }
    public String getString() {
        return string;
    }
    public int getValue() {
        return value;
    }
    public String getLabelName() {
        return labelName;
    }

    @Override
    public String toString() {
        return this.getLabelName();
    }
    
  
}