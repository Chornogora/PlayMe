package com.dataart.playme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    private String id;

    private String name;

    @Override
    public String getAuthority() {
        return "ROLE_" + getName();
    }

    public enum RoleName {
        USER("user"),
        ADMINISTRATOR("administrator");

        private final String value;

        RoleName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
