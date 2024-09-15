import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

/**
 * Reads a list of numbers, and can reconstruct the corresponding list of palindromes,
 * produce the size of the largest magic set, and the content of that magic set.
 * 
 * Usage:
 * 
 * The program's functionality is split between 3 different tasks, which
 * will be executed according to the first number given as input.
 * 
 * The tasks are as follows:
 * 
 * TASK 1: Output the list after all the numbers have been restored to palindromes.
 * 
 * TASK 2: Output the size of the largest magic set present in the list. (A magic set is a subset of
 *         palindromes present in the list in which any of the shorter elements can be obtained from 
 *         the largest one - known as "X" - by removing the same amount of digits on both ends)
 *         The program will output "1" if the correct list does not contain any magical set.
 * 
 * TASK 3: Output the elements from which the largest magic set is formed, listed in ascending order.
 *         If there are several such magic sets on the correct list, output the one with the greatest "X".
 *         If there are no magic sets, the greatest number in the correct list is output.
 * 
 * In order for a number to be restored to its correct value, it needs increase to the nearest palindrome which
 * is greater than or equal to the number in question.
 * 
 * 
 * The program takes as input the following values:
 * 
 * "taskNumber"
 * "N"
 * "KingsList[0] KingsList[1] KingsList[2] (...) KingsList[N-1]"
 * 
 * where:
 * 
 * "taskNumber" - the number of the task that is going to be executed,
 * "N" - the size of the array that is going to be given as input,
 * "KingsList" - an array of size N composed of natural numbers, all with an odd number of digits.
 * 
 * It should be noted that all input must consist of natural numbers.
 * It should also be noted that "KingsList" accepts natural numbers that have up to 17 digits.
 * 
 * If the user inputs any other value than 1, 2 or 3 for "taskNumber", the program will not run.
 * If the user does not provide N different values for "KingsList", the program will not run.
 * 
 * 
 * 
 * @author 
 * @ID
 * @author 
 * @ID 
 * 
 */

class KingsPalindromeList {

    // Takes input and provides expected output, based on the task number.
    private void KingsParser() {
        Scanner scan = new Scanner(System.in); // Creates a Scanner object for further use.

        // Initialize variables and provide input for the problem.
        int taskNumber = scan.nextInt();
        int N = scan.nextInt();
        long[] KingsList= new long[N];

        // Provide input for the King's List.
        for(int i=0; i < N; i++){
            KingsList[i] = scan.nextLong();
        }

        // Creates an array where all the terms have been turned to palindromes.
        long[] PetersList = new long[N];
        PetersList = restorePalindrome(KingsList);

        scan.close(); // Closes the scanner.

        Set<String> setOfPalindromes = new HashSet<>();

        for (long num: PetersList){
            setOfPalindromes.add(String.valueOf(num));
        }

        // Creates an array of the largest magic set.
        String[] magicSet = largestMagicSet(setOfPalindromes.toArray(new String[0]));

        // Provides output, as described in the documentation.
        if (taskNumber == 1){
            // Outputs all the elements of the palindrome list.
            for(long i: PetersList){
                System.out.print(i + " ");
            }

        } else if (taskNumber == 2){
            // Outputs the length of the largest magic set.
            System.out.println(magicSet.length);

        } else if (taskNumber == 3){

            /* Outputs the largest magic set in ascending order.
             * Outputs the largest element if there are no magic sets.
             */
            for(long i: insertionSort(magicSet)){
                System.out.print(i+" ");
            }
        }
            
    }

    // Checks if  a given number is a palindrome.
    private boolean isPalindrome(long num) {
        String numToString = Long.toString(num);
        int frontPointer = 0;
        int rearPointer = numToString.length() - 1;

        for(int i = 0; i < numToString.length() / 2; i++) {
            if (numToString.charAt(frontPointer) != numToString.charAt(rearPointer)){
                return false;
            }

            frontPointer++;
            rearPointer--;
        }

        return true;
        
    }
    
