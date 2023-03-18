package com.financecrm.webportal.payload.uservalidationdocument;

import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import com.financecrm.webportal.enums.UserValidationDocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserValidationDocumentPayload {

    private String id;
    private String url;
    private UserValidationDocumentType type;
    private UserValidationDocumentStatus status;
    private Date createDate;
}
