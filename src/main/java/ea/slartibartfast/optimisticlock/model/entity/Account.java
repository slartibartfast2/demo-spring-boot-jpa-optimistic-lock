package ea.slartibartfast.optimisticlock.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    @Id
    private String id;

    private String name;

    private BigDecimal balance;
}
