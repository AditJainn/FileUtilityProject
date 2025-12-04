package com.example.fileconverter;import java.util.Arrays;
import java.util.List;
import java.util.Optional;
public enum ImageType {
    JPEG("image/jpeg", ".jpeg", ".jpg"),
    PNG("image/png", ".png");

    private final String mimeType;
    private final List<String> extensions;

    ImageType(String mimeType, String... extensions) {
        this.mimeType = mimeType;
        this.extensions = Arrays.asList(extensions);
    }

    public String getMimeType() {
        return mimeType;
    }
    public List<String> getExtensions() {
        return extensions;
    }

    public static Optional<ImageType> fromMime(String mime) {
        return Arrays.stream(values()) // Goes through list of all enums 
                .filter(t -> t.mimeType.equalsIgnoreCase(mime))
                .findFirst();
    }

    // Helper: find enum by file extension
    public static Optional<ImageType> fromExtension(String filename) {
        return Arrays.stream(values())
                .filter(t -> t.extensions
                        .stream()
                        .anyMatch(filename.toLowerCase()::endsWith))
                .findFirst();
    }
    
}