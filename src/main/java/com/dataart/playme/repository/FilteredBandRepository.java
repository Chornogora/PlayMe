package com.dataart.playme.repository;

import com.dataart.playme.dto.BandFilterBean;
import com.dataart.playme.model.Band;

import java.util.List;

public interface FilteredBandRepository {

    List<Band> getBandsFiltered(BandFilterBean filterBean);
}
