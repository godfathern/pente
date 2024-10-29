import java.util.Random;

public class RandomGenerator {
    char [] letters = new char [15];
    int [] numbers = new int [15];

    public String move(){
        for (char letter = 'A'; letter <= 'O'; letter++){
            letters[letter-'A']=letter;
        }
        for (int i = 0; i<15; i++ ){
            numbers[i] = i + 1;
        }
        Random random = new Random();
        char randomLetter = letters[random.nextInt(letters.length)];
        int randomNumber = numbers[random.nextInt(numbers.length)];
        return randomLetter + String.valueOf(randomNumber);
    }



}