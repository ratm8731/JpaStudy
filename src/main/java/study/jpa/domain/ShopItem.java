package study.jpa.domain;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class ShopItem {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;
}
