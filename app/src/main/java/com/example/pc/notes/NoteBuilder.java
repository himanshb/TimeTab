package com.example.pc.notes;



public class NoteBuilder {
    private String title, content,fileName,fileDate;
    private int color;

    public NoteBuilder(String title, String content,String fileDate) {
        this.title = title;
        this.content = content;
        this.fileDate=fileDate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDate() {
        return fileDate;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}