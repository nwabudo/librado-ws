package io.neoOkpara.librado.ws.respository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.neoOkpara.librado.ws.entity.Book;
import io.neoOkpara.librado.ws.entity.User;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	Optional<Book> findByBookISBNCode(String bookISBNCode);
	
	Page<Book> findByAuthorNameIgnoreCaseContaining(String authorName, Pageable pageable);
	
	Page<Book> findByBookTitleIgnoreCaseContaining(String bookTitle, Pageable pageable);
	
	Page<Book> findByAuthorNameIgnoreCaseContainingOrBookTitleIgnoreCaseContaining(String authorName, String bookTitle, Pageable pageable);

	Optional<Book> findByCurrentHandler(User user);
}
