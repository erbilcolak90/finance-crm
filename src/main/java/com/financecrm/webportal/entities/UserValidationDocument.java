package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.UserValidationDocumentStatus;
import com.financecrm.webportal.enums.UserValidationDocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("UserValidationDocuments")
public class UserValidationDocument {

    @Id
    private String id;
    private String userId;
    private String url;
    private UserValidationDocumentType type;
    private UserValidationDocumentStatus status;
    private boolean isDeleted;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;

}
