package com.exovum.ld37warmup.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.Random;

/**
 * Created by exovu_000 on 12/10/2016.
 */
public class BookComponent implements Component{
    public static final String STATE_THROWN = "THROWN"; //"TREE_NORMAL";
    public static final String STATE_CAUGHT = "CAUGHT"; //"TREE_LIGHTS";
    public static final float WIDTH = 3f;
    public static final float HEIGHT = 4f;


    public enum BookTitle {
        QUIXOTE, MOCKING, WATCH; // TODO: make larger 128x128 images for: GATSBY, IDIOT;

        public String getAssetName() {
            switch (this) {
                case QUIXOTE:
                    return "donq";
                case MOCKING:
                    return "mock";
                case WATCH:
                    return "watch";
                /*
                case GATSBY:
                    return "gatsby";
                case IDIOT:
                    return "idiot";
                    */
                default:
                    return null;
            }
        }

        /**
         * Generate a random quote for the BookTitle
         *
         * @return Text for a random quote from the book
         */
        public String getRandomQuote() {
            // TODO: add quotes
            switch (this) {
                case QUIXOTE:
                    return (this.toString() + " by Miguel de Cervantes");
                case MOCKING:
                    return (this.toString() + " by Harper Lee");
                case WATCH:
                    return (this.toString() + "  by Zora Neale Hurston");
                /*
                case GATSBY:
                    return "'The Great Gatsby' by F. Scott Fitzgerald";
                case IDIOT:
                    return "'The Idiot' by Fyodor Dostoevsky";
                */
                default:
                    return "NO QUOTE";
            }
        }

        public static BookTitle getBookTitleByID(int bookid) {
            return BookTitle.values()[bookid];
        }

        public String toString() {
            switch(this) {
                case QUIXOTE:
                    return "\'Don Quixote\'";
                case MOCKING:
                    return "\'To Kill a Mockingbird\'";
                case WATCH:
                    return "\'Their Eyes Were Watching God\'";
                default:
                    return "No Book Title";
            }
        }
    }

    // Maps BookTitle to an array of quoted Strings
    private static ArrayMap<BookTitle, Array<String>> quotes;

    static {
        quotes = new ArrayMap<>();
        loadQuotes();
    }

    private static void loadQuotes() {
        Array<String> addQuotes = new Array<>();
        // Adding BookTitle.MOCKING quotes
        quotes.put(BookTitle.MOCKING, getQuotesMocking());
        addQuotes.clear();
        // Adding BookTitle.WATCH quotes
        addQuotes.addAll("Tea Cake is so fiiiine.", "I can fix that.");
        quotes.put(BookTitle.WATCH, addQuotes);
        addQuotes.clear();
        // Adding BookTitle.QUIXOTE quotes
        addQuotes.addAll("En tus marcas", "Si se puede!");
        quotes.put(BookTitle.QUIXOTE, addQuotes);
        addQuotes.clear();
    }

    // Generate list of quotes from 'To Kill a Mockingbird'
    private static Array<String> getQuotesMocking() {
        Array<String> addQuotes = new Array<>();
        addQuotes.addAll("Hello", "Goodbye");
        return addQuotes;
    }

    /**
     * Retrieve a random quote text for @title using Random variable
     * @param title Source book for the quote
     * @param random Random variable to choose an index
     * @return A string containing a quote from @title
     */
    public static String getRandomQuote(BookTitle title, Random random) {
        // get a random index from 0 to number of quotes for @title
        if(!quotes.containsKey(title) || quotes.get(title).size == 0) {
            return "No quotes for " + title; // will call toString for BookTitle
        }
        int randomID = random.nextInt(quotes.get(title).size);
        return quotes.get(title).get(randomID);
    }
}
