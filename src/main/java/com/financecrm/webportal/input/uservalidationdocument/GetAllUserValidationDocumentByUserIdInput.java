package com.financecrm.webportal.input.uservalidationdocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUserValidationDocumentByUserIdInput {

    private String userId;
}
