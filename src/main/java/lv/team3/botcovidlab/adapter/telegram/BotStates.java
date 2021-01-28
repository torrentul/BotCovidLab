package lv.team3.botcovidlab.adapter.telegram;

public enum BotStates {

    DEFAULT(""),
    QUESTION1("Please, enter your firstname."),
    QUESTION2("Please, enter your lastname."),
    QUESTION3("What is your ID?"),
    QUESTION5("What is your body temperature?"),
    QUESTION6("Do you have any symptoms? If Yes, what kind of?"),
    QUESTION7("Have you been In contact with covid positive person?"),
    QUESTION8("Please, enter your telephone number:"),
    PROFILE_FILLED("Your Application Recieved. We will contact with you as soon as possible.");


    BotStates(String s) {
    }
}
