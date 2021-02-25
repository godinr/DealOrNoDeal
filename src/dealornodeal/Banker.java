package dealornodeal;

public class Banker {
    private Game game;
    private Money offer;
    private String lastOffer;

    public Banker(Game game){
        this.game = game;
        this.offer = new Money(0);
        this.lastOffer = "";
    }

    public String getOffer(){
        int caseLeft = 0;
        double total = 0f;

        for (Case c : game.getCases()){
            if (!c.isOpen()){
                caseLeft++;
                total+=c.getValue();
            }
        }

        double avg = Math.floor(total/caseLeft);
        double result = 0;
        if (game.getRound() < 3){
            result = avg/2.5;
        }else if (game.getRound() == 4){
            result = avg/1.5;
        }else {
            result = avg;
        }
        offer.setMoney(result);
        lastOffer = offer.getCurrencyFormat();
        return lastOffer;
    }

    public String getLastOffer(){
        return lastOffer;
    }

}
