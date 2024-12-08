package ru.practicum.shareit.request.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "requests", schema = "public")
public class ItemRequest {

    @Id
    @NotNull
    private Long id;

    @NotNull
    private Long requester;

    @NotNull
    @Future
    private LocalDate created;

    @NotBlank(message = "Request description must not be blank")
    @Size(max = 500)
    private String desciption;

}
