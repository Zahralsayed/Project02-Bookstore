package com.Bookstore.dto;

import java.util.List;

public record GoogleBookResponse(List<Item> items) {
    public record Item(VolumeInfo volumeInfo) {}

    public record VolumeInfo(
            String title,
            List<String> authors,
            String description,
            ImageLinks imageLinks
    ) {}

    public record ImageLinks(String thumbnail) {}
}