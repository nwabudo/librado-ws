package io.neoOkpara.librado.ws.service;

import org.springframework.data.domain.Page;

import io.neoOkpara.librado.ws.dtos.BookDTO;
import io.neoOkpara.librado.ws.entity.Book;

public interface LibraryService {

	Book fetchBookByISBNNo(String bookCode);
	
	boolean addNewBook(BookDTO book);
	
	boolean modifyBook(BookDTO book);
	
	boolean deleteBook(String bookCode);
	
	Page<Book> searchBooksByTitle(String title, int page, int size);
	
	Page<Book> searchBooksByTitleOrAuthorName(String title, String authorName, int page, int size);
	
	Page<Book> searchBooksByAuthorName(String authorName, int page, int size);

	Page<Book> getAllBooks(int page, int size);
}
