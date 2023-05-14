package com.financecrm.webportal.input.uservalidationdocument;

import com.financecrm.webportal.enums.UserValidationDocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddUserValidationDocumentInput {

    private String userId;
    private String url;
    private UserValidationDocumentType type;
    private Date date = new Date();

}
