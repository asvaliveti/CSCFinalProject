package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ValueCalculator {
    //have an ArrayList of cards and count the total
    private ArrayList<Integer> numbers;
    private int total = 0;

    public ValueCalculator(ArrayList<Integer> n) {
        numbers = n;
    }

    //use text io to find the value of the cards
    public int getValue(int cardNumber) throws FileNotFoundException {
        File f = new File(".\\src\\sample\\values.txt");
        Scanner scan = new Scanner(f);
        int value = 0;
        while(scan.hasNext()) {
            String s = scan.nextLine();
            String[] arr = s.split(" ");
            if (Integer.parseInt(arr[1]) >= cardNumber && Integer.parseInt(arr[0]) <= cardNumber) {
                value = Integer.parseInt(arr[2]);
            }
            if (value == 1) {
                if (total + 11 > 21) {
                    value = 1;
                } else {
                    value = 11;
                }
            }
        }
        return value;
    }
    //get the total value of all the cards
    public void totalValue() throws FileNotFoundException {
        for (int i = 0; i < numbers.size(); i++) {
            total += getValue(numbers.get(i));
        }
    }

    //return the total
    public int getTotal() {
        return this.total;
    }
}