package com.financecrm.webportal.input;

import com.financecrm.webportal.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginationInput {

    private int page;
    private int size;
    private String fieldName;
    private SortBy sortBy;
}
