package lv.team3.botcovidlab.adapter.twitter;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.CovidStatsProcessor;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Status;

import java.util.Date;


public class Tweeter {
    Twitter twitter;
    Status status;

    public Tweeter() {
        twitter = TwitterFactory.getSingleton();
    }

    private void postTweetAbout(String country) throws TwitterException {
        try {
            status = twitter.updateStatus("Hei, there! Just testing out my new app! Hello, from " + country);
            System.out.println("Status updated with " + status.getText());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws TwitterException {
        Tweeter tweeter = new Tweeter();
        tweeter.postTweetAbout("Latvia");
    }
}
