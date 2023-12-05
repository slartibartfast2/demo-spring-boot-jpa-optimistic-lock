package ea.slartibartfast.optimisticlock.model.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@MappedSuperclass
public class BaseEntity {

    @Version
    private Long version;
}
