package io.neoOkpara.librado.ws.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

	@EqualsAndHashCode.Include
	private String bookTitle;
	
	private String authorName;
	
	@EqualsAndHashCode.Include
	@Column(nullable = false, unique = true)
	private String bookISBNCode;
}
