package com.sofe4640.noteme.models;

public class Note {

    public String title;
    public String subtitle;
    public String body;
    public String noteColor;
    public String date;
    public String image;

    public Note(String title, String subtitle, String body, String noteColor, String date, String image) {
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.noteColor = noteColor;
        this.date = date;
        this.image = image;
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
                ", image='" + image + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public String getImage(){return image;}

    public void setDate(String date) {
        this.date = date;
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
