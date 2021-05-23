package com.dataart.playme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "band_statuses")
public class BandStatus {

    @Id
    private String id;

    private String name;

    public enum StatusName {
        ACTIVE("active"),
        DISABLED("disabled");

        private final String value;

        StatusName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
