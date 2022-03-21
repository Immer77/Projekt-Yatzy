package yatzy.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Yatzy {
    // Face values of the 5 dice.
    // 1 <= values[i] <= 6.
    private int[] values = new int[5];

    // Number of times the 5 dice have been thrown.
    // 0 <= throwCount <= 3.
    private int throwCount = 0;

    // Random number generator.
    private Random random = new Random();

    public Yatzy() {
        //
    }

    /**
     * Returns the 5 face values of the dice.
     */
    public int[] getValues() {
        return this.values; // Returnere listen for de værdier der er i value array.
    }

    /**
     * Sets the 5 face values of the dice. Pre: values contains 5 face values in
     * [1..6]. Note: This method is only meant to be used for test, and
     * therefore has package visibility.
     */
    void setValues(int[] values) {
        this.values = values;
    }

    /**
     * Returns the number of times the 5 dice has been thrown.
     */
    public int getThrowCount() {
        // TODO Check
        return this.throwCount; //returnerer Throwcount mængden af kast
    }

    /**
     * Resets the throw count.
     */
    public void resetThrowCount() {
        // TODO Check
        this.throwCount = 0; //Sætter throwcount til 0
    }

    /**
     * Rolls the 5 dice. Only roll dice that are not hold. Pre: holds contain 5
     * boolean values.
     */
    public void throwDice(boolean[] holds) {
        // TODO Check
        for (int i = 0; i < values.length; i++) {
            if (!holds[i]) { //Hvis han ikke holder på terningen skal den kastes
                values[i] = random.nextInt(1, 6);
            }
        }
        throwCount++;

    }

    // -------------------------------------------------------------------------

    /**
     * Returns all results possible with the current face values. The order of
     * the results is the same as on the score board. Note: This is an optional
     * method. Comment this method out, if you don't want use it.
     */
    public int[] getResults() {
        int[] results = new int[15];
        for (int i = 0; i <= 5; i++) {
            results[i] = this.sameValuePoints(i + 1);
        }
        results[6] = this.onePairPoints();
        results[7] = this.twoPairPoints();
        results[8] = this.threeSamePoints();
        results[9] = this.fourSamePoints();
        results[10] = this.fullHousePoints();
        results[11] = this.smallStraightPoints();
        results[12] = this.largeStraightPoints();
        results[13] = this.chancePoints();
        results[14] = this.yatzyPoints();

        return results;
    }

    // -------------------------------------------------------------------------

    // Returns an int[7] containing the frequency of face values.
    // Frequency at index v is the number of dice with the face value v, 1 <= v
    // <= 6.
    // Index 0 is not used.
    public int[] calcCounts() {
        int[] numControl = new int[7]; // Opretter et array på 7 pladser
        int tmp = 0; //Tmp variablen er til at optælle flere
        for (int value : values) { //For hver value i values array
            tmp = value; // Vi sætter variablen tmp med værdien i value
            numControl[tmp]++; // Vi plusser pladsen op på tmp index plads ++
        }
        return numControl; // Returner numcontrol værdien
    }

    /**
     * Returns same-value points for the given face value. Returns 0, if no dice
     * has the given face value. Pre: 1 <= value <= 6;
     */
    public int sameValuePoints(int value) {
        int sameValuePoints = 0;
        for (int j : values) { // For Hver element i values hvis value er det samme så plusser vi den op
            if (value == j) {
                sameValuePoints++;
            }
        }
        return sameValuePoints * value; //Herefter ganges der så meget value så man får det rigtige resultat
    }

    /**
     * Returns points for one pair (for the face value giving highest points).
     * Returns 0, if there aren't 2 dice with the same face value.
     */
    public int onePairPoints() {
        int[] counts = calcCounts(); // til at tælle om der er flere end 1 terning der har samme værdi
        int sumOfPair = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] >= 2) { //Hvis counts er større end eller lig med 2
                sumOfPair = counts[i] * i; // Så kan vi ligge i som er facevalue og gange den med 2 for at få points

            }
        }
        return sumOfPair;
    }

    /**
     * Returns points for two pairs (for the 2 face values giving highest
     * points). Returns 0, if there aren't 2 dice with one face value and 2 dice
     * with a different face value.
     */
    public int twoPairPoints() {
        // TODO
        int[] count = calcCounts();
        int sumOfPairs = 0;
        for (int i = 1; i < count.length; i++) {
            if(count[i] >= 2 && count[i] != 4){
                sumOfPairs += i*2; // bruger += med da vi regner med 2 par og skal have skrevet det andet par point oveni
            }
        }
        return sumOfPairs;
    }

    /**
     * Returns points for 3 of a kind. Returns 0, if there aren't 3 dice with
     * the same face value.
     */
    public int threeSamePoints() {
        // TODO
        int[] counts = calcCounts();
        int points = 0;
        for (int i = 0; i < counts.length; i++) {
            if(counts[i] >= 3){ // Hvis der er 3 ens når man kigger i count
                points = i * 3; // Så længe at man ganger med 3 på den plads som har det indhold vil det gå op
            }
        }
        return points;
    }

    /**
     * Returns points for 4 of a kind. Returns 0, if there aren't 4 dice with
     * the same face value.
     */
    public int fourSamePoints() {
        // TODO
        int[] counts = calcCounts();
        int points = 0;
        for (int i = 0; i < counts.length; i++) {
            if(counts[i] >= 4){ //Hvis der er 4 ens i count
                points = i * 4; // Ganger man med 4 vil man få det ønskede resultat
            }
        }
        return points;

    }

    /**
     * Returns points for full house. Returns 0, if there aren't 3 dice with one
     * face value and 2 dice a different face value.
     */
    public int fullHousePoints() {
        // TODO
        int[] count = calcCounts();
        int par1 = 0;
        int par2 = 0;
        for (int i = 0; i < count.length; i++) {
            if(count[i] == 3){
                par1 = i*3; //Laver først det ene par som tager 3 ens
            }
        }
        for (int i = 0; i < count.length; i++) {
            if(count[i] == 2){ //Andet par som laver 2 ens
                par2 = i * 2;
            }
        }
        if(par1 > 0 && par2 > 0){ //Tjekker om begge er over 0, er de det så vil vi returnere det fulde hus
            return par1 + par2;
        }
        return 0;
    }

    /**
     * Returns points for small straight. Returns 0, if the dice are not showing
     * 1,2,3,4,5.
     */
    public int smallStraightPoints() {
        // TODO
        int[] counts = calcCounts();
        int points = 0;
        for (int i = 1; i < counts.length - 1; i++) { //Starter fra index 1 da vi kun skal have fra 1-5 heraf minus 1 i length
            if (counts[i] != 1) { // Hvis en af count er forskellig fra 1 vil det betyde at der ikke er en small-straight
                return 0;
            }
            points += i; // Ellers plusser vi lige
        }
        return points;
    }

    /**
     * Returns points for large straight. Returns 0, if the dice is not showing
     * 2,3,4,5,6.
     */
    public int largeStraightPoints() {
        // TODO
        int[] counts = calcCounts();
        int points = 0;
        for (int i = 2; i < counts.length; i++) { //Skal gå fra 2-6 for at få en largestraight
            if(counts[i] != 1){ //Ligesom med smallstraight
                return 0;
            }
            points += i;
        }
        return points;
    }

    /**
     * Returns points for chance.
     */
    public int chancePoints() { //Samler alle pointene
        // TODO
        int sum = 0;
        for (int element : values){ //Looper igennem det
            sum+= element;
        }
        return sum;
    }

    /**
     * Returns points for yatzy. Returns 0, if there aren't 5 dice with the same
     * face value.
     */
    public int yatzyPoints() {
        // TODO
        int[] counts = calcCounts();
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] == 5){ //Tjekker at hvis der er en der er der 5gange så er det yatzy
                return 50;
            }
        }
        return 0;
    }

}
