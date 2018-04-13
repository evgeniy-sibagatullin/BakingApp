package android.seriously.com.bakingapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class RecipeStep implements Serializable {

    private final int id;
    private final String shortDescription;
    private final String description;
    private final String videoURL;
    private final String thumbnailURL;

    public RecipeStep(int id, @NonNull String shortDescription, @NonNull String description,
                      String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeStep that = (RecipeStep) o;

        if (id != that.id) return false;
        if (!shortDescription.equals(that.shortDescription)) return false;
        if (!description.equals(that.description)) return false;
        if (videoURL != null ? !videoURL.equals(that.videoURL) : that.videoURL != null) return false;
        return thumbnailURL != null ? thumbnailURL.equals(that.thumbnailURL) : that.thumbnailURL == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + shortDescription.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (videoURL != null ? videoURL.hashCode() : 0);
        result = 31 * result + (thumbnailURL != null ? thumbnailURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }
}
