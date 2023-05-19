package com.financecrm.webportal.input.uservalidationdocument;

import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserValidationDocumentStatusInput {

    private String documentId;
    private UserValidationDocumentStatus status;
}
