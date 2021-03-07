package io.neoOkpara.librado.ws.dtos;

import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "productResponse", itemRelation = "item")
@JsonInclude(Include.NON_NULL)
public class BookDTO extends RepresentationModel<BookDTO>  {
	
	@NotBlank(message = "Book Title is mandatory")
	private String bookTitle;
	
	@NotBlank(message = "Author Name is mandatory")
	private String authorName;
	
	@NotBlank(message = "Book Description is mandatory")
	private String bookDesc;
	
	@Builder.Default
	private boolean isavailable = true;
	
	@NotBlank(message = "Book ISBN No is mandatory")
	private String bookISBNCode;
	
	private String handlerId;
}
