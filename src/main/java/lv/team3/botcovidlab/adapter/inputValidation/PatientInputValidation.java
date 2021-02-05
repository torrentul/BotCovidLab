package lv.team3.botcovidlab.adapter.inputValidation;

/**
 * Utility that validates the users input
 *
 * @author Vladislavs Visnevskis
 */
public class PatientInputValidation {

    private PatientInputValidation() {
    }

    /**
     * Method that validates the input name
     *
     * @param name Facebook messenger senders input name
     * @return the validity of input name
     * @author Vladislavs Visnevskis
     */
    public static boolean validateFirstName(String name){
        if (name.matches("[A-Za-z]+") && name.length() >= 3 && name.length() <= 32) {
            return true;
        }
        else return false;
    }

    /**
     * Method that validates the input last name
     *
     * @param lastName Facebook messenger senders input last name
     * @return the validity of input last name
     * @author Vladislavs Visnevskis
     */
    public static boolean validateLastName(String lastName){
        if (lastName.matches("[A-Za-z]+") && lastName.length() >= 3 && lastName.length() <= 32) {
            return true;
        }
        else return false;
    }

    /**
     * Method that validates the temperature
     *
     * @param temperature Facebook messenger senders input temperature
     * @return the validity of input temperature
     * @author Vladislavs Visnevskis
     */
    public static boolean validateTemperature(String temperature){
        try{
            double parsedTemperature = Double.parseDouble(temperature);
            if (parsedTemperature >= 33.0d && parsedTemperature <= 43.0d){
                return true;
            }
            else return false;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Method that validates the input personal code
     *
     * @param personalCode Facebook messenger senders input personal code
     * @return the validity of input personal code
     * @author Vladislavs Visnevskis
     */
    public static boolean validatePersonalCode(String personalCode){
        if (personalCode.matches("\\d{6}\\-\\d{5}") || personalCode.matches("\\d{11}")) {
            return true;
        }
        else return false;
    }

    /**
     * Method that validates the input phone number
     *
     * @param phoneNumber Facebook messenger senders input phone number
     * @return the validity of input personal phone number
     * @author Vladislavs Visnevskis
     */
    public static boolean validatePhoneNumber(String phoneNumber){
        if (phoneNumber.matches("(2|6)\\d{7}") || phoneNumber.matches("(\\+?371)(2|6)(\\d{7})")) {
            return true;
        }
        else return false;
    }

}
