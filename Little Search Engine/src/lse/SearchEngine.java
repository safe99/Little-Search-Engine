package lse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchEngine {
	static Scanner sc1, sc2;
	
	public static void main(String[] args) throws IOException {
		sc1 = new Scanner(System.in);
		System.out.print("Enter word:");
		String word = sc1.nextLine();
		LittleSearchEngine lse = new LittleSearchEngine();
		lse.makeIndex("docs.txt", "noisewords.txt");
		//System.out.println(lse.getKeyword(word));
		
		System.out.println(lse.keywordsIndex.toString());
		for(String s: lse.keywordsIndex.keySet()) {
			if(lse.keywordsIndex.get(s).size()>1) {
				//System.out.print(s+": ");
				//System.out.println(lse.keywordsIndex.get(s));
			}
		}
		System.out.println();
		

		ArrayList<Occurrence> occ = new ArrayList<Occurrence>();
		occ.add(new Occurrence("a",12));
		occ.add(new Occurrence("b",8));
		occ.add(new Occurrence("a",7));
		occ.add(new Occurrence("a",6));
		occ.add(new Occurrence("a",5));
		occ.add(new Occurrence("a",4));
		occ.add(new Occurrence("a",3));
		occ.add(new Occurrence("a",2));
		occ.add(new Occurrence("a",9));
		System.out.println(lse.insertLastOccurrence(occ));
		System.out.println();
		
		System.out.println(lse.keywordsIndex.get("small"));
		System.out.println(lse.keywordsIndex.get("own"));
		
		System.out.println("Flinto!");
		
		ArrayList<String> ayy = lse.top5search("small", "own");
		System.out.println(ayy.toString());
		
		while(!word.equals("quit")) {
			sc1 = new Scanner(System.in);
			System.out.print("Enter word1: ");
			word = sc1.nextLine();
			System.out.print("Enter word2: ");
			String word2 = sc1.nextLine();
			ArrayList<String> ayye = lse.top5search(word, word2);
			if(ayye!=null)
			System.out.println(ayye.toString());
			else
				System.out.println("null");
		}
		
	}
}
