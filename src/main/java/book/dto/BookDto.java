package book.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookDto {
	private int bookId;				// 고유 식별자
	private String title;			// 도서 제목
	private String author;			// 저자
	private String publisher;		// 출판사	
	private String publishedDate;	// 출판일
	private String isbn;			// ISBN 번호
	private String description;		// 도서 설명
	private String createdAt;		// 생성 시각
	private String updatedAt;		// 수정 시각
	
	// 첨부 파일 정보를 저장할 필드를 추가
    private List<BookImageDto> imageInfoList;
}
