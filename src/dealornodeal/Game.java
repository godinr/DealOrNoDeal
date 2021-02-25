package dealornodeal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {

    static double[] moneyValues = {0.01, 1, 5, 10, 25, 50, 75, 100,
            200, 300, 400, 500, 750, 1000, 5000, 10000, 25000, 50000,75000,
            100000, 200000, 300000, 400000, 500000, 750000, 1000000};

    private Case[] cases;
    private int round;
    private int picked;
    private boolean offerAvailable;

    public Game(){
        this.cases = new Case[26];
        this.round = 0;
        this.picked = 0;
        this.offerAvailable = false;
    }

    public Case[] getCases(){
        return this.cases;
    }

    public Case getCaseByNumber(int num){
        for (int i = 0; i < cases.length; i++){
            if (cases[i].getNumber() == num) return cases[i];
        }
        return null;
    }

    public int getRound(){
        return round;
    }

    public void nextRound(){
        round++;
    }

    public int getPicked(){
        return picked;
    }

    public void incPicked(){
        picked++;
    }

    public void resetPicked(){
        picked = 0;
    }

    public boolean isOfferAvailable(){
        return offerAvailable;
    }

    public void setOfferAvailable(boolean offerAvailable){
        this.offerAvailable = offerAvailable;
    }

    public void initializeCases(){
        for (int i = 0; i < cases.length; i++){
            cases[i] = new Case(i+1,moneyValues[i],false, false);
        }
    }

    public void setCaseNumbers(){
        for(int i = 0; i < cases.length; i++){
            cases[i].setNumber(i+1);
        }
    }

    public void shuffleCases(){
        List<Case> tempList = Arrays.asList(cases);
        Collections.shuffle(tempList, new Random(10));
        tempList.toArray(cases);
        setCaseNumbers();
    }

    public int getCaseToOpen(){
        switch(round){
            case 1:
                return 6;
            case 2:
                return 5;
            case 3:
                return 4;
            case 4:
                return 3;
            case 5:
                return 2;
            default:
                return 1;
        }
    }
    public double[] getMoneyValues(){
        return moneyValues;
    }
}
