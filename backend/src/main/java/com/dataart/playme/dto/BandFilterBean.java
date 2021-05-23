package com.dataart.playme.dto;

import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class BandFilterBean {

    private static final String DEFAULT_SORTING_FIELD = "id";

    private static final String DEFAULT_SORTING_TYPE = "ASC";

    private static final int DEFAULT_LIMIT = 9;

    private String name = StringUtils.EMPTY;

    private Date minCreationDate = DateUtil.getDateFromString(
            Constants.Constraints.MIN_CREATION_DATE);

    private Date maxCreationDate = new Date(System.currentTimeMillis());

    private int limit = DEFAULT_LIMIT;

    private int offset = 0;

    private String sortingField = DEFAULT_SORTING_FIELD;

    private String sortingType = DEFAULT_SORTING_TYPE;
}
