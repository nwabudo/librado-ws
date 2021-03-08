package io.neoOkpara.librado.ws.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.neoOkpara.librado.ws.dtos.ApiResponse;
import io.neoOkpara.librado.ws.dtos.BookDTO;
import io.neoOkpara.librado.ws.dtos.ErrorMessages;
import io.neoOkpara.librado.ws.dtos.RequestOperationStatus;
import io.neoOkpara.librado.ws.dtos.assembler.BookAssembler;
import io.neoOkpara.librado.ws.entity.Book;
import io.neoOkpara.librado.ws.service.LibraryService;

@RestController
@RequestMapping("api/v1/library")
public class LibraryController {

	private final LibraryService libraryService;

	@Autowired
	PagedResourcesAssembler<Book> pagedResourcesAssembler;

	@Autowired
	BookAssembler bookAssembler;

	public LibraryController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = { "application/hal+json" })
	public ResponseEntity<CollectionModel<BookDTO>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		Page<Book> books = this.libraryService.getAllBooks(page, size);
		PagedModel<BookDTO> bookModel = pagedResourcesAssembler.toModel(books, bookAssembler);
		return new ResponseEntity<>(bookModel, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<ApiResponse> saveNewBook(@Valid @RequestBody BookDTO book) {
		boolean resp = this.libraryService.addNewBook(book);
		String status = resp ? RequestOperationStatus.SUCCESS.name()
				: ErrorMessages.COULD_NOT_CREATE_RECORD.getErrorMessage();
		int code = resp ? 200 : 400;
		return new ResponseEntity<>(new ApiResponse(code, status), HttpStatus.OK);
	}

	@PutMapping("")
	public ResponseEntity<ApiResponse> modifyExistingBook(@Valid @RequestBody BookDTO book) {
		boolean resp = this.libraryService.modifyBook(book);
		String status = resp ? RequestOperationStatus.SUCCESS.name()
				: ErrorMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage();
		int code = resp ? 200 : 400;
		return new ResponseEntity<>(new ApiResponse(code, status), HttpStatus.OK);
	}

	@DeleteMapping("")
	public ResponseEntity<ApiResponse> deleteBook(@RequestParam("bookISBNNo") String bookISBNNo) {
		boolean resp = this.libraryService.deleteBook(bookISBNNo);
		String status = resp ? RequestOperationStatus.SUCCESS.name()
				: ErrorMessages.COULD_NOT_DELETE_RECORD.getErrorMessage();
		int code = resp ? 200 : 400;
		return new ResponseEntity<>(new ApiResponse(code, status), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/lendBook/{bookISBNNo}", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> lendBook(@PathVariable("userId") String userId,
			@PathVariable("bookISBNNo") String bookISBNNo) {
		boolean resp = this.libraryService.lendBook(userId, bookISBNNo);
		String status = resp ? RequestOperationStatus.SUCCESS.name()
				: ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage();
		int code = resp ? 200 : 400;
		return new ResponseEntity<>(new ApiResponse(code, status), HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}/returnBook/{bookISBNNo}", method = RequestMethod.POST)
	public ResponseEntity<ApiResponse> returnBook(@PathVariable("userId") String userId,
			@PathVariable("bookISBNNo") String bookISBNNo) {
		boolean resp = this.libraryService.returnBook(userId, bookISBNNo);
		String status = resp ? RequestOperationStatus.SUCCESS.name()
				: ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage();
		int code = resp ? 200 : 400;
		return new ResponseEntity<>(new ApiResponse(code, status), HttpStatus.OK);
	}

	@RequestMapping(value = "/search/byISBNNo", method = RequestMethod.GET, produces = { "application/hal+json" })
	public ResponseEntity<BookDTO> getByBookId(@RequestParam("bookISBNNo") String bookISBNNo) {
		Book book = this.libraryService.fetchBookByISBNNo(bookISBNNo);
		BookDTO bookdto = bookAssembler.toModel(book);
		return new ResponseEntity<>(bookdto, HttpStatus.OK);
	}

	@RequestMapping(value = "/search/byAuthorName", method = RequestMethod.GET, produces = { "application/hal+json" })
	public ResponseEntity<PagedModel<BookDTO>> getByAuthorName(@RequestParam("authorName") String authorName,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		Page<Book> book = this.libraryService.searchBooksByAuthorName(authorName, page, size);
		PagedModel<BookDTO> bookModel = pagedResourcesAssembler.toModel(book, bookAssembler);
		return new ResponseEntity<>(bookModel, HttpStatus.OK);
	}

	@RequestMapping(value = "/search/byBookTitle", method = RequestMethod.GET, produces = { "application/hal+json" })
	public ResponseEntity<PagedModel<BookDTO>> getByBookTitle(@RequestParam("bookTitle") String bookTitle,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		Page<Book> book = this.libraryService.searchBooksByTitle(bookTitle, page, size);
		PagedModel<BookDTO> bookModel = pagedResourcesAssembler.toModel(book, bookAssembler);
		return new ResponseEntity<>(bookModel, HttpStatus.OK);
	}

	@RequestMapping(value = "/search/byTitleOrAuthor", method = RequestMethod.GET, produces = {
			"application/hal+json" })
	public ResponseEntity<PagedModel<BookDTO>> getBookByTitleOrAuthor(@RequestParam("bookTitle") String bookTitle,
			@RequestParam("authorName") String authorName, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		Page<Book> book = this.libraryService.searchBooksByTitleOrAuthorName(bookTitle, authorName, page, size);
		PagedModel<BookDTO> bookModel = pagedResourcesAssembler.toModel(book, bookAssembler);
		return new ResponseEntity<>(bookModel, HttpStatus.OK);
	}

}
