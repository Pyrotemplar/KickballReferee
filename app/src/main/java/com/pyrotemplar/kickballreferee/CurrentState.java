package com.pyrotemplar.kickballreferee;

/**
 * Created by Anna on 6/14/2016.
 */
public class CurrentState {

   private int awayTeamScore;
    private int homeTeamScore;
    private  int strikeCount;
    private int ballCount;
    private int foulCount;
    private int outCount;
    private int inning;
    private int topOrBot;

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getStrikeCount() {
        return strikeCount;
    }

    public void setStrikeCount(int strikeCount) {
        this.strikeCount = strikeCount;
    }

    public int getBallCount() {
        return ballCount;
    }

    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }

    public int getFoulCount() {
        return foulCount;
    }

    public void setFoulCount(int foulCount) {
        this.foulCount = foulCount;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public int getTopOrBot() {
        return topOrBot;
    }

    public void setTopOrBot(int topOrBot) {
        this.topOrBot = topOrBot;
    }
}
