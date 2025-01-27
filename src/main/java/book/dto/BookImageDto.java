package book.dto;

import lombok.Data;

@Data
public class BookImageDto {
    private int imageId;
    private int bookId;
    private String originalFileName;
    private String storedFilePath;
    private String fileSize;
}
