package dealornodeal;

import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UIController {

    @FXML private Label instructionLabel;
    @FXML private Label playerCaseLabel;
    @FXML private Label caseValueLabel;
    @FXML private List<Label> labelList;
    @FXML private List<Button> buttonList;
    @FXML private Button keepCaseBtn;
    @FXML private Button tradeCaseBtn;
    @FXML private Label caseOpenLabel;
    @FXML private Label lastOfferLabel;


    private Main main;
    private Game game;
    private Banker banker;
    private Parent parent;
    private Case playerCase;

    private double temp = 2;
    private double temp2 = 2.5;

    public void setMain(Main main) {
        this.main = main;
        startGame();
    }

    public void startGame() {
        this.game = new Game();
        this.banker = new Banker(this.game);
        this.parent = main.getRoot();

        game.initializeCases();
        game.shuffleCases();
        instructionLabel.setText("Pick your lucky case.");


        int index = 0;
        for (Label l : labelList){
            l.setText(new Money(game.getMoneyValues()[index]).getCurrencyFormat());
            index++;
        }
    }

    public void resetGame(){
        for (Button b : buttonList){
            b.setVisible(true);
        }

        for (Label l : labelList){
            l.setVisible(true);
        }

        playerCaseLabel.setText("Your case");
        lastOfferLabel.setText("Last bank offer");
        startGame();

    }


    public boolean check1V1(){
        int count = 0;
        for (Case c: game.getCases()){
            if (!c.isOpen() && !c.isChosen()){
                count++;
            }
        }
        if (count == 1){
            return true;
        }
        return false;
    }


    public void handleClick(Event event) {
        Button btn = (Button) event.getSource();
        int number = Integer.parseInt(btn.getText());
        Case chosen = game.getCaseByNumber(number);

        if (!game.isOfferAvailable()) {

            if (game.getRound() == 0){
                playerCase = chosen;
                caseValueLabel.setText("YOUR CASE NUMBER IS " + number);
                chosen.setChosen(true);
                btn.setVisible(false);
                caseValueLabel.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(temp));

                pause.setOnFinished(e -> {
                    caseValueLabel.setVisible(false);
                    playerCaseLabel.setText(number+"");
                    instructionLabel.setText("Pick 6 case to open.");
                    game.nextRound();
                });
                pause.play();

            }else {
                int caseToOpen = game.getCaseToOpen() - 1;
                chosen.setOpen(true);


                instructionLabel.setText(caseToOpen - game.getPicked() + " more to open.");

                caseValueLabel.setText(new Money(chosen.getValue()).getCurrencyFormat());



                PauseTransition pauseCase = new PauseTransition(Duration.seconds(0.5));
                PauseTransition pause = new PauseTransition(Duration.seconds(temp2));

                pauseCase.setOnFinished(e -> {
                    btn.setVisible(false);
                    caseValueLabel.setVisible(true);
                });
                pauseCase.play();

                pause.setOnFinished(e -> {

                    caseValueLabel.setVisible(false);

                    for (int i = 0; i < labelList.size(); i++){

                        String cleanLabel = Money.removeCurrencyFormat(labelList.get(i).getText());
                        double money = Money.transformCurrencyStringToDouble(cleanLabel);

                        if (chosen.getValue() == money){
                            labelList.get(i).setVisible(false);
                        }
                    }

                    game.incPicked();

                    if (game.getCaseToOpen() == game.getPicked()) {
                        game.resetPicked();
                        caseValueLabel.setText("Banker is making you an offer");
                        caseValueLabel.setVisible(true);
                        PauseTransition bankerPause = new PauseTransition(Duration.seconds(temp));
                        bankerPause.setOnFinished(env -> {
                            caseValueLabel.setText(banker.getOffer());
                            instructionLabel.setText("DEAL OR NO DEAL?");
                            game.setOfferAvailable(true);
                        });
                        bankerPause.play();

                    }
                });
                pause.play();
            }
        }
    }

    public void dealHandler(Event event) {
        caseValueLabel.setText("CONGRATS!!!\nYOU WON\n" + banker.getOffer());

        PauseTransition pause = new PauseTransition(Duration.seconds(temp));
        pause.setOnFinished(env -> {
            caseValueLabel.setVisible(false);
            resetGame();
            startGame();
        });
        pause.play();
    }

    public void noDealHandler(Event event) {
        if (game.isOfferAvailable()) {
            caseValueLabel.setText("No Deal");
            PauseTransition pause = new PauseTransition(Duration.seconds(temp));

            pause.setOnFinished(e -> {
                game.nextRound();
                if (check1V1()){
                    caseValueLabel.setText("Last option!");
                    caseValueLabel.setVisible(true);

                    String keepString = "KEEP CASE";
                    String tradeString = "TRADE CASE";

                    int tradeNum = 0;
                    for (Case c: game.getCases()){
                        if (!c.isOpen() && !c.isChosen()){
                            tradeNum = c.getNumber();
                        }
                    }
                    tradeCaseBtn.setText(tradeString+"\n"+tradeNum);
                    keepCaseBtn.setText(keepString+"\n"+playerCase.getNumber());
                    tradeCaseBtn.textAlignmentProperty().set(TextAlignment.CENTER);
                    keepCaseBtn.textAlignmentProperty().set(TextAlignment.CENTER);
                    tradeCaseBtn.setVisible(true);
                    keepCaseBtn.setVisible(true);
                    instructionLabel.setText("");

                }else {
                    instructionLabel.setText("Pick " +game.getCaseToOpen() + " more cases");
                    lastOfferLabel.setText("Last bank offer\n" +banker.getLastOffer());
                    lastOfferLabel.textAlignmentProperty().set(TextAlignment.CENTER);
                    game.setOfferAvailable(false);
                    caseValueLabel.setVisible(false);
                }

            });
            pause.play();
        }
    }


    public void keepCaseHandler(Event event) throws IOException {
        keepCaseBtn.setVisible(false);
        tradeCaseBtn.setVisible(false);
        caseValueLabel.setText("");
        openCaseAnimation();
    }

    public void tradeCaseHandler(Event event) throws IOException {
        keepCaseBtn.setVisible(false);
        tradeCaseBtn.setVisible(false);
        caseValueLabel.setText("");

        for (Case c : game.getCases()){
            if (!c.isOpen() && !c.isChosen()){
                playerCase = c;
            }
        }
        openCaseAnimation();
    }


    public void openCaseAnimation() throws IOException {

        caseOpenLabel.setVisible(true);

        InputStream imageStream = this.getClass().getResourceAsStream("../assets/case-bigger.png");
        InputStream imageStream2 = this.getClass().getResourceAsStream("../assets/case-open1.png");
        InputStream imageStream3 = this.getClass().getResourceAsStream("../assets/case-open2.png");
        InputStream imageStream4 = this.getClass().getResourceAsStream("../assets/case-open3.png");

        Image image = new Image(imageStream);
        Image image1 = new Image(imageStream2);
        Image image2 = new Image(imageStream3);
        Image image3 = new Image(imageStream4);

        BackgroundSize backgroundSize = new BackgroundSize(300,240, true, true,true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,backgroundSize);
        BackgroundImage backgroundImage2 = new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,backgroundSize);
        BackgroundImage backgroundImage3 = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,backgroundSize);
        BackgroundImage backgroundImage4 = new BackgroundImage(image3, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,backgroundSize);

        Background background = new Background(backgroundImage);
        Background background2 = new Background(backgroundImage2);
        Background background3 = new Background(backgroundImage3);
        Background background4 = new Background(backgroundImage4);

        caseOpenLabel.setBackground(background);

        PauseTransition firstPause = new PauseTransition(Duration.seconds(1));
        PauseTransition secondPause = new PauseTransition(Duration.seconds(2));
        PauseTransition thirdPause = new PauseTransition(Duration.seconds(3));
        PauseTransition finalPause = new PauseTransition(Duration.seconds(6));

        firstPause.setOnFinished(e -> {
            caseOpenLabel.setBackground(background2);
        });
        firstPause.play();

        secondPause.setOnFinished(e -> {
            caseOpenLabel.setBackground(background3);
        });
        secondPause.play();

        thirdPause.setOnFinished(e -> {
            caseOpenLabel.setBackground(background4);
            caseOpenLabel.setTextFill(Color.GOLD);
            caseOpenLabel.setText(new Money(playerCase.getValue()).getCurrencyFormat());
        });
        thirdPause.play();

        finalPause.setOnFinished(e -> {
            caseOpenLabel.setVisible(false);
            caseValueLabel.setVisible(false);
            caseOpenLabel.setText("");
            resetGame();
        });
        finalPause.play();


    }
}
