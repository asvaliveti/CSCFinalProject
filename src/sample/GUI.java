package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GUI {
    private Cards cards;
    private ArrayList<Integer> dealerCards = new ArrayList<Integer>();
    private ArrayList<Integer> playerCards = new ArrayList<Integer>();
    private int index = 0;
    private HBox dealerArea = new HBox();
    private HBox playerArea = new HBox();
    private Text yourTotal;
    private VBox secondColumn = new VBox();
    private Stage stage = new Stage();
    private Button hitMeButton = new Button("Hit Me!");

    public GUI() {
        //render the GUI when the GUI object is made
        renderGUI();
    }

    public void renderGUI() {
        //add all other panes to this HBox
        HBox box = new HBox();

        Pane firstColumn = createFirstColumn();
        Pane secondColumn = createSecondColumn();
        Pane thirdColumn = createThirdColumn();
        box.getChildren().addAll(firstColumn, secondColumn, thirdColumn);

        //add main HBox to Scene and set stage properties.
        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Blackjack Game");
        stage.show();
    }

    public Pane createFirstColumn() {
        //use a FlowPane and a VBox to create the first pane with text and an image
        FlowPane pane = new FlowPane();
        Text t = new Text("Deck:");
        pane.getChildren().add(t);

        Image faceDownCard = new Image(".\\cards_png_zip\\PNG\\0.png");
        Pane pane1 = new Pane();
        ImageView iv = new ImageView(faceDownCard);
        iv.setFitHeight(200);
        iv.setFitWidth(140);
        pane1.getChildren().add(iv);

        VBox vbox = new VBox();
        vbox.getChildren().add(pane);
        vbox.getChildren().add(pane1);

        return vbox;
    }

    public Pane createSecondColumn() {
        try {
            //get cards from Cards class
            cards = new Cards();
            cards.shuffleDeck();
            cards.addCardsToCurrent(4);
            index = 4;

            ArrayList<Integer> firstCards = cards.getCurrentCards();

            playerCards.add(firstCards.get(0));
            playerCards.add(firstCards.get(2));

            //render cards for player
            Image card1 = new Image(".\\cards_png_zip\\PNG\\" + playerCards.get(0) + ".png");
            Image card2 = new Image(".\\cards_png_zip\\PNG\\" + playerCards.get(1) + ".png");

            ImageView iv1 = new ImageView(card1);
            iv1.setFitWidth(140);
            iv1.setFitHeight(200);
            ImageView iv2 = new ImageView(card2);
            iv2.setFitWidth(140);
            iv2.setFitHeight(200);

            //calculate total value of player
            ValueCalculator vc = new ValueCalculator(playerCards);
            vc.totalValue();
            int total = vc.getTotal();

            Text currentCardsText = new Text("Your Cards:");
            Text yourTotal = new Text("You are currently at: " + String.valueOf(total));

            playerArea.getChildren().addAll(iv1, iv2);

            Text dealerText = new Text("Dealer's cards");

            //add cards to dealer's cards
            dealerCards.add(firstCards.get(1));
            dealerCards.add(firstCards.get(3));

            //render dealer cards
            Image dealer1 = new Image(".\\cards_png_zip\\PNG\\" + dealerCards.get(0) + ".png");
            Image dealer2 = new Image(".\\cards_png_zip\\PNG\\0.png");
            ImageView dealeriv1 = new ImageView(dealer1);
            dealeriv1.setFitWidth(140);
            dealeriv1.setFitHeight(200);
            ImageView dealeriv2 = new ImageView(dealer2);
            dealeriv2.setFitWidth(140);
            dealeriv2.setFitHeight(200);

            dealerArea.getChildren().addAll(dealeriv1, dealeriv2);

            ValueCalculator valueCalculator = new ValueCalculator(dealerCards);
            valueCalculator.totalValue();
            int dealerTotal = valueCalculator.getTotal();

            if (total == 21) {
                createFinishedModal(total, dealerTotal);
            }

            //add images and text to second column
            secondColumn.getChildren().addAll(currentCardsText, playerArea, yourTotal, dealerText, dealerArea);
        } catch (Exception e) {
            System.out.println("There was an error: " + e);
        }
        return secondColumn;
    }

    //create the third column
    public Pane createThirdColumn() {
        VBox buttonsBox = new VBox();

        Pane p = CreateHitMeButton();
        Pane p1 = createStandButton();

        buttonsBox.getChildren().addAll(p, p1);

        return buttonsBox;
    }

    public Pane CreateHitMeButton() {
        //render button and create onClick event
        hitMeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                cards.addCardsToCurrent(index, index + 1);
                playerCards.clear();
                for (int i = 0; i < index + 1; i++) {
                    if (i != 1 && i != 3) {
                        playerCards.add(cards.getCardFromCurrent(i));
                    }
                }
                index++;

                //rerender cards
                playerArea.getChildren().clear();

                Image image = new Image(".\\cards_png_zip\\PNG\\" + playerCards.get(1) + ".png");
                ImageView iv = new ImageView(image);
                iv.setFitHeight(200);
                iv.setFitWidth(140);
                ValueCalculator calc = new ValueCalculator(playerCards);

                for (int i = 0; i < playerCards.size(); i++) {
                    Image im = new Image(".\\cards_png_zip\\PNG\\" + playerCards.get(i) + ".png");
                    ImageView iv1 = new ImageView(im);
                    iv1.setFitHeight(200);
                    iv1.setFitWidth(140);
                    playerArea.getChildren().add(iv1);
                }
                try {
                    calc.totalValue();
                    int total = calc.getTotal();
                    Text t = new Text("Your current total is: " + total);
                    secondColumn.getChildren().remove(2);
                    secondColumn.getChildren().add(2, t);
                    if (total > 21) {
                        createBustedModal();
                        hitMeButton.setDisable(true);
                    } else if (total == 21) {
                        createFinishedModal(21, 0);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        //add button to box
        VBox box = new VBox();
        box.getChildren().add(hitMeButton);
        box.setPadding(new Insets(20, 10, 0, 10));
        return box;
    }

    public Pane createStandButton() {
        //create button and setOnClick event
        Button standButton = new Button("I Stand!");
        standButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    hitMeButton.setDisable(true);

                    //calculate what happens to each person's cards
                    ValueCalculator dealerCalc = new ValueCalculator(dealerCards);
                    dealerCalc.totalValue();
                    int dealerTotal = dealerCalc.getTotal();

                    ValueCalculator player = new ValueCalculator(playerCards);
                    player.totalValue();
                    int playerTotal = player.getTotal();

                    dealerArea.getChildren().clear();
                    Image dealer1 = new Image(".\\cards_png_zip\\PNG\\" + dealerCards.get(0) + ".png");
                    Image dealer2 = new Image(".\\cards_png_zip\\PNG\\" + dealerCards.get(1) + ".png");
                    ImageView dealeriv1 = new ImageView(dealer1);
                    dealeriv1.setFitWidth(140);
                    dealeriv1.setFitHeight(200);
                    ImageView dealeriv2 = new ImageView(dealer2);
                    dealeriv2.setFitWidth(140);
                    dealeriv2.setFitHeight(200);

                    dealerArea.getChildren().addAll(dealeriv1, dealeriv2);
                    if (dealerTotal <= 16) {    //if dealer's cards are under 16, the dealer gets another card
                        cards.addCardsToCurrent(index, index + 1);
                        dealerCards.clear();
                        for (int i = 0; i < index + 1; i++) {
                            if (i != 0 && i != 2) {
                                dealerCards.add(cards.getCardFromCurrent(i));
                            }
                        }
                        index++;
                        //render new card if dealer gets a new card
                        Image im1 = new Image(".\\cards_png_zip\\PNG\\" + dealerCards.get(dealerCards.size() - 1) + ".png");
                        ImageView dealeriv3 = new ImageView(im1);
                        dealeriv3.setFitWidth(140);
                        dealeriv3.setFitHeight(200);
                        dealerArea.getChildren().add(dealeriv3);
                    }

                    ValueCalculator newDealerCalc = new ValueCalculator(dealerCards);
                    newDealerCalc.totalValue();
                    int newDealerTotal = newDealerCalc.getTotal();

                    //create modal based on who wins
                    createFinishedModal(playerTotal, newDealerTotal);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox standBox = new HBox();
        standBox.getChildren().add(standButton);
        standBox.setPadding(new Insets(10, 10, 0, 10));
        return standBox;
    }

    public void createBustedModal() {
        //create modal if player busts
        Text bustedText = new Text("Oh no, you busted!");
        bustedText.setFill(Color.RED);
        bustedText.setFont(Font.font ("Verdana", 20));

        Button restartButton = new Button("Restart Game");

        VBox secondaryLayout = new VBox();
        secondaryLayout.getChildren().addAll(bustedText, restartButton);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        Stage newWindow = new Stage();
        newWindow.setTitle("Busted!");
        newWindow.setScene(secondScene);

        newWindow.initModality(Modality.WINDOW_MODAL);

        restartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                reset all fields
                secondColumn.getChildren().clear();
                playerArea.getChildren().clear();
                dealerArea.getChildren().clear();
                stage.close();
                newWindow.close();
                playerCards.clear();
                dealerCards.clear();
                cards.clearCurrentCards();
                cards.shuffleDeck();
                hitMeButton.setDisable(false);
                renderGUI();
            }
        });

        newWindow.show();
    }

    public void createFinishedModal(int playerTotal, int dealerTotal) {
        //create modal based on who wins
        Text endGame;
        if (playerTotal < dealerTotal && dealerTotal <= 21) {
            endGame = new Text("Oh no, you lost!");
            endGame.setFill(Color.RED);
        } else if (playerTotal == dealerTotal) {
            endGame = new Text("There was a tie!");
        } else {
            endGame = new Text("Congratulations, you won!");
            endGame.setFill(Color.GREEN);
        }
        endGame.setFont(Font.font ("Verdana", 20));

        Button restartButton = new Button("Restart Game");

        VBox endBox = new VBox();
        endBox.getChildren().addAll(endGame, restartButton);

        Scene endScene = new Scene(endBox, 300, 100);

        Stage endWindow = new Stage();
        endWindow.setTitle("Final Results");
        endWindow.setScene(endScene);

        endWindow.initModality(Modality.WINDOW_MODAL);

        endWindow.show();

        restartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //clear all fields
                secondColumn.getChildren().clear();
                playerArea.getChildren().clear();
                dealerArea.getChildren().clear();
                stage.close();
                endWindow.close();
                playerCards.clear();
                dealerCards.clear();
                cards.clearCurrentCards();
                cards.shuffleDeck();
                hitMeButton.setDisable(false);
                renderGUI();
            }
        });
    }
}