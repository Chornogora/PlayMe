package com.dataart.playme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "member_statuses")
@Data
@NoArgsConstructor
public class MemberStatus {

    @Id
    private String id;

    private String name;

    public enum ExistedStatus {
        LEADER("leader"),
        ADMINISTRATOR("administrator"),
        PLAYER("player"),
        SUBSCRIBER("subscriber");

        private final String value;

        ExistedStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
