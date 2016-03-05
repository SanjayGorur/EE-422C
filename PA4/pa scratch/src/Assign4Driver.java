/** WordLadderDriver ****************************************
 * Driver to manage the Word Ladder and Dictionary Classes.
 * find word ladders between pairs from word file.
 * check the data from the dictionary.
 * print out the word ladders for each pair.
 * 
 * Section : F 2:00 - 3:30pm
 * UT EID: rpm953, hjw396
 * @author Ronald Macmaster, Horng-Bin Justin Wei 
 * @version 1.01 2/27/2016
 ************************************************************/



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assign4Driver{

	public static void main(String[] args){
		// Create dictionary
		// Dictionary dict = new Dictionary("A4words.dat");	//takes the input from file of that name
		// System.out.println(dict);
		// System.out.println("Dictionary Word count: " + dict.getCapacity());

		// Create a word ladder solver object
		

		try{
			// 1) Read dictionary and filename
			String dictfile; // dictionary filename
			String pairfile; // word pairs filename
			
			if(args.length != 2){ // invalid commandline arguments
				Scanner input = new Scanner(System.in);
				// prompt filenames
				System.out.println("Enter your dictionary filename: ");
				dictfile = input.next();
				System.out.println("Enter your word pairs filename: ");
				pairfile = input.next();
			}
			else{
				dictfile = args[0];
				pairfile = args[1];
			}
			
			// 2) create a wordladdersolver object (includes graph and dictionary)
			Assignment4Interface wordLadderSolver = new WordLadderSolver(dictfile);	
			
			// 3) file input loop:
			FileReader reader = new FileReader(pairfile);
			BufferedReader fhand = new BufferedReader(reader);
			
			String line;
			System.out.println("********************"); //print banner
			while((line =  fhand.readLine()) != null){
				Pattern pattern = Pattern.compile("[a-zA-Z]{5}[\\s]+[a-zA-Z]{5}");
				Matcher matcher = pattern.matcher(line);
				
				if(matcher.find()){
					String[] words = matcher.group().split("[\\s]+");
					String startword = words[0];
					String endword  = words[1];
					System.out.println("word1: " + word1 + "\tword2: " + word2);
					
					List<String> wordladder = wordLadderSolver.computerLadder(startword, endword);
					
					
					
					System.out.println("********************"); //print banner
				}
				else{
					System.err.println("Error: Input line does not contain a valid 5 letter word pair.");
					System.out.println("********************"); //print banner
					continue;
				}
				
				
				
				
			/**
				a. Read in word pair
				b. Output delimiter *************
				c. If word data invalid: throw exception and repeat
				d. Compute Ladder from start to end
				e. If no ladder exists: Print no ladder exists! And repeat
				f. Else: print word ladder and repeat
			4)  Output delimiter *************
			5) End driver. **/
			
			
		}
		catch(Exception err){
			System.err.println("fail!!");
		}
		
		/*
		 * try{ List<String> result = wordLadderSolver.computeLadder("money",
		 * "honey"); boolean correct = wordLadderSolver.validateResult("money",
		 * "honey", result); } catch (NoSuchLadderException e){
		 * 
		 * e.printStackTrace(); }
		 */
	}
}

//they are giving us two inputs: first is dictionary, second contains the sets of two words