package book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import book.common.FileUtils;
import book.dto.BookDto;
import book.dto.BookImageDto;
import book.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j		//로그 찍기 위해 추가
@Service	// Service 구현 클래스를 빈으로 등록
public class BookServiceImpl implements BookService {

	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BookDto> selectBookList() {
		return bookMapper.selectBookList();
	}

	@Override
	public void insertBook(BookDto bookDto, MultipartHttpServletRequest request) {
		// DB에 insert 되지 않도록, 구문을 주석 처리한 후 실행 테스트
        bookMapper.insertBook(bookDto);
        
        try {
            // 첨부 파일을 디스크에 저장하고, 첨부 파일 정보를 반환
            List<BookImageDto> imageInfoList = fileUtils.parseFIleInfo(bookDto.getBookId(), request);
            
            // 첨부 파일 정보를 DB에 저장
            if (!CollectionUtils.isEmpty(imageInfoList)) {
                bookMapper.insertBookImageList(imageInfoList);
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        } 
	}

	@Override
	public BookDto selectBookDetail(int bookId) {
		// 업로드 파일 목록 조회 후 출력
		BookDto bookDto = bookMapper.selectBookDetail(bookId);
		List<BookImageDto> bookFileInfoList = bookMapper.selectBookImageList(bookId);
		bookDto.setImageInfoList(bookFileInfoList);
		
		return bookDto;
	}

	// 도서 정보 수정
	@Override
	public void updateBook(BookDto bookDto) {
		bookMapper.updateBook(bookDto);
	}

	// 도서 삭제
	@Override
	public void deleteBook(int bookId) {
		BookDto bookDto = new BookDto();
		bookDto.setBookId(bookId);
		bookMapper.deleteBook(bookDto);
	}

	@Override
	public BookImageDto selectBookImageInfo(int imageId, int bookId) {
		return bookMapper.selectBookImageInfo(imageId, bookId);
	}
}
