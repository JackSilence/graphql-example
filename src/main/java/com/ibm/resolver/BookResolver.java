package com.ibm.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.ibm.model.Author;
import com.ibm.model.Book;
import com.ibm.repository.AuthorRepository;

public class BookResolver implements GraphQLResolver<Book> {
	private AuthorRepository authorRepository;

	public BookResolver( AuthorRepository authorRepository ) {
		this.authorRepository = authorRepository;
	}

	public Author getAuthor( Book book ) {
		return authorRepository.findOne( book.getAuthor().getId() );
	}
}