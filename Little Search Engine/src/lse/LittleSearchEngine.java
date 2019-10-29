package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		HashMap<String, Occurrence> hashie = new HashMap<String,Occurrence>(1000,2.0f);
	
		Scanner sc = new Scanner(new File(docFile));
		String[]words;
		String line;
		
		while (sc.hasNext()) {
			line = sc.next();
			words = line.split(" ");
			for(String word: words) {
				if(getKeyword(word)!=null) {
					word = getKeyword(word);
					Occurrence value;
					if(!hashie.containsKey(word)) {
						value = new Occurrence(docFile, 1);
					}
					else {
						value = hashie.get(word);
						value = new Occurrence(value.document,value.frequency+1);
					}
					hashie.put(word, value);
				}
			}
		}
		sc.close();
		return hashie;	
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(String key: kws.keySet()) {
			ArrayList<Occurrence> value = new ArrayList<Occurrence>();
			if(!keywordsIndex.containsKey(key)) {
				value.add(new Occurrence(kws.get(key).document,kws.get(key).frequency));
			}
			else {
				value = keywordsIndex.get(key);
				value.add(new Occurrence(kws.get(key).document,kws.get(key).frequency));
				insertLastOccurrence(value);
			}	
			keywordsIndex.put(key, value);
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		word = word.toLowerCase();
		boolean strip = true;
		while(strip) {
			Character x = word.charAt(word.length()-1);
			switch(x){
				case '.': case ',': case '?': case '!': case ';': case ':': word = word.substring(0,word.length()-1);
					break;
				default: strip = false;
			}
			if(word.length()==0) {
				strip = false;
			}
		}	
		boolean isKeyWord = true;
		for(int i= 0; i<word.length(); i++) {
			if(!Character.isAlphabetic(word.charAt(i))) {
				isKeyWord = false;
				break;
			}
		}
		if(noiseWords.contains(word)) {isKeyWord = false;}
		if(word.length()==0) {isKeyWord = false;}
		if(isKeyWord) {return word;}
		return null;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> inting = new ArrayList<Integer>();
		ArrayList<Integer> sequence = new ArrayList<Integer>();
		for(Occurrence o: occs) {
			inting.add(o.frequency);
		}
		if(inting.size()==1) {return null;}
		else {
			//Binary Search
			int target = inting.get(inting.size()-1);
		    int left = 0;
		    int right = inting.size() - 2;
		    int mid=0;

		    while(left <= right){
		        mid = (left + right)/2;
		        sequence.add(mid);
		        if(inting.get(mid) == target){break;}
		        else if(inting.get(mid) > target){left = mid + 1;}
		        else{right = mid - 1;}
		     }
		    if(inting.get(mid)>target) {mid++;}
		    Occurrence replace = occs.get(occs.size()-1);
		    Occurrence temp;
		    for(int i=mid; i<occs.size(); i++) {
		    	temp = occs.get(i);
		    	occs.set(i, replace);
		    	replace = temp;
		    }
		}
		return sequence;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Occurrence> r = new ArrayList<Occurrence>();
		ArrayList<Occurrence> r2 = new ArrayList<Occurrence>();
		boolean dupe = false;
		int dupeId = 0;
		
		r = keywordsIndex.get(kw1);
		r2 = keywordsIndex.get(kw2);
		if(r==null && r2==null) {
			return null;
		}
		if(r==null) {
			for(Occurrence o: r2) {
				result.add(o.document);
				if(result.size()==5) {
					return result;
				}
			}
			return result;
		}
		if(r2==null) {
			for(Occurrence o: r) {
				result.add(o.document);
				if(result.size()==5) {
					return result;
				}
			}
			return result;
		}
		for(int i=0; i<r2.size(); i++) {
			for(int j=0; j<r.size(); j++) {
				if(r2.get(i).document.equals(r.get(j).document)) {
					dupe = true;
					dupeId = j;
				}
			}
			if(dupe) {
				int r2Freq = r2.get(i).frequency;
				int rFreq = r.get(dupeId).frequency;
				if(r2Freq>rFreq) {
					r.remove(dupeId);
					r.add(r2.get(i));
					insertLastOccurrence(r);
				}
			}
			else {
				r.add(r2.get(i));
				insertLastOccurrence(r);	
			}
		}
		
		for(Occurrence o: r) {
			result.add(o.document);
			if(result.size()==5) {
				return result;
			}
		}
		return result;
	
	}
}
