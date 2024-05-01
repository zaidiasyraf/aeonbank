package com.zaidi.aeonbank.book;

import jakarta.validation.constraints.NotBlank;

public record BookRegister(@NotBlank(message = "isbn cannot be blank") String isbn, @NotBlank(message = "title cannot be blank") String title, @NotBlank(message = "author cannot be blank") String author) {

    public Book asBook() {
        return new Book(this.isbn, this.title, this.author);
    }

}
