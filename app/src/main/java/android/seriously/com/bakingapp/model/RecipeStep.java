package android.seriously.com.bakingapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class RecipeStep implements Serializable {

    private final int id;
    private final String shortDesc;
    private final String fullDesc;
    private final String videoUrl;

    public RecipeStep(int id, @NonNull String shortDesc, @NonNull String fullDesc,
                      String videoUrl) {
        this.id = id;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.videoUrl = videoUrl;
    }

    public int getId() {
        return id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeStep that = (RecipeStep) o;

        if (id != that.id) return false;
        if (!shortDesc.equals(that.shortDesc)) return false;
        if (!fullDesc.equals(that.fullDesc)) return false;
        return videoUrl != null ? videoUrl.equals(that.videoUrl) : that.videoUrl == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + shortDesc.hashCode();
        result = 31 * result + fullDesc.hashCode();
        result = 31 * result + (videoUrl != null ? videoUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
                "id=" + id +
                ", shortDesc='" + shortDesc + '\'' +
                ", fullDesc='" + fullDesc + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
