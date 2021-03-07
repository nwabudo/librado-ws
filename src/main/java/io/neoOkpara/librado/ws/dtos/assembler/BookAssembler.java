package io.neoOkpara.librado.ws.dtos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import io.neoOkpara.librado.ws.controller.LibraryController;
import io.neoOkpara.librado.ws.dtos.BookDTO;
import io.neoOkpara.librado.ws.entity.Book;

@Component
public class BookAssembler extends RepresentationModelAssemblerSupport<Book, BookDTO>  {

	@Autowired
	ModelMapper mapper;
	
	public BookAssembler() {
		super(LibraryController.class, BookDTO.class);
	}

	@Override
	public BookDTO toModel(Book entity) {
		BookDTO model = instantiateModel(entity);
		model = mapper.map(entity, BookDTO.class);
		model.add(linkTo(methodOn(LibraryController.class).getByBookId(entity.getBookISBNCode())).withSelfRel());
		return model;
	}

}
