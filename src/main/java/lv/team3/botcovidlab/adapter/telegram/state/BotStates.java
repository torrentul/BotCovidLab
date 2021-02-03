package lv.team3.botcovidlab.adapter.telegram.state;


/**
 * Enum list of all users states
 * @author Vladislavs Kraslavskis
 */
public enum BotStates {

    DEFAULT(""),
    FILLING_COUNTRY("Filling country"),
    FILLING_PERIOD("fillPeriod"),
    IN_PROGRESS("Saving Profile"),
    QUESTION1("Please, enter your firstname."),
    QUESTION2("Please, enter your lastname."),
    QUESTION3("What is your ID?"),
    QUESTION4("What is your body temperature?"),
    QUESTION5("Do you have a cough?"),
    QUESTION6("Do you have troubles in breathing?"),
    QUESTION7("Do you have a headache?"),
    QUESTION8("Have you been In contact with covid positive person?"),
    QUESTION9("Please, enter your telephone number:"),
    PROFILE_FILLED("Filled");


    BotStates(String s) {
    }

}
