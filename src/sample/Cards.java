package sample;

import java.util.ArrayList;
import java.util.Collections;

public class Cards {
    //create all cards and cards that are currently in play
    private ArrayList<Integer> allCards = new ArrayList<Integer>();
    private ArrayList<Integer> currentCards = new ArrayList<Integer>();

    public Cards() {
        for (int i = 1; i <= 52; i++) {
            allCards.add(i);
        }
    }

    public ArrayList<Integer> getCurrentCards() {
        return currentCards;
    }

    public void setCurrentCards(ArrayList<Integer> currentCards) {
        this.currentCards = currentCards;
    }

    public ArrayList<Integer> getAllCards() {
        return allCards;
    }

    public void setAllCards(ArrayList<Integer> allCards) {
        this.allCards = allCards;
    }
    
    public void shuffleDeck() {
        //shuffle the deck
        Collections.shuffle(allCards);
    }

    public void clearCurrentCards() {
        //clear the current cards
        currentCards.clear();
    }

    public void addCardsToCurrent(int n) {
        //add a number of cards to the current card list
        for (int i = 0; i < n; i++) {
            currentCards.add(allCards.get(i));
        }
    }

    public int getCardFromCurrent(int index) {
        return currentCards.get(index);
    }

    public void addCardsToCurrent(int start, int end) {
        //add a specific part of the list
        for (int i = start; i < end; i++) {
            currentCards.add(allCards.get(i));
        }
    }


    
}