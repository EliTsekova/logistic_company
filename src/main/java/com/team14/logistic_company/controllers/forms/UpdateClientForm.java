package com.team14.logistic_company.controllers.forms;
import com.team14.logistic_company.dtos.ClientDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientForm {

    private Integer id;

    @NotBlank(message = "Phone number cannot be blank!")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 characters!")
    private String phoneNumber;

    // Конвертиране към ClientDto
    public ClientDto toClientDto() {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(this.id);
        clientDto.setPhoneNumber(this.phoneNumber);
        return clientDto;
    }
}