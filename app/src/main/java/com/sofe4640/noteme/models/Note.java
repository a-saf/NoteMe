package com.sofe4640.noteme.models;

public class Note {

    public String title;
    public String subtitle;
    public String body;
    public String noteColor;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String date;

    public Note(String title, String subtitle, String body, String noteColor, String date) {
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.noteColor = noteColor;
        this.date = date;
    }

    public Note() { }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", body='" + body+ '\'' +
                ", noteColor='" + noteColor + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }
}
