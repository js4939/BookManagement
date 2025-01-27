package book.service;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import book.dto.BookDto;
import book.dto.BookImageDto;

public interface BookService {
	List<BookDto> selectBookList();
	void insertBook(BookDto bookDto, MultipartHttpServletRequest request);
	BookDto selectBookDetail(int bookId);
	void updateBook(BookDto bookDto);
	void deleteBook(int bookId);
	BookImageDto selectBookImageInfo(int imageId, int bookId);
}
