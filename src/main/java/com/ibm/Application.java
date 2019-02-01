package com.ibm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ibm.exception.GraphQLErrorAdapter;
import com.ibm.model.Author;
import com.ibm.model.Book;
import com.ibm.repository.AuthorRepository;
import com.ibm.repository.BookRepository;
import com.ibm.resolver.BookResolver;
import com.ibm.resolver.Mutation;
import com.ibm.resolver.Query;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GraphQLErrorHandler;

@SpringBootApplication
public class Application {
	// https://www.pluralsight.com/guides/building-a-graphql-server-with-spring-boot
	public static void main( String[] args ) {
		SpringApplication.run( Application.class, args );
	}

	@Bean
	public GraphQLErrorHandler errorHandler() {
		return new GraphQLErrorHandler() {
			@Override
			public List<GraphQLError> processErrors( List<GraphQLError> errors ) {
				List<GraphQLError> clientErrors = errors.stream().filter( this::isClientError ).collect( Collectors.toList() );

				List<GraphQLError> serverErrors = errors.stream().filter( e -> !isClientError( e ) ).map( GraphQLErrorAdapter::new ).collect( Collectors.toList() );

				List<GraphQLError> e = new ArrayList<>();
				e.addAll( clientErrors );
				e.addAll( serverErrors );
				return e;
			}

			protected boolean isClientError( GraphQLError error ) {
				return !( error instanceof ExceptionWhileDataFetching || error instanceof Throwable );
			}
		};
	}

	@Bean
	public BookResolver authorResolver( AuthorRepository authorRepository ) {
		return new BookResolver( authorRepository );
	}

	@Bean
	public Query query( AuthorRepository authorRepository, BookRepository bookRepository ) {
		return new Query( authorRepository, bookRepository );
	}

	@Bean
	public Mutation mutation( AuthorRepository authorRepository, BookRepository bookRepository ) {
		return new Mutation( authorRepository, bookRepository );
	}

	@Bean
	public CommandLineRunner demo( AuthorRepository authorRepository, BookRepository bookRepository ) {
		return ( args ) -> {
			Author author = new Author( "Herbert", "Schildt" );

			authorRepository.save( author );

			bookRepository.save( new Book( "Java: A Beginner's Guide, Sixth Edition", "0071809252", 728, author ) );
		};
	}
}