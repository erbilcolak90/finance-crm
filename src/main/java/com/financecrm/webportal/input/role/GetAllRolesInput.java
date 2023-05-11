package com.financecrm.webportal.input.role;

import com.financecrm.webportal.input.PaginationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllRolesInput {

    private PaginationInput paginationInput;
}
