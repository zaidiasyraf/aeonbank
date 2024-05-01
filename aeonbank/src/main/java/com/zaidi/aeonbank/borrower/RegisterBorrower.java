package com.zaidi.aeonbank.borrower;

import jakarta.validation.constraints.NotBlank;

public record RegisterBorrower(@NotBlank(message = "Name cannot be blank") String name, @NotBlank(message = "Email cannot be blank") String email) {

    public Borrower borrower() {
        return new Borrower(this.name, this.email);
    }

}
