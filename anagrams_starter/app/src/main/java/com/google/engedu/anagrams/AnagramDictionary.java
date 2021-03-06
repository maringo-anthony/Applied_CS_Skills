/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.util.Log;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private final Random random = new Random();
    private final List<String> wordList;
    private final Set<String> wordSet;
    private final Map<String, ArrayList<String>> lettersToWord;
    private Map<Integer, ArrayList<String>> sizeToWords;


    public AnagramDictionary(Reader reader) throws IOException {
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();

        wordList = new ArrayList<>();

        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                ArrayList<String> valueList = lettersToWord.get(sortedWord);
                valueList.add(word);
            } else {
                ArrayList<String> valueList = new ArrayList<>();
                valueList.add(word);
                lettersToWord.put(sortedWord, valueList);
            }
            if (sizeToWords.containsKey(word.length())) {
                ArrayList<String> valueList = sizeToWords.get(word.length());
                valueList.add(word);
                sizeToWords.put(word.length(), valueList);
            } else {
                ArrayList<String> valueList = new ArrayList<>();
                valueList.add(word);
                sizeToWords.put(word.length(), valueList);
            }


            wordSet.add(word);
            wordList.add(word);

        }
    }

    public boolean isGoodWord(String word, String base) {
        // Check that the word is valid dictionary word
        if (!wordSet.contains(word)) {
            Log.d("isGoodWord", "word not in set");
            return false;
        }
        // Check that the word does not contain the substring
        else if (word.contains(base)) {
            Log.d("isGoodWord", "word contains base");
            return false;
        } else return true;
    }

    private List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        int targetLength = targetWord.length();
        for (String word : wordList) {
            if (targetLength == sortLetters(word).length()) {
                result.add(word);
            }
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result;
        String sortedWord = sortLetters(word);
        // Get the list of words that go along with it in its current state
        result = lettersToWord.get(sortedWord);
        if (result == null) {
            result = new ArrayList<>();
        }

        List<String> anagrams;
        for (char curChar = 'a'; curChar <= 'z'; curChar++) {
            String extendedWord = word + curChar;
            anagrams = getAnagrams(extendedWord);
            result.addAll(anagrams);
        }


        return result;
    }

    public String pickGoodStarterWord() {
        // Index needs to be bounded to the size of the wordList
        int index = random.nextInt(wordList.size());
        String starterWord;
        ArrayList<String> anagramList;

        while(true){
            ArrayList<String> curWordList = sizeToWords.get(wordLength);

            // Try to find a word within the given length bounds
            if(curWordList != null){
                for(String word: curWordList){
                    anagramList = (ArrayList<String>) getAnagramsWithOneMoreLetter(word);
                    if(anagramList.size() >= MIN_NUM_ANAGRAMS){
                        return word;
                    }
                }
            }
            else if(wordLength <= MAX_WORD_LENGTH){
                wordLength++;
            }
            // If there are no valid words in the given length bounds, find a random one as before
            else{
                starterWord = wordList.get(index);
                anagramList = (ArrayList<String>) getAnagramsWithOneMoreLetter(starterWord);
                if (anagramList.size() >= MIN_NUM_ANAGRAMS) {
                    return starterWord;
                }

                // Ensure that it will wrap around to the start of the list
                index = (index + 1) % wordList.size();

            }
        }

    }

    private String sortLetters(String input) {
        char[] chars = input.toCharArray();
        //Log.d("sortLetters", "Before input: " + Arrays.toString(chars));
        Arrays.sort(chars);
        //Log.d("sortLetters", "After input: " + Arrays.toString(chars));

        return input;
    }
}
