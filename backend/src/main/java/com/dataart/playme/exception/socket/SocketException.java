package com.dataart.playme.exception.socket;

public class SocketException extends RuntimeException {

    String rehearsalId;

    String musicianId;

    String name;

    public SocketException(String rehearsalId, String musicianId, String name, String message) {
        super(message);
        this.name = name;
        this.rehearsalId = rehearsalId;
        this.musicianId = musicianId;
    }

    public String getRehearsalId() {
        return rehearsalId;
    }

    public String getMusicianId() {
        return musicianId;
    }

    public String getName() {
        return name;
    }
}
