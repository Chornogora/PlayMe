package com.dataart.playme.dto;

import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;

@Data
@NoArgsConstructor
public class FilterBean {

    private static final String DEFAULT_SORTING_FIELD = "id";

    private static final String DEFAULT_SORTING_TYPE = "ASC";

    private static final int DEFAULT_LIMIT = 10;

    private String login = StringUtils.EMPTY;

    private String email = StringUtils.EMPTY;

    private String firstName = StringUtils.EMPTY;

    private String lastName = StringUtils.EMPTY;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdateFrom = DateUtil.getDateFromString(Constants.Constraints.MIN_BIRTHDATE);

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdateTo = DateUtil.getDateFromString(Constants.Constraints.MAX_BIRTHDATE);

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creationDateFrom = DateUtil.getDateFromString(Constants.Constraints.MIN_CREATION_DATE);

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date creationDateTo = new Date(System.currentTimeMillis());

    private String[] roles = Arrays.stream(Role.RoleName.values())
        .map(Role.RoleName::getValue).toArray(String[]::new);

    private String[] statuses = Arrays.stream(Status.StatusName.values())
            .map(Status.StatusName::getValue).toArray(String[]::new);

    private String sortingField = DEFAULT_SORTING_FIELD;

    private String sortingType = DEFAULT_SORTING_TYPE;

    private int offset;

    private int limit = DEFAULT_LIMIT;
}
