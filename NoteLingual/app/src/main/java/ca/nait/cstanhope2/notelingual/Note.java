package ca.nait.cstanhope2.notelingual;

import java.util.Date;

public class Note {

    private int noteID;
    private String title;
    private String dateCreated;
    private String dateLastEdited;
    private String content;

    public Note(int noteID, String title, String dateCreated, String dateLastEdited, String content) {
        this.noteID = noteID;
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateLastEdited = dateLastEdited;
        this.content = content;
    }

    public Note(int noteID, String title, String content) {
        this.noteID = noteID;
        this.title = title;
        this.content = content;

        Date today = new Date();
        today.getDate();
        this.dateCreated = today.toString();
        this.dateLastEdited = today.toString(); // set edit date to today as well
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateLastEdited(String date) {
        this.dateLastEdited = date;
    }

    public String getDateLastEdited() {
        return dateLastEdited;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}