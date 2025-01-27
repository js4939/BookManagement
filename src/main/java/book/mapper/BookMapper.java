package book.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import book.dto.BookDto;
import book.dto.BookImageDto;

@Mapper
public interface BookMapper {
	List<BookDto> selectBookList();
	void insertBook(BookDto bookDto);
	BookDto selectBookDetail(int bookId);
	void updateBook(BookDto bookDto);
	void deleteBook(BookDto bookDto);
	void insertBookImageList(List<BookImageDto> imageInfoList);
	List<BookImageDto> selectBookImageList(int bookId);
	BookImageDto selectBookImageInfo(@Param("imageId") int imageId, @Param("bookId") int bookId);
}
