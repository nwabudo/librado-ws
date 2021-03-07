package io.neoOkpara.librado.ws.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="books_tbl")
@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Book extends AuditModel {

	private static final long serialVersionUID = -5038454088605176461L;

	@Column(nullable = false, length = 100)
	@EqualsAndHashCode.Include
	private String bookTitle;
	
	@Column(nullable = false, length = 50)
	private String authorName;
	
	@Lob
	@Column(nullable = false, length = 250, columnDefinition = "TEXT")
	private String bookDesc;
	
	@EqualsAndHashCode.Include
	@Column(nullable = false, unique = true)
	private String bookISBNCode;
	
	//private int bookCount;
	
	private boolean isavailable;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User currentHandler;
}
