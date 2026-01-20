package com.Bookstore.service;

import com.Bookstore.dto.BookAutofillDto;
import com.Bookstore.dto.GoogleBookResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalBookService {

    private final RestClient restClient;

    public ExternalBookService(RestClient restClient) {
        this.restClient = restClient;
    }

    public BookAutofillDto fetchBookByIsbn(String isbn) {
        GoogleBookResponse response = restClient.get()
                .uri("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn)
                .retrieve()
                .body(GoogleBookResponse.class);

        if (response != null && response.items() != null && !response.items().isEmpty()) {
            var info = response.items().get(0).volumeInfo();

            String rawDescription = info.description() != null ? info.description() : "No description available.";


            String shortDescription = rawDescription.replaceAll("<[^>]*>", "");
            if (shortDescription.length() > 200) {
                shortDescription = shortDescription.substring(0, 197) + "...";
            }

            return new BookAutofillDto(
                    info.title(),
                    info.authors() != null ? String.join(", ", info.authors()) : "Unknown",
                    isbn,
                    info.imageLinks() != null ? info.imageLinks().thumbnail() : "",
                    shortDescription
            );
        }
        throw new RuntimeException("Book not found for ISBN: " + isbn);
    }
}