    // Turns a given array into a set of palindromes.
    private long[] restorePalindrome(long[] arrayOfPalindromes) {
        for(int i = 0; i < arrayOfPalindromes.length; i++) {
            long currentPalindrome = arrayOfPalindromes[i];
            
            while(!isPalindrome(currentPalindrome)) {
                currentPalindrome++;
            }

            arrayOfPalindromes[i] = currentPalindrome;
        }

        return arrayOfPalindromes;

    }

    // Finds the largest magic set.
    private String[] largestMagicSet(String[] myArr){

        // Creates an array which will record the number of elements a magic set contains if the element at index i is "X".
        int[] personalSet=new int[myArr.length];

        for(int i = 0; i < myArr.length; i++){
            personalSet[i] = 0;
        }

        for(int i = 0; i < myArr.length; i++){
            for(int j=0; j < myArr.length; j++){
                if(myArr[i].length() >= myArr[j].length()){

                    int startMiddle = (myArr[i].length() / 2) - (myArr[j].length() / 2);
                    int endMiddle = (myArr[i].length() / 2) + (myArr[j].length() / 2) + 1;

                    if(myArr[i].substring(startMiddle, endMiddle).equals(myArr[j])){
                        personalSet[i]++;
                    }
                }

            }
        }

        /* Creates variables which will store the index of "X" in the biggest magic set
         * and the length of this set.
        */
        int correctIndex = 0;
        int maxValue = 0;

        for(int i = 0; i < personalSet.length; i++){
            if(maxValue < personalSet[i]){
                maxValue = personalSet[i];
                correctIndex = i;
            }
        }

        // Returns an array with the largest element if there are no magic sets with length greater than 1.
        if(maxValue == 1){
            return oneElementArr(myArr);
        }

        /* Check if there are any magic sets which both have maximum length.
         * If there are, store the index for the bigger "X".
         */
        for(int i = 0; i < myArr.length - 1; i++){
            for(int j=i+1; j<myArr.length; j++){
                if(personalSet[i] == maxValue && personalSet[j] == maxValue){
                    if(Long.valueOf(myArr[i]) > Long.valueOf(myArr[j])){
                        correctIndex = i;
                    } else if(Long.valueOf(myArr[i]) < Long.valueOf(myArr[j])){
                        correctIndex = j;
                    }
                }
            }
        }

        // Store the largest magic set in a string set.
        Set<String> bigMagicSet = new HashSet<>();

        for(int i = 0; i < myArr.length; i++){
            int startMiddle = (myArr[correctIndex].length() / 2) - (myArr[i].length() / 2);
            int endMiddle = (myArr[correctIndex].length() / 2) + (myArr[i].length() / 2) + 1;

            if (myArr[correctIndex].length() >= myArr[i].length()){
                if(myArr[correctIndex].substring(startMiddle, endMiddle).equals(myArr[i])){
                    bigMagicSet.add(myArr[i]);
                }
            }

        }

        // Turns the previous set into a string array.
        String[] bigMagicArray = bigMagicSet.toArray(new String[0]);
        
        return bigMagicArray;
    }


    // Returns a sorted integer array.
    private long[] insertionSort(String[] myArr){
        long[] longArr = new long[myArr.length];

        for(int i = 0; i < myArr.length; i++){
            longArr[i] = Long.valueOf(myArr[i]);
        }


        for(int i = 1; i < longArr.length; i++){
            long key = longArr[i];
            int j = i - 1;

            while(j >= 0 && longArr[j] > key){
                longArr[j+1] = longArr[j];
                j -= 1;
            }

            longArr[j+1] = key;
        }

        return longArr;

    }

    // Returns an array containing the largest element from the array given as a parameter.
    private String[] oneElementArr(String[] myArr){
        long maxValue = 0;
        for(String i: myArr){
            if(Long.valueOf(i) > maxValue){
                maxValue = Long.valueOf(i);
            }
        }

        String[] maxValueArr = new String[]{String.valueOf(maxValue)};

        return maxValueArr;
    }

    public static void main(String[] args){
        new KingsPalindromeList().KingsParser();
    }
}