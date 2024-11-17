package ru.practicum.shareit.item.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Item {

    @NotNull
    private Integer id;

    @NotNull
    private Integer owner;

    @NotBlank(message = "Name of item must not be blank")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Description of item must not be blank")
    @Size(max = 500)
    private String description;

    @NotNull
    private Boolean available;

    @NotNull
    private Integer request;

}
