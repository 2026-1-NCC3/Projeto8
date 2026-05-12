package com.example.projeto8.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

    private String name;
    public boolean isExpanded = false;

    private String title;
    private int serie;
    private int reps;
    private Long exercise_id;
    private String midiaURL;
    private String description;
    private String tags;

    public Task(Long exercise_id, String title, int serie, int reps, String midiaURL, String description) {
        this.exercise_id = exercise_id;
        this.title = title;
        this.serie = serie;
        this.reps = reps;
        this.midiaURL = midiaURL;
        this.description = description;
        this.tags = "";
        this.isExpanded = false;

    }

    protected Task(Parcel in) {
        name = in.readString();
        isExpanded = in.readByte() != 0;
        title = in.readString();
        serie = in.readInt();
        reps = in.readInt();
        if (in.readByte() == 0) {
            exercise_id = null;
        } else {
            exercise_id = in.readLong();
        }
        midiaURL = in.readString();
        description = in.readString();
        tags = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isExpanded ? 1 : 0));
        dest.writeString(title);
        dest.writeInt(serie);
        dest.writeInt(reps);
        if (exercise_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(exercise_id);
        }
        dest.writeString(midiaURL);
        dest.writeString(description);
        dest.writeString(tags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public Long getExerciseId() {
        return exercise_id;
    }

    public String getTitle() {
        return title;
    }

    public int getReps() {
        return reps;
    }

    public int getSerie() {
        return serie;
    }

    public String getMidiaURL() {
        return midiaURL;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    // Teste com dados mockados
    public Task(String name) {
        this.name = name;
    }

    // Getter para o Adapter conseguir ler o nome
    public String getName() {
        return name;
    }
}
