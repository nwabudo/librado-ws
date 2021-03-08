package io.neoOkpara.librado.ws.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.neoOkpara.librado.ws.dtos.BookDTO;
import io.neoOkpara.librado.ws.dtos.ErrorMessages;
import io.neoOkpara.librado.ws.entity.Book;
import io.neoOkpara.librado.ws.entity.User;
import io.neoOkpara.librado.ws.exceptions.RecordNotFoundException;
import io.neoOkpara.librado.ws.exceptions.ServiceException;
import io.neoOkpara.librado.ws.respository.BookRepository;
import io.neoOkpara.librado.ws.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	BookRepository bookrepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	ModelMapper mapper;

	@Override
	public Book fetchBookByISBNNo(String bookCode) {
		Book book = bookrepo.findByBookISBNCode(bookCode)
				.orElseThrow(() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
		// BeanUtils.copyProperties(book, bookDto);
		// bookDto.add(linkTo(methodOn(LibraryController.class).getByBookId(bookCode)).withSelfRel());
		return book;
	}

	@Override
	public boolean addNewBook(BookDTO bookdto) {
		Book book = null;
		try {
			Optional<Book> bookOp = bookrepo.findByBookISBNCode(bookdto.getBookISBNCode());
			if (bookOp.isPresent())
				return true;
			book = mapper.map(bookdto, Book.class);
			return bookrepo.save(book) != null;
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
	}

	@Override
	public boolean modifyBook(BookDTO bookdto) {
		try {
			Book book = bookrepo.findByBookISBNCode(bookdto.getBookISBNCode())
					.orElseThrow(() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

			book.setAuthorName(bookdto.getAuthorName());
			book.setBookTitle(bookdto.getBookTitle());
			book.setBookDesc(bookdto.getBookDesc());
			return bookrepo.save(book) != null;
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
	}

	@Override
	public boolean deleteBook(String bookCode) {
		Book book = bookrepo.findByBookISBNCode(bookCode)
				.orElseThrow(() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
		try {
			bookrepo.delete(book);
			return true;
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
	}

	@Override
	public Page<Book> getAllBooks(int page, int size) {
		Pageable pageableRequest = PageRequest.of(page, size);
		Page<Book> prodPage;
		try {
			prodPage = bookrepo.findAll(pageableRequest);
			if (prodPage == null) {
				prodPage = Page.empty(pageableRequest);
			}
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
		return prodPage;
	}

	@Override
	public Page<Book> searchBooksByTitle(String title, int page, int size) {
		Pageable pageableRequest = PageRequest.of(page, size);
		Page<Book> prodPage;
		try {
			prodPage = bookrepo.findByBookTitleIgnoreCaseContaining(title, pageableRequest);
			if (prodPage == null) {
				prodPage = Page.empty(pageableRequest);
			}
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
		return prodPage;
	}

	@Override
	public Page<Book> searchBooksByTitleOrAuthorName(String title, String authorName, int page, int size) {
		Pageable pageableRequest = PageRequest.of(page, size);
		Page<Book> prodPage;
		try {
			prodPage = bookrepo.findByAuthorNameIgnoreCaseContainingOrBookTitleIgnoreCaseContaining(authorName, title,
					pageableRequest);
			if (prodPage == null) {
				prodPage = Page.empty(pageableRequest);
			}
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
		return prodPage;
	}

	@Override
	public Page<Book> searchBooksByAuthorName(String authorName, int page, int size) {
		Pageable pageableRequest = PageRequest.of(page, size);
		Page<Book> prodPage;
		try {
			prodPage = bookrepo.findByAuthorNameIgnoreCaseContaining(authorName, pageableRequest);
			if (prodPage == null) {
				prodPage = Page.empty(pageableRequest);
			}
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
		return prodPage;
	}

	@Override
	public boolean lendBook(String userId, String bookCode) {
		try {
			User user = userRepo.findByUserId(userId).orElseThrow(
					() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(userId)));
			Book book = bookrepo.findByBookISBNCode(bookCode).orElseThrow(
					() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(bookCode)));
			if (!book.isAvailable())
				return false;
			if (book.getCurrentHandler() == null) {
				book.setCurrentHandler(user);
				book.setAvailable(false);
				bookrepo.save(book);
				return true;
			}
			return false;
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
	}

	@Override
	public boolean returnBook(String userId, String bookCode) {
		try {
			User user = userRepo.findByUserId(userId).orElseThrow(
					() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(userId)));
			Book book = bookrepo.findByBookISBNCode(bookCode).orElseThrow(
					() -> new RecordNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(bookCode)));
			if (book.isAvailable())
				return true;
			if (book.getCurrentHandler() == null)
				return true;
			if (book.getCurrentHandler().equals(user)) {
				book.setCurrentHandler(null);
				book.setAvailable(true);
				bookrepo.save(book);
				return true;
			}
			return false;
		} catch (Exception ex) {
			String message = ErrorMessages.INTERNAL_SERVER_ERROR.getErrorMessage() + ": " + ex.getMessage();
			log.error("Error Occured {}. Caused by {}", message, ex.getCause());
			throw new ServiceException(message);
		}
	}

}
