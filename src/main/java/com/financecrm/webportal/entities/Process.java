package com.financecrm.webportal.entities;

import com.financecrm.webportal.enums.ProcessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Processes")
public class Process {

    @Id
    private String id;
    private String userId;
    private String employeeId;
    private ProcessType processType;
    private Date createDate;
}
