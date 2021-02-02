package lv.team3.botcovidlab.adapter.inputValidation;

public class PatientInputValidation {

    /**
     * @param name Facebook messenger senders input name
     * @return the validity of input name
     * @Author Vladislavs Višņevskis
     */
    public static boolean validateFirstName(String name){
        if (name.matches("[A-Za-z]+") && name.length() > 3 && name.length() < 32) {
            return true;
        }
        else return false;
    }

    /**
     * @param lastName Facebook messenger senders input last name
     * @return the validity of input last name
     * @Author Vladislavs Višņevskis
     */
    public static boolean validateLastName(String lastName){
        if (lastName.matches("[A-Za-z]+") && lastName.length() > 3 && lastName.length() < 32) {
            return true;
        }
        else return false;
    }

    /**
     * @param temperature Facebook messenger senders input temperature
     * @return the validity of input temperature
     * @Author Vladislavs Višņevskis
     */
    public static boolean validateTemperature(String temperature){
        try{
            double parsedTemperature = Double.parseDouble(temperature);
            if (parsedTemperature > 30.0d && parsedTemperature < 45.0d){
                return true;
            }
            else return false;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * @param personalCode Facebook messenger senders input personal code
     * @return the validity of input personal code
     * @Author Vladislavs Višņevskis
     */
    public static boolean validatePersonalCode(String personalCode){
        if (personalCode.matches("\\d{6}\\-\\d{5}") || personalCode.matches("\\d{11}")) {
            return true;
        }
        else return false;
    }

    /**
     * @param phoneNumber Facebook messenger senders input phone number
     * @return the validity of input personal phone number
     * @Author Vladislavs Višņevskis
     */
    public static boolean validatePhoneNumber(String phoneNumber){
        if (phoneNumber.matches("(2|6)\\d{7}") || phoneNumber.matches("(\\+?371)(2|6)(\\d{7})")) {
            return true;
        }
        else return false;
    }

}
