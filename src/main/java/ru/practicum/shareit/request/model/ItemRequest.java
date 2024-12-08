package ru.practicum.shareit.request.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "requests", schema = "public")
public class ItemRequest {

    private Long id;

    private String description;

    private Long requestor;

    private LocalDate created;

}
