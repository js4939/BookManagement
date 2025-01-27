package book.controller;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import book.dto.BookDto;
import book.dto.BookImageDto;
import book.service.BookService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BookController {
	@Autowired
	private BookService bookService;
	
	@GetMapping("/book/openBookList.do")
	public ModelAndView openBookList() throws Exception {
		ModelAndView mv = new ModelAndView("/book/bookList");
		
		List<BookDto> list = bookService.selectBookList();
		mv.addObject("list", list);
		
		return mv;
	}
	
	@GetMapping("/book/openBookWrite.do")
	public String openBookWrite() throws Exception {
		return "/book/bookWrite";
	}
	
    // 저장 요청을 처리하는 메서드
    @PostMapping("/book/insertBook.do")
    public String insertBook(BookDto bookDto, MultipartHttpServletRequest request) throws Exception {
        bookService.insertBook(bookDto, request);
        return "redirect:/book/openBookList.do";
    }
    
	// 상세 조회 요청을 처리하는 메서드
	// /book/openBookDetail.do?bookId=1234
	@GetMapping("/book/openBookDetail.do")
	public ModelAndView openBookDetail(@RequestParam("bookId") int bookId) throws Exception {
		// @RequestParam int bookId으로 주소값에 포함된 id 값 가져옴
		// 해당 bookId에 일치하는 도서 정보 전달받도록 요청
		BookDto bookDto = bookService.selectBookDetail(bookId);
		
		ModelAndView mv = new ModelAndView("/book/BookDetail");
		mv.addObject("book", bookDto);
		return mv;
	}
	
	// 수정 요청을 처리할 메서드
	@PostMapping("/book/updateBook.do")
	public String updateBook(BookDto bookDto) throws Exception{
		bookService.updateBook(bookDto);
		
		// 정상 업데이트 시, 목록으로 이동
		return "redirect:/book/openBookList.do";
	}
	
	// 삭제 요청을 처리할 메서드
	@PostMapping("/book/deleteBook.do")
	public String deleteBook(@RequestParam("bookId") int bookId) throws Exception {
		bookService.deleteBook(bookId);
		
		// 정상 삭제 시, 목록으로 이동
		return "redirect:/book/openBookList.do";
	}
	
	// 파일 다운로드 요청을 처리하는 메서드 
    @GetMapping("/book/downloadBookImage.do")
    public void downloadBookImage(@RequestParam("imageId") int imageId, @RequestParam("bookId") int bookId, HttpServletResponse response) throws Exception {
        // imageId와 boardIdx가 일치하는 파일 정보를 조회
        BookImageDto bookImageDto = bookService.selectBookImageInfo(imageId, bookId);
        if (ObjectUtils.isEmpty(bookImageDto)) {
            return;
        }
        
        // 원본 파일 저장 위치에서 파일을 읽어서 호출(요청)한 곳으로 파일을 응답으로 전달
        Path path = Paths.get(bookImageDto.getStoredFilePath());
        byte[] file = Files.readAllBytes(path);
        
        response.setContentType("application/octet-stream");	
        response.setContentLength(file.length);		
        response.setHeader("Content-Disposition", 		
"attachment; fileName=\"" + URLEncoder.encode(bookImageDto.getOriginalFileName(), "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");	
        response.getOutputStream().write(file);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
