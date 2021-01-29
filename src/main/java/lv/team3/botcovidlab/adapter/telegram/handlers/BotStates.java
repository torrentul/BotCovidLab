package lv.team3.botcovidlab.adapter.telegram.handlers;

public enum BotStates {

    DEFAULT(""),
    IN_PROGRESS("Profile is filling"),
    QUESTION1("Please, enter your firstname."),
    QUESTION2("Please, enter your lastname."),
    QUESTION3("What is your ID?"),
    QUESTION4("What is your body temperature?"),
    QUESTION5("Do you have a cough?"),
    QUESTION6("Do you have troubles in breathing?"),
    QUESTION7("Do you have a headache?"),
    QUESTION8("Have you been In contact with covid positive person?"),
    QUESTION9("Please, enter your telephone number:"),
    SHOW_MAIN_MENU("Main Menu"),
    PROFILE_FILLED("Your Application Recieved. We will contact with you as soon as possible."),
    GET_SYMPTOMS("GET SYMPTOMS"),
    GET_STATS_COUNTRY("Get country stats"),
    GET_STATS_WORLDWIDE("Get worldwide stats");


    BotStates(String s) {
    }

}
