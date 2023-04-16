package com.financecrm.webportal.input.department;

import com.financecrm.webportal.input.PaginationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllDepartmentsInput {

    private PaginationInput pagination;
}
