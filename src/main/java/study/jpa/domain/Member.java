package study.jpa.domain;

import study.jpa.type.RoleType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "member_seq_generator",
        sequenceName = "member_seq",
        initialValue = 1, allocationSize = 100
)
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    private String city;
    private String street;
    private String zipCode;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<Order>();

    @Transient
    private int temp;


    public Member(){}
    public Member(String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
