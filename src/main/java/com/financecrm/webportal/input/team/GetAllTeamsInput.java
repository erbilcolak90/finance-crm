package com.financecrm.webportal.input.team;

import com.financecrm.webportal.input.PaginationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTeamsInput {

    private PaginationInput pagination;
}
