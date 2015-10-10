package edu.unc.cs.hdwhite.readroom;

/**
 * Created by hdwhite on 10/10/2015.
 */
public class Book {
    private String title;
    private String author;

    public Book (String _title, String _author){
        title = _title;
        author = _author;
    }

    @Override
    public String toString(){
        return title + " by " + author;
    }
}
