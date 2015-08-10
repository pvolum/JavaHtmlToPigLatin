//TODO: modulize get command and get word code blacks, refactor toPig for capitalization in all and punctiation in all
package hw_3;
import java.util.Scanner;
import java.util.Formatter;
import java.io.*;

public class WebToPigLatin {
	
	static String inputFile;
	static String outputFile;
	Scanner input;
	FileWriter output;
	int index;
	
	//constructor
	WebToPigLatin(String in, String out){
		try {
			inputFile = in;
			input = new Scanner (new File(inputFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("\nUsage: java WebToPigLatin inputFile outputFile\nMust have two command-line parameters");
		}
        try {
			output = new FileWriter(outputFile);
			output.append('f');
			
		} catch (IOException e) {
			System.out.println("IO EXCEPTION");
			e.printStackTrace();
		}
		
	}
	
	public String getCommand(String s, int i){
		String result = "";
		boolean stop = false; 
		for( int e = i; stop == false; e++)
		{
			if (s.charAt(e)=='>'){
				result = s.substring(i, (e+1));
				stop = true;
				index = (e + 1);
			}
			
		}

		return result;
	}
	
	//gets next line from input file and formats it
	String nextLine(){	//outputs line of formatted html code/content
		String s = this.input.nextLine();
		String result= "";
		String word ="";
		boolean stop = false;
		for( index = 0; index < s.length(); index++)
		{
			if (s.charAt(index)=='<')
				result = result + getCommand(s, index);
			
			if(index<s.length() && s.charAt(index) != ' '){
				result = result + toPig(getWord(s, (index))) + " ";
			}	
		}
			return result;
	}
	
	String getWord(String s, int i)
	{
		String result = "";
		boolean stop = false;
		int e;
		for( e = i; stop == false && e<s.length(); e++)
		{
			if ( s.charAt(e)==' ' ){
				result = s.substring(i, (e + 1));
				stop = true;
				index = (e - 1);
			}
			else if(s.charAt(e)=='<'){
				result = s.substring(i, (e + 1));
				stop = true;
				index = (e - 1);
			}
			 
			
		}
	
			
			//result = result.substring(0, result.charAt(result.length() - 2));
		//System.out.print ( " RETURNED: " + index);
		return result;
	}
	

	
	//Returns location of first vowel and stores first vowel in callers firstV variable
	int getVowel(String word){
		int firstVLoc = 1000;
		String vowels = "AaEeIiOoUuYy";
		for (int i = 0; i<word.length(); i++){		//finds first v and location if it exists
			for (int e = 0; e<vowels.length(); e++){
				if(word.charAt(i) == vowels.charAt(e)){
					firstVLoc = i;
					return firstVLoc;
				}
				
			}
		}
		return firstVLoc;
	}
	
	//checks if a word needs to be recapitalized after formatting
	public boolean needCap(String word){
		if(Character.isUpperCase(word.charAt(0)))	//keep track if formatted word needs capitalization
			return true;
		else
			return false;
	}
	
	//adds capitalization to formatted word
	public String capFormat (String result){
		result = result.toLowerCase();
		result = Character.toUpperCase(result.charAt(0)) + result.substring(1);
		return result;
	}
	
	//checks if punctuation needs to be added to formatted word
	public boolean needPunct (String word, int punct){		
		if (punct >= 33 && punct <= 64 && punct != 60 && punct != 62) //ascii value of punctuation characters, non <.
			return true;
		else
			return false;
	}
	
	String toPig(String word){ //returns word in piglatten 
		if(word.length()==0)
			return "";
		
		String result= "NAH";
		char firstV =' ';
		int firstVLoc = getVowel(word);
		if(firstVLoc != 1000)
			firstV = word.charAt(firstVLoc);
		
		int punct = (int)word.charAt(word.length()-1);;
		
		boolean needsCap = needCap(word);
		boolean needsPunc = needPunct(word, punct);
		
		
		if(firstVLoc==0 && firstV != 'Y' && firstV != 'y' ){	//takes care of first letter vowel situation

			if(needsPunc==true){
				word = word.substring(0, word.length()-1);
				result = word + "way" + (char)punct;
				return result; 
			}
			else	
				result = word + "way";
			
			if(needsCap==true)
				capFormat(result);
				
			return result;
		}
		
		else if(word.charAt(0) == '&'){	//takes care of html inline symbols
			for (int i = 0; i<word.length(); i++){
				if (word.charAt(i)==';')
					return (word.substring(0, (i + 1)) + toPig(word.substring(i+1)));
			}
		}
		
		else if(firstVLoc == 1000) //takes care of abreviations w/ no vowels
			return word;

		else if (firstVLoc==0 && (firstV=='Y' | firstV=='y')){ //takes care of word starting with y
			boolean cap1=false;
				if (firstV == 'Y')
					cap1 = true;
				else
					cap1 = false;
				firstVLoc = getVowel(word.substring(1));	//get next vowel location and value
				firstV= word.substring(1).charAt(firstVLoc);
				word =word.substring(1);
				if(needsPunc == true)
					result = word.substring(firstVLoc, (word.length()-1)) + 'y' + word.substring(0, firstVLoc) + "ay" +(char)punct;
				else
					result = word.substring(firstVLoc, (word.length()-1)) + 'y' + word.substring(0, firstVLoc) + "ay" ;
				
				if(cap1 == true)
					result = capFormat(result);
				
				return result;

			}
		
		else{ //takes care of word starting with constanent  thats not y

			if(needsPunc == true)
				result = word.substring(firstVLoc, (word.length()-1)) + word.substring(0, firstVLoc) + "ay" +(char)punct;
			else
				result = word.substring(firstVLoc, (word.length()-1)) + word.substring(0, firstVLoc) + "ay" ;
			
			if(needsCap == true)
				result = capFormat(result);
			
			return result;
		}
		return result;
		
	
	}
	
	
	public static void main(String[] args) throws IOException {
		inputFile = args[0];
		outputFile = args[1];
		
		String s;
		
		WebToPigLatin core = new WebToPigLatin(inputFile, outputFile);
		
		while (core.input.hasNext()){
			s = core.nextLine();
			System.out.println(s);
			core.output.append(s + '\n');
		}
		core.output.close();
	}

}
