package com.gameserver.utils.account.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(schema = "global", name = "account")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "global.account_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "birthday")
    private Date birthday;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "gm_level")
    private int gmLevel;

    @Type(type = "jsonb")
    @Column(name = "data")
    private Map<String, Object> data;

    @Column(name = "data_integrity_key")
    private UUID dataIntegrityKey;

    public Account() {
    }

    @PrePersist
    public void initializeDataIntegrityKey() {
        if (dataIntegrityKey == null) {
            dataIntegrityKey = UUID.randomUUID();
        }
    }

    public Account(@Valid String username, String email, String password, Date birthday) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getGmLevel() {
        return gmLevel;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() { // for debugging
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", createTime=" + createTime +
                ", gmLevel=" + gmLevel +
                ", dataIntegrityKey='" + dataIntegrityKey + '\'' +
                '}';
    }

}