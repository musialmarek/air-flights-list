package pl.jgora.aeroklub.airflightslist.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prices")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "native")
    private BigDecimal nativeMember;
    @Column(name = "others")
    private BigDecimal others;

    public Price(int sameForBoth) {
        this.nativeMember = new BigDecimal(sameForBoth);
        this.others = new BigDecimal(sameForBoth);
    }
}